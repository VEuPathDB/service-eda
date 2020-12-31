-- the EDA service doesn't need to know much about the Study, because the WDK will serve that data
-- the abbrev would be used in the name of the tall and ancestors tables
-- the study_id is a stable ID
create table Study (
  stable_id varchar(50) not null,
  display_name varchar(30) not null,
  internal_abbrev varchar(30),
  PRIMARY KEY (stable_id)
);
alter table Study add unique (display_name);
alter table Study add unique (internal_abbrev);;

-- The entities types per study, eg Participants, and their tree relationships
CREATE TABLE EntityTypeGraph (
  stable_id varchar(50) not null,
  study_stable_id varchar(50) not null,
  parent_stable_id varchar(30),
  internal_abbrev varchar(20),
  display_name varchar(30) not null,
  display_name_plural varchar(30),
  description varchar(100),
);
alter table EntityTypeGraph add unique (study_stable_id, stable_id);
alter table EntityTypeGraph add unique (study_stable_id, internal_abbrev);
alter table EntityTypeGraph add unique (display_name, study_stable_id, stable_id);
ALTER TABLE EntityTypeGraph
   ADD FOREIGN KEY (study_stable_id) REFERENCES Study (stable_id);

--------------------------------------------------------------------------------------------------
-- the following tables are per-study.  Their name is formed using the study ID, in this case ds2324.
-- Each entity type gets two tables:
--    AttributeValue_XXXXX_YYYYY
--    Ancestors_XXXXX_YYYYY
--  where XXXXX is the study internal_abbrev and YYYYY is the entity's internal_abbrev
--------------------------------------------------------------------------------------------------
create table Attribute_ds2324_Hshld (
  stable_id varchar(30),
  ontology_term_id integer,
  parent_stable_id varchar(30),
  provider_label varchar(30) not null,
  display_name varchar(30) not null,
  term_type varchar(20),
  has_value integer,
  data_type varchar(10),
  has_multiple_values_per_entity integer,
  data_shape varchar(20),
  unit varchar (30),
  unit_ontology_term_id integer,
  precision integer,
  PRIMARY KEY (stable_id)
);

create table AttributeValue_ds2324_Hshld (
  hshld_id varchar(20),
  attribute_stable_id varchar(30),
  number_value integer, 
  string_value varchar(100),
  date_value varchar(30),
);
ALTER TABLE AttributeValue_ds2324_Hshld
   ADD FOREIGN KEY (attribute_stable_id) REFERENCES Attribute_ds2324_Hshld (stable_id);
CREATE INDEX AttributeValue_ds2324_Hshld_i1
ON AttributeValue_ds2324_Hshld (attribute_stable_id, hshld_id, number_value, string_value, date_value);
-- for test db only
CREATE unique INDEX AttributeValue_ds2324_Hshld_i2 ON AttributeValue_ds2324_Hshld (attribute_stable_id, hshld_id);

create table Ancestors_ds2324_Hshld (
  hshld_id integer,
  PRIMARY KEY (Hshld_id)
);

-----------------------------------------------------------------------------
create table Attribute_ds2324_HshldObsrvtn (
  stable_id varchar(30),
  ontology_term_id integer,
  parent_stable_id varchar(30),
  provider_label varchar(30) not null,
  display_name varchar(30) not null,
  term_type varchar(20),
  has_value integer,
  data_type varchar(10),
  has_multiple_values_per_entity integer,
  data_shape varchar(20),
  unit varchar (30),
  unit_ontology_term_id integer,
  precision integer,
  PRIMARY KEY (stable_id)
);

create table AttributeValue_ds2324_HshldObsrvtn (
  HshldObsrvtn_id integer,
  attribute_stable_id varchar(30),
  number_value integer, 
  string_value varchar(100),
  date_value varchar(30),
);
ALTER TABLE AttributeValue_ds2324_HshldObsrvtn
   ADD FOREIGN KEY (attribute_stable_id) REFERENCES Attribute_ds2324_HshldObsrvtn (stable_id);
CREATE INDEX AttributeValue_ds2324_HshldObsrvtn_i1
ON AttributeValue_ds2324_HshldObsrvtn (attribute_stable_id, HshldObsrvtn_id, number_value, string_value, date_value);
-- for test db only
CREATE unique INDEX AttributeValue_ds2324_HshldObsrvtn_i2 ON AttributeValue_ds2324_HshldObsrvtn (attribute_stable_id, HshldObsrvtn_id);

create table Ancestors_ds2324_HshldObsrvtn (
  HshldObsrvtn_id integer,
  Hshld_id integer,
  PRIMARY KEY (Hshld_id)
);

-----------------------------------------------------------------------------
create table Attribute_ds2324_Prtcpnt (
  stable_id varchar(30),
  ontology_term_id integer,
  parent_stable_id varchar(30),
  provider_label varchar(30) not null,
  display_name varchar(30) not null,
  term_type varchar(20),
  has_value integer,
  data_type varchar(10),
  has_multiple_values_per_entity integer,
  data_shape varchar(20),
  unit varchar (30),
  unit_ontology_term_id integer,
  precision integer,
  PRIMARY KEY (stable_id)
);
create table AttributeValue_ds2324_Prtcpnt (
  prtcpnt_id integer,
  attribute_stable_id varchar(30),
  number_value integer,
  string_value varchar(100),
  date_value varchar(30),
);
ALTER TABLE AttributeValue_ds2324_Prtcpnt
   ADD FOREIGN KEY (attribute_stable_id) REFERENCES Attribute_ds2324_Prtcpnt (stable_id);
CREATE INDEX AttributeValue_ds2324_Prtcpnt_i1
ON AttributeValue_ds2324_Prtcpnt (attribute_stable_id, prtcpnt_id, number_value, string_value, date_value);
-- for test db only
CREATE unique INDEX AttributeValue_ds2324_Prtcpnt_i2 ON AttributeValue_ds2324_Prtcpnt (attribute_stable_id, prtcpnt_id);

create table Ancestors_ds2324_Prtcpnt (
  prtcpnt_id integer,
  hshld_id integer,
  PRIMARY KEY (prtcpnt_id)
);

-----------------------------------------------------------------------------
create table Attribute_ds2324_PrtcpntObsrvtn (
  stable_id varchar(30),
  ontology_term_id integer,
  parent_stable_id varchar(30),
  provider_label varchar(30) not null,
  display_name varchar(30) not null,
  term_type varchar(20),
  has_value integer,
  data_type varchar(10),
  has_multiple_values_per_entity integer,
  data_shape varchar(20),
  unit varchar (30),
  unit_ontology_term_id integer,
  precision integer,
  PRIMARY KEY (stable_id)
);

create table AttributeValue_ds2324_PrtcpntObsrvtn (
  PrtcpntObsrvtn_id integer,
  attribute_stable_id varchar(30),
  number_value integer, 
  string_value varchar(100),
  date_value varchar(30),
);
ALTER TABLE AttributeValue_ds2324_PrtcpntObsrvtn
   ADD FOREIGN KEY (attribute_stable_id) REFERENCES Attribute_ds2324_PrtcpntObsrvtn (stable_id);
CREATE INDEX AttributeValue_ds2324_PrtcpntObsrvtn_i1
ON AttributeValue_ds2324_PrtcpntObsrvtn (attribute_stable_id, PrtcpntObsrvtn_id, number_value, string_value, date_value);
-- for test db only
CREATE unique INDEX AttributeValue_ds2324_PrtcpntObsrvtn_i2 ON AttributeValue_ds2324_PrtcpntObsrvtn (attribute_stable_id, PrtcpntObsrvtn_id);

create table Ancestors_ds2324_PrtcpntObsrvtn (
  PrtcpntObsrvtn_id integer,
  Prtcpnt_id integer,
  Hshld_id integer,
  PRIMARY KEY (PrtcpntObsrvtn_id)
);



