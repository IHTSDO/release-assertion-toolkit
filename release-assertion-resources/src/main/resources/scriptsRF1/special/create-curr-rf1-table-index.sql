drop table if exists curr_concept;
drop table if exists curr_description;
drop table if exists curr_relationship;
drop table if exists curr_stated_relationship;
drop table if exists curr_textdefinition;
drop table if exists curr_canonical;
drop table if exists curr_references;
drop table if exists curr_componenthistory;
drop table if exists curr_subsetmembers_us;
drop table if exists curr_subsets_us;
drop table if exists curr_subsetmembers_gb;
drop table if exists curr_subsets_gb;
drop table if exists curr_subsetmembers_nonhuman;
drop table if exists curr_subsets_nonhuman;
drop table if exists curr_subsetmembers_vtmvmp;
drop table if exists curr_subsets_vtmvmp;
drop table if exists curr_crossmaptargets_icd9;
drop table if exists curr_crossmapsets_icd9;
drop table if exists curr_crossmaps_icd9;
drop table if exists curr_crossmaptargets_icdo;
drop table if exists curr_crossmapsets_icdo;
drop table if exists curr_crossmaps_icdo;
commit;

create table IF NOT EXISTS curr_concept(
   CONCEPTID numeric(18) not null,
   CONCEPTSTATUS VARCHAR(2),
   FULLYSPECIFIEDNAME VARCHAR(256),
   CTV3ID VARCHAR(5),
   SNOMEDID VARCHAR(8),
   ISPRIMITIVE VARCHAR(1)
);
create unique index idx_id on curr_concept(CONCEPTID);
create index idx_effectivetime on curr_concept(CONCEPTSTATUS);
create index idx_active on curr_concept(FULLYSPECIFIEDNAME);
create index idx_moduleid on curr_concept(CTV3ID);
create index idx_definitionstatusid on curr_concept(SNOMEDID);
create index idx_isprimitive on curr_concept(ISPRIMITIVE);


create table IF NOT EXISTS curr_description(
   DESCRIPTIONID numeric(18) not null,
   DESCRIPTIONSTATUS CHAR(1),
   CONCEPTID numeric(18),
   TERM VARCHAR(512),
   INITIALCAPITALSTATUS VARCHAR(1),   
   DESCRIPTIONTYPE VARCHAR(1),
   LANGUAGECODE VARCHAR(5)   
);
create unique index idx_DESCRIPTIONID on curr_description(DESCRIPTIONID);
create index idx_DESCRIPTIONSTATUS on curr_description(DESCRIPTIONSTATUS);
create index idx_conceptid on curr_description(CONCEPTID);
create index idx_term on curr_description(term);
create index idx_INITIALCAPITALSTATUS on curr_description(INITIALCAPITALSTATUS);
create index idx_DESCRIPTIONTYPE on curr_description(DESCRIPTIONTYPE);
create index idx_languagecode on curr_description(languagecode);

	
create table IF NOT EXISTS curr_relationship(
   RELATIONSHIPID VARCHAR(18)  NOT NULL,   
   CONCEPTID1 numeric(18),
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),   
   CHARACTERISTICTYPE VARCHAR(1),   
   REFINABILITY VARCHAR(1),
   RELATIONSHIPGROUP CHAR(1)
);
create index idx_relationshipid on curr_relationship(RELATIONSHIPID);
create index idx_conceptid1 on curr_relationship(CONCEPTID1);
create index idx_relationshiptype on curr_relationship(RELATIONSHIPTYPE);
create index idx_conceptid2 on curr_relationship(CONCEPTID2);
create index idx_characteristictype on curr_relationship(CHARACTERISTICTYPE);
create index idx_refinability on curr_relationship(REFINABILITY);
create index idx_relationshipgroup on curr_relationship(RELATIONSHIPGROUP);


 

create table IF NOT EXISTS curr_stated_relationship(
   RELATIONSHIPID numeric(18) not null,   
   CONCEPTID1 numeric(18),
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),   
   CHARACTERISTICTYPE VARCHAR(1),   
   REFINABILITY VARCHAR(1),
   RELATIONSHIPGROUP CHAR(1)
);
create unique index idx_relationshipid on curr_stated_relationship(RELATIONSHIPID);
create index idx_conceptid1 on curr_stated_relationship(CONCEPTID1);
create index idx_relationshiptype on curr_stated_relationship(RELATIONSHIPTYPE);
create index idx_conceptid2 on curr_stated_relationship(CONCEPTID2);
create index idx_characteristictype on curr_stated_relationship(CHARACTERISTICTYPE);
create index idx_refinability on curr_stated_relationship(REFINABILITY);
create index idx_relationshipgroup on curr_stated_relationship(RELATIONSHIPGROUP);



create table IF NOT EXISTS curr_textdefinition(
   CONCEPTID numeric(18) not null,
   SNOMEDID VARCHAR(8),
   FULLYSPECIFIEDNAME VARCHAR(256),
   DEFINITION VARCHAR(4000) 
);
create unique index idx_conceptid on curr_textdefinition(CONCEPTID);
create index idx_snomedid on curr_textdefinition(SNOMEDID);
create index idx_fsn on curr_textdefinition(FULLYSPECIFIEDNAME);
create index idx_def on curr_textdefinition(DEFINITION);


create table IF NOT EXISTS curr_references(
   COMPONENTID numeric(18) not null,
   REFERENCETYPE VARCHAR(1),
   REFERENCEDID numeric(18)   
);
create index idx_componentid on curr_references(COMPONENTID);
create index idx_referencetype on curr_references(REFERENCETYPE);
create index idx_referencedid on curr_references(REFERENCEDID);


create table IF NOT EXISTS curr_componenthistory(
   COMPONENTID numeric(18) not null,
   RELEASEVERSION CHAR(8),
   CHANGETYPE VARCHAR(1),
   STATUS VARCHAR(1),
   REASON VARCHAR(1024) 
);
create index idx_componentid on curr_componenthistory(COMPONENTID);
create index idx_releaseversion on curr_componenthistory(RELEASEVERSION);
create index idx_changetype on curr_componenthistory(CHANGETYPE);
create index idx_status on curr_componenthistory(STATUS);
create index idx_reason on curr_componenthistory(REASON);	


create table IF NOT EXISTS curr_canonical(
   CONCEPTID1 numeric(18) not null,
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),
   RELATIONSHIPGROUP CHAR(1)
);
create unique index idx_CONCEPTID1 on curr_canonical(CONCEPTID1);
create index idx_RELATIONSHIPTYPE on curr_canonical(RELATIONSHIPTYPE);
create index idx_CONCEPTID2 on curr_canonical(CONCEPTID2);
create index idx_RELATIONSHIPGROUP on curr_canonical(RELATIONSHIPGROUP);



create table IF NOT EXISTS curr_subsetmembers_us(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);
create index idx_SUBSETID on curr_subsetmembers_us(SUBSETID);
create unique index idx_MEMBERID on curr_subsetmembers_us(MEMBERID);
create index idx_MEMBERSTATUS on curr_subsetmembers_us(MEMBERSTATUS);



create table IF NOT EXISTS curr_subsetmembers_gb(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);
create index idx_SUBSETID on curr_subsetmembers_gb(SUBSETID);
create unique index idx_MEMBERID on curr_subsetmembers_gb(MEMBERID);
create index idx_MEMBERSTATUS on curr_subsetmembers_gb(MEMBERSTATUS);




create table IF NOT EXISTS curr_subsets_us(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);
create index idx_SUBSETID on curr_subsets_us(SUBSETID);
create unique index idx_SUBSETORIGINALID on curr_subsets_us(SUBSETORIGINALID);
create index idx_SUBSETVERSION on curr_subsets_us(SUBSETVERSION);
create index idx_SUBSETNAME on curr_subsets_us(SUBSETNAME);
create index idx_SUBSETTYPE on curr_subsets_us(SUBSETTYPE);
create index idx_LANGUAGECODE on curr_subsets_us(LANGUAGECODE);
create index idx_REALMID on curr_subsets_us(REALMID);
create index idx_CONTEXTID on curr_subsets_us(CONTEXTID);	


create table IF NOT EXISTS curr_subsets_gb(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create index idx_SUBSETID on curr_subsets_gb(SUBSETID);
create unique index idx_SUBSETORIGINALID on curr_subsets_gb(SUBSETORIGINALID);
create index idx_SUBSETVERSION on curr_subsets_gb(SUBSETVERSION);
create index idx_SUBSETNAME on curr_subsets_gb(SUBSETNAME);
create index idx_SUBSETTYPE on curr_subsets_gb(SUBSETTYPE);
create index idx_LANGUAGECODE on curr_subsets_gb(LANGUAGECODE);
create index idx_REALMID on curr_subsets_gb(REALMID);
create index idx_CONTEXTID on curr_subsets_gb(CONTEXTID);	

 

create table IF NOT EXISTS curr_crossmaptargets_icdo(
   TARGETID numeric(18) not null,
   TARGETSCHEMEID VARCHAR(64) not null,  
   TARGETCODES numeric(18),
   TARGETRULE VARCHAR(1),
   TARGETADVICE VARCHAR(1)   
);
create unique index idx_TARGETID on curr_crossmaptargets_icdo(TARGETID);
create index idx_TARGETSCHEMEID on curr_crossmaptargets_icdo(TARGETSCHEMEID);
create index idx_TARGETCODES on curr_crossmaptargets_icdo(TARGETCODES);


create table IF NOT EXISTS curr_crossmaps_icdo(
   MAPSETID numeric(18) not null,
   MAPCONCEPTID numeric(18) not null,  
   MAPOPTION VARCHAR(1) not null,
   MAPPRIORITY VARCHAR(1),
   MAPTARGETID numeric(18),
   MAPRULE VARCHAR(1),
   MAPADVICE VARCHAR(1)   
);

create index idx_MAPSETID on curr_crossmaps_icdo(MAPSETID);
create index idx_MAPCONCEPTID on curr_crossmaps_icdo(MAPCONCEPTID);
create index idx_MAPOPTION on curr_crossmaps_icdo(MAPOPTION);
create index idx_MAPPRIORITY on curr_crossmaps_icdo(MAPPRIORITY);
create index idx_MAPTARGETID on curr_crossmaps_icdo(MAPTARGETID);



create table IF NOT EXISTS curr_crossmapsets_icdo(
   MAPSETID numeric(18) not null,
   MAPSETNAME numeric(18) not null,  
   MAPSETTYPE CHAR(1) not null,
   MAPSETSCHEMEID VARCHAR(36),
   MAPSETSCHEMENAME VARCHAR(36),
   MAPSETSCHEMEVERSION CHAR(4),
   MAPSETREALMID CHAR(1),
   MAPSETSEPARATOR CHAR(1),
   MAPSETRULETYPE CHAR(1) 
);

create unique index idx_MAPSETID on curr_crossmapsets_icdo(MAPSETID);
create unique index idx_MAPSETNAME on curr_crossmapsets_icdo(MAPSETNAME);
create index idx_MAPSETTYPE on curr_crossmapsets_icdo(MAPSETTYPE);
create index idx_MAPSETSCHEMEID on curr_crossmapsets_icdo(MAPSETSCHEMEID);
create index idx_MAPSETSCHEMENAME on curr_crossmapsets_icdo(MAPSETSCHEMENAME);
create index idx_MAPSETSCHEMEVERSION on curr_crossmapsets_icdo(MAPSETSCHEMEVERSION);
create index idx_MAPSETSEPARATOR on curr_crossmapsets_icdo(MAPSETSEPARATOR);



create table IF NOT EXISTS curr_crossmaps_icd9(
   MAPSETID numeric(18) not null,
   MAPCONCEPTID numeric(18) not null,  
   MAPOPTION CHAR(1) not null,
   MAPPRIORITY CHAR(1),
   MAPTARGETID numeric(18),
   MAPRULE CHAR(1),
   MAPADVICE CHAR(1)   
);


create unique index idx_MAPSETID on curr_crossmaps_icd9(MAPSETID);
create unique index idx_MAPCONCEPTID on curr_crossmaps_icd9(MAPCONCEPTID);
create index idx_MAPOPTION on curr_crossmaps_icd9(MAPOPTION);
create index idx_MAPPRIORITY on curr_crossmaps_icd9(MAPPRIORITY);
create index idx_MAPTARGETID on curr_crossmaps_icd9(MAPTARGETID);
create index idx_MAPRULE on curr_crossmaps_icd9(MAPRULE);
create index idx_MAPADVICE on curr_crossmaps_icd9(MAPADVICE);



create table IF NOT EXISTS curr_crossmaptargets_icd9(
   TARGETID numeric(18) not null,
   TARGETSCHEMEID VARCHAR(36) not null,  
   TARGETCODES numeric(18),
   TARGETRULE CHAR(1),
   TARGETADVICE CHAR(1)   
);

				

create unique index idx_TARGETID on curr_crossmaptargets_icd9(TARGETID);
create unique index idx_TARGETSCHEMEID on curr_crossmaptargets_icd9(TARGETSCHEMEID);
create index idx_TARGETCODES on curr_crossmaptargets_icd9(TARGETCODES);
create index idx_TARGETRULE on curr_crossmaptargets_icd9(TARGETRULE);
create index idx_TARGETADVICE on curr_crossmaptargets_icd9(TARGETADVICE);




create table IF NOT EXISTS curr_crossmapsets_icd9(
   MAPSETID numeric(18) not null,
   MAPSETNAME numeric(18) not null,  
   MAPSETTYPE CHAR(1) not null,
   MAPSETSCHEMEID VARCHAR(36),
   MAPSETSCHEMENAME VARCHAR(36),
   MAPSETSCHEMEVERSION CHAR(4),
   MAPSETREALMID CHAR(1),
   MAPSETSEPARATOR CHAR(1),
   MAPSETRULETYPE CHAR(1) 
);


create unique index idx_MAPSETID on curr_crossmapsets_icd9(MAPSETID);
create unique index idx_MAPSETNAME on curr_crossmapsets_icd9(MAPSETNAME);
create index idx_MAPSETTYPE on curr_crossmapsets_icd9(MAPSETTYPE);
create index idx_MAPSETSCHEMEID on curr_crossmapsets_icd9(MAPSETSCHEMEID);
create index idx_MAPSETSCHEMENAME on curr_crossmapsets_icd9(MAPSETSCHEMENAME);
create index idx_MAPSETSCHEMEVERSION on curr_crossmapsets_icd9(MAPSETSCHEMEVERSION);
create index idx_MAPSETREALMID on curr_crossmapsets_icd9(MAPSETREALMID);
create index idx_MAPSETSEPARATOR on curr_crossmapsets_icd9(MAPSETSEPARATOR);
create index idx_MAPSETRULETYPE on curr_crossmapsets_icd9(MAPSETRULETYPE);



create table IF NOT EXISTS curr_subsetmembers_nonhuman(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on curr_subsetmembers_nonhuman(SUBSETID);
create unique index idx_MEMBERID on curr_subsetmembers_nonhuman(MEMBERID);
create index idx_MEMBERSTATUS on curr_subsetmembers_nonhuman(MEMBERSTATUS);
create index idx_LINKEDID on curr_subsetmembers_nonhuman(LINKEDID);


create table IF NOT EXISTS curr_subsets_nonhuman(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create index idx_SUBSETID on curr_subsets_nonhuman(SUBSETID);
create unique index idx_SUBSETORIGINALID on curr_subsets_nonhuman(SUBSETORIGINALID);
create index idx_SUBSETVERSION on curr_subsets_nonhuman(SUBSETVERSION);
create index idx_SUBSETNAME on curr_subsets_nonhuman(SUBSETNAME);
create index idx_SUBSETTYPE on curr_subsets_nonhuman(SUBSETTYPE);
create index idx_LANGUAGECODE on curr_subsets_nonhuman(LANGUAGECODE);
create index idx_REALMID on curr_subsets_nonhuman(REALMID);
create index idx_CONTEXTID on curr_subsets_nonhuman(CONTEXTID);	



create table IF NOT EXISTS curr_subsetmembers_vtmvmp(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on curr_subsetmembers_vtmvmp(SUBSETID);
create unique index idx_MEMBERID on curr_subsetmembers_vtmvmp(MEMBERID);
create index idx_MEMBERSTATUS on curr_subsetmembers_vtmvmp(MEMBERSTATUS);
create index idx_LINKEDID on curr_subsetmembers_vtmvmp(LINKEDID);


create table IF NOT EXISTS curr_subsets_vtmvmp(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create unique index idx_SUBSETID on curr_subsets_vtmvmp(SUBSETID);
create unique index idx_SUBSETORIGINALID on curr_subsets_vtmvmp(SUBSETORIGINALID);
create index idx_SUBSETVERSION on curr_subsets_vtmvmp(SUBSETVERSION);
create index idx_SUBSETNAME on curr_subsets_vtmvmp(SUBSETNAME);
create index idx_SUBSETTYPE on curr_subsets_vtmvmp(SUBSETTYPE);
create index idx_LANGUAGECODE on curr_subsets_vtmvmp(LANGUAGECODE);
create index idx_REALMID on curr_subsets_vtmvmp(REALMID);
create index idx_CONTEXTID on curr_subsets_vtmvmp(CONTEXTID);	



commit;    

