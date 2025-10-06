CREATE SCHEMA IF NOT EXISTS edauser;
GRANT USAGE ON SCHEMA edauser TO comm_wdk_w;

-- USERS table
CREATE TABLE edauser.users (
  user_id INTEGER NOT NULL,
  is_guest INTEGER NOT NULL,
  preferences TEXT, 
  PRIMARY KEY (user_id)
);

-- Grant privileges (PostgreSQL syntax)
GRANT SELECT, INSERT, UPDATE, DELETE ON edauser.users TO comm_wdk_w;

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
  is_public INTEGER NOT NULL,
  num_filters INTEGER NOT NULL,
  num_computations INTEGER NOT NULL,
  num_visualizations INTEGER NOT NULL,
  analysis_descriptor TEXT,
  notes TEXT,
  provenance TEXT,
  PRIMARY KEY (analysis_id),
  FOREIGN KEY (user_id) REFERENCES edauser.users (user_id)
);

CREATE INDEX analysis_user_id_idx ON edauser.analysis (user_id);
