-- The SCHEMA may only be created by the DBA or a superuser.
CREATE SCHEMA IF NOT EXISTS edauser AUTHORIZATION userdb_owner;
GRANT USAGE ON SCHEMA edauser TO edauser_r;


-- USERS table
CREATE TABLE edauser.users (
  user_id INTEGER NOT NULL,
  is_guest BOOLEAN NOT NULL,
  preferences TEXT,
  PRIMARY KEY (user_id)
);

-- Make sure the table is owned by userdb_owner role, not the user running the script.
ALTER TABLE edauser.users OWNER TO userdb_owner;
GRANT SELECT on edauser.users TO edauser_r;
GRANT INSERT, UPDATE, DELETE on edauser.users TO edauser_w;

-- ANALYSIS table
CREATE TABLE edauser.analysis (
  analysis_id VARCHAR(50) NOT NULL,
  user_id INTEGER NOT NULL,
  study_id VARCHAR(50) NOT NULL,
  study_version VARCHAR(50),
  api_version VARCHAR(50),
  display_name VARCHAR(50) NOT NULL,
  description TEXT,
  creation_time TIMESTAMP NOT NULL,
  modification_time TIMESTAMP NOT NULL,
  is_public BOOLEAN NOT NULL,
  num_filters INTEGER NOT NULL,
  num_computations INTEGER NOT NULL,
  num_visualizations INTEGER NOT NULL,
  analysis_descriptor TEXT,
  notes TEXT,
  provenance TEXT,
  PRIMARY KEY (analysis_id),
  FOREIGN KEY (user_id) REFERENCES edauser.users (user_id)
);

-- Make sure the table is owned by userdb_owner role, not the user running the script.
ALTER TABLE edauser.analysis OWNER TO userdb_owner;
GRANT SELECT on edauser.analysis TO edauser_r;
GRANT INSERT, UPDATE, DELETE on edauser.analysis TO edauser_w;

CREATE INDEX analysis_user_id_idx ON edauser.analysis (user_id);
