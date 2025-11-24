# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#   Build Service & Dependencies
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
FROM veupathdb/alpine-dev-base:jdk-21-gradle-8.7 AS prep

LABEL service="eda-build"

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

WORKDIR /workspace

RUN apk add --no-cache npm \
  && npm install -gs raml2html raml2html-modern-theme

# copy files required to build dev environment and fetch dependencies
COPY ["build.gradle.kts", "settings.gradle.kts", "startup.sh", "gradlew", "./"]
COPY gradle/ gradle/

# download raml tools (these never change)
RUN ./gradlew install-raml-4-jax-rs install-raml-merge

# download project dependencies in advance
RUN ./gradlew download-dependencies

# copy raml over for merging, then perform code and documentation generation
COPY api.raml ./
COPY schema schema
RUN ./gradlew generate-jaxrs generate-raml-docs

# copy remaining files
COPY src/ src

# build the project
RUN ./gradlew clean test shadowJar


# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#   Run the service
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
FROM amazoncorretto:21-alpine3.20

LABEL service="eda-service"

RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/America/New_York /etc/localtime \
    && echo "America/New_York" > /etc/timezone

ENV JAVA_HOME=/opt/jdk \
    PATH=/opt/jdk/bin:$PATH \
    JVM_MEM_ARGS="" \
    JVM_ARGS=""

COPY startup.sh startup.sh

COPY --from=prep /workspace/build/libs/service.jar /service.jar

CMD ./startup.sh
