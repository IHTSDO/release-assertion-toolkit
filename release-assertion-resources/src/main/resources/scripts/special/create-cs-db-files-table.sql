drop table if exists cs_attributes;
drop table if exists cs_descriptions;
drop table if exists cs_relationships;


create table cs_attributes(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   definitionstatusid VARCHAR(18),
   concept_uuid VARCHAR(36),
   isDefined_uuid VARCHAR(36),
   author VARCHAR(36),
   path VARCHAR(36),
   commitTime VARCHAR(18)
);


create table cs_descriptions(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   conceptId VARCHAR(36),
   languageCode VARCHAR(5),
   typeId VARCHAR(36),
   term VARCHAR(250),
   caseSignificanceId VARCHAR(36),
   description_uuid VARCHAR(36),
   concept_uuid VARCHAR(36),
   type_uuid VARCHAR(36),
   isCaseSig_uuid VARCHAR(36),
   author VARCHAR(36),
   path VARCHAR(36),
   commitTime VARCHAR(18)
);


create table cs_relationships(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   sourceId VARCHAR(36),
   destinationId VARCHAR(5),
   relationshipGroup VARCHAR(2),
   typeId VARCHAR(36),
   characteristicTypeId VARCHAR(36),
   modifierId VARCHAR(36),
   relationship_uuid VARCHAR(36),
   source_uuid VARCHAR(36),
   target_uuid VARCHAR(36),
   type_uuid VARCHAR(36),
   characteristic_uuid VARCHAR(36),
   author VARCHAR(36),
   path VARCHAR(36),
   commitTime VARCHAR(18)
);