package org.veupathdb.service.eda.access.controller;

import org.glassfish.jersey.server.ContainerRequest;
import org.junit.jupiter.api.*;
import org.veupathdb.lib.test.MockUtil;
import org.veupathdb.lib.test.RandUtil;
import org.veupathdb.service.eda.access.service.user.EndUserCreationService;
import org.veupathdb.service.eda.access.service.user.EndUserSearchService;
import org.veupathdb.service.eda.generated.model.ApprovalStatus;
import org.veupathdb.service.eda.generated.model.EndUserCreateRequest;
import org.veupathdb.service.eda.generated.model.EndUserCreateResponse;
import org.veupathdb.service.eda.generated.model.EndUserList;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@DisplayName("EndUserController")
class EndUserControllerTest
{
  private ContainerRequest mockRequest;

  private EndUserController target;

  @BeforeEach
  void setUp() throws Exception {
    mockRequest = mock(ContainerRequest.class);
    target      = new EndUserController();
    target._request = mockRequest;
  }

  @Nested
  @DisplayName("#getDatasetEndUsers(String, int, int ApprovalStatus)")
  class GetDatasetEndUsers
  {
    private EndUserSearchService mock;

    @BeforeEach
    void setUp() throws Exception {
      mock = MockUtil.mockSingleton(EndUserSearchService.class);
    }

    @AfterEach
    void tearDown() throws Exception {
      MockUtil.resetSingleton(EndUserSearchService.class);
    }

    @Test
    @DisplayName("passes parameters through to the backing service")
    void test1() {
      var dsId   = RandUtil.randString();
      var limit  = RandUtil.randLong();
      var offset = RandUtil.randLong();
      var status = ApprovalStatus.DENIED;
      var retVal = mock(EndUserList.class);

      doReturn(retVal).when(mock).findEndUsers(dsId, limit, offset, status, mockRequest);

      assertSame(retVal, target.getDatasetEndUsers(dsId, limit, offset, status).getEntity());

      verify(mock).findEndUsers(dsId, limit, offset, status, mockRequest);
    }
  }

  @Nested
  @DisplayName("#postDatasetEndUsers(EndUserCreateRequest)")
  class PostDatasetEndUsers
  {
    private EndUserCreationService mock;

    @BeforeEach
    void setUp() throws Exception {
      mock = MockUtil.mockSingleton(EndUserCreationService.class);
    }

    @AfterEach
    void tearDown() throws Exception {
      MockUtil.resetSingleton(EndUserCreationService.class);
    }

    @Test
    @DisplayName("passes parameters through to the backing service")
    void test1() {
      var in  = mock(EndUserCreateRequest.class);
      var out = mock(EndUserCreateResponse.class);

      doReturn(out).when(mock).handleUserCreation(in, mockRequest);

      assertSame(out, target.postDatasetEndUsers(in).getEntity());

      verify(mock).handleUserCreation(in, mockRequest);
    }
  }
}
