drop table if exists prev_concept;
drop table if exists prev_description;
drop table if exists prev_relationship;
drop table if exists prev_stated_relationship;
drop table if exists prev_textdefinition;
drop table if exists prev_canonical;
drop table if exists prev_references;
drop table if exists prev_componenthistory;
drop table if exists prev_subsetmembers_us;
drop table if exists prev_subsets_us;
drop table if exists prev_subsetmembers_gb;
drop table if exists prev_subsets_gb;
drop table if exists prev_subsetmembers_nonhuman;
drop table if exists prev_subsets_nonhuman;
drop table if exists prev_subsetmembers_vtmvmp;
drop table if exists prev_subsets_vtmvmp;
drop table if exists prev_crossmaptargets_icd9;
drop table if exists prev_crossmapsets_icd9;
drop table if exists prev_crossmaps_icd9;
drop table if exists prev_crossmaptargets_icdo;
drop table if exists prev_crossmapsets_icdo;
drop table if exists prev_crossmaps_icdo;

commit;


create table IF NOT EXISTS prev_concept(
   CONCEPTID numeric(18) not null,
   CONCEPTSTATUS VARCHAR(2),
   FULLYSPECIFIEDNAME VARCHAR(256),
   CTV3ID VARCHAR(5),
   SNOMEDID VARCHAR(8),
   ISPRIMITIVE VARCHAR(1)
);

create unique index idx_id on prev_concept(CONCEPTID);
create index idx_effectivetime on prev_concept(CONCEPTSTATUS);
create index idx_active on prev_concept(FULLYSPECIFIEDNAME);
create index idx_moduleid on prev_concept(CTV3ID);
create index idx_definitionstatusid on prev_concept(SNOMEDID);
create index idx_isprimitive on prev_concept(ISPRIMITIVE);

create table IF NOT EXISTS prev_description(
   DESCRIPTIONID numeric(18) not null,
   DESCRIPTIONSTATUS CHAR(1),
   CONCEPTID numeric(18),
   TERM VARCHAR(512),
   INITIALCAPITALSTATUS VARCHAR(1),   
   DESCRIPTIONTYPE VARCHAR(1),
   LANGUAGECODE VARCHAR(5)    
);


create unique index idx_DESCRIPTIONID on prev_description(DESCRIPTIONID);
create index idx_DESCRIPTIONSTATUS on prev_description(DESCRIPTIONSTATUS);
create index idx_conceptid on prev_description(CONCEPTID);
create index idx_term on prev_description(term);
create index idx_INITIALCAPITALSTATUS on prev_description(INITIALCAPITALSTATUS);
create index idx_DESCRIPTIONTYPE on prev_description(DESCRIPTIONTYPE);
create index idx_languagecode on prev_description(languagecode);



create table IF NOT EXISTS prev_relationship(
   RELATIONSHIPID numeric(18) not null,   
   CONCEPTID1 numeric(18),
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),   
   CHARACTERISTICTYPE VARCHAR(1),   
   REFINABILITY VARCHAR(1),
   RELATIONSHIPGROUP CHAR(1)
);

create index idx_relationshipid on prev_relationship(RELATIONSHIPID);
create index idx_conceptid1 on prev_relationship(CONCEPTID1);
create index idx_relationshiptype on prev_relationship(RELATIONSHIPTYPE);
create index idx_conceptid2 on prev_relationship(CONCEPTID2);
create index idx_characteristictype on prev_relationship(CHARACTERISTICTYPE);
create index idx_refinability on prev_relationship(REFINABILITY);
create index idx_relationshipgroup on prev_relationship(RELATIONSHIPGROUP);



create table IF NOT EXISTS prev_stated_relationship(
   RELATIONSHIPID numeric(18) not null,   
   CONCEPTID1 numeric(18),
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),   
   CHARACTERISTICTYPE VARCHAR(1),   
   REFINABILITY VARCHAR(1),
   RELATIONSHIPGROUP CHAR(1)
);

create unique index idx_relationshipid on prev_stated_relationship(RELATIONSHIPID);
create index idx_conceptid1 on prev_stated_relationship(CONCEPTID1);
create index idx_relationshiptype on prev_stated_relationship(RELATIONSHIPTYPE);
create index idx_conceptid2 on prev_stated_relationship(CONCEPTID2);
create index idx_characteristictype on prev_stated_relationship(CHARACTERISTICTYPE);
create index idx_refinability on prev_stated_relationship(REFINABILITY);
create index idx_relationshipgroup on prev_stated_relationship(RELATIONSHIPGROUP);



create table IF NOT EXISTS prev_textdefinition(
   CONCEPTID numeric(18) not null,
   SNOMEDID VARCHAR(8),
   FULLYSPECIFIEDNAME VARCHAR(256),
   DEFINITION VARCHAR(4000) 
);

	
create unique index idx_conceptid on prev_textdefinition(CONCEPTID);
create index idx_snomedid on prev_textdefinition(SNOMEDID);
create index idx_fsn on prev_textdefinition(FULLYSPECIFIEDNAME);
create index idx_def on prev_textdefinition(DEFINITION);


create table IF NOT EXISTS prev_references(
   COMPONENTID numeric(18) not null,
   REFERENCETYPE VARCHAR(1),
   REFERENCEDID numeric(18)    
);

create index idx_componentid on prev_references(COMPONENTID);
create index idx_referencetype on prev_references(REFERENCETYPE);
create index idx_referencedid on prev_references(REFERENCEDID);




create table IF NOT EXISTS prev_componenthistory(
   COMPONENTID numeric(18) not null,
   RELEASEVERSION CHAR(8),
   CHANGETYPE VARCHAR(1),
   STATUS VARCHAR(1),
   REASON VARCHAR(1024) 
);

create index idx_componentid on prev_componenthistory(COMPONENTID);
create index idx_releaseversion on prev_componenthistory(RELEASEVERSION);
create index idx_changetype on prev_componenthistory(CHANGETYPE);
create index idx_status on prev_componenthistory(STATUS);
create index idx_reason on prev_componenthistory(REASON);	


create table IF NOT EXISTS prev_subsetmembers_us(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on prev_subsetmembers_us(SUBSETID);
create unique index idx_MEMBERID on prev_subsetmembers_us(MEMBERID);
create index idx_MEMBERSTATUS on prev_subsetmembers_us(MEMBERSTATUS);
create index idx_LINKEDID on prev_subsetmembers_us(LINKEDID);


create table IF NOT EXISTS prev_subsetmembers_gb(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on prev_subsetmembers_gb(SUBSETID);
create unique index idx_MEMBERID on prev_subsetmembers_gb(MEMBERID);
create index idx_MEMBERSTATUS on prev_subsetmembers_gb(MEMBERSTATUS);
create index idx_LINKEDID on prev_subsetmembers_gb(LINKEDID);



create table IF NOT EXISTS prev_subsets_us(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create index idx_SUBSETID on prev_subsets_us(SUBSETID);
create unique index idx_SUBSETORIGINALID on prev_subsets_us(SUBSETORIGINALID);
create index idx_SUBSETVERSION on prev_subsets_us(SUBSETVERSION);
create index idx_SUBSETNAME on prev_subsets_us(SUBSETNAME);
create index idx_SUBSETTYPE on prev_subsets_us(SUBSETTYPE);
create index idx_LANGUAGECODE on prev_subsets_us(LANGUAGECODE);
create index idx_REALMID on prev_subsets_us(REALMID);
create index idx_CONTEXTID on prev_subsets_us(CONTEXTID);	


create table IF NOT EXISTS prev_subsets_gb(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);
create index idx_SUBSETID on prev_subsets_gb(SUBSETID);
create unique index idx_SUBSETORIGINALID on prev_subsets_gb(SUBSETORIGINALID);
create index idx_SUBSETVERSION on prev_subsets_gb(SUBSETVERSION);
create index idx_SUBSETNAME on prev_subsets_gb(SUBSETNAME);
create index idx_SUBSETTYPE on prev_subsets_gb(SUBSETTYPE);
create index idx_LANGUAGECODE on prev_subsets_gb(LANGUAGECODE);
create index idx_REALMID on prev_subsets_gb(REALMID);
create index idx_CONTEXTID on prev_subsets_gb(CONTEXTID);	


create table IF NOT EXISTS prev_crossmaptargets_icdo(
   TARGETID numeric(18) not null,
   TARGETSCHEMEID VARCHAR(36) not null,  
   TARGETCODES numeric(18),
   TARGETRULE CHAR(1),
   TARGETADVICE CHAR(1)   
);	

create unique index idx_TARGETID on prev_crossmaptargets_icdo(TARGETID);
create index idx_TARGETSCHEMEID on prev_crossmaptargets_icdo(TARGETSCHEMEID);
create index idx_TARGETCODES on prev_crossmaptargets_icdo(TARGETCODES);
create index idx_TARGETRULE on prev_crossmaptargets_icdo(TARGETRULE);
create index idx_TARGETADVICE on prev_crossmaptargets_icdo(TARGETADVICE);




create table IF NOT EXISTS prev_crossmaps_icdo(
   MAPSETID numeric(18) not null,
   MAPCONCEPTID numeric(18) not null,  
   MAPOPTION CHAR(1) not null,
   MAPPRIORITY CHAR(1),
   MAPTARGETID numeric(18),
   MAPRULE CHAR(1),
   MAPADVICE CHAR(1)   
);
create index idx_MAPSETID on prev_crossmaps_icdo(MAPSETID);
create index idx_MAPCONCEPTID on prev_crossmaps_icdo(MAPCONCEPTID);
create index idx_MAPOPTION on prev_crossmaps_icdo(MAPOPTION);
create index idx_MAPPRIORITY on prev_crossmaps_icdo(MAPPRIORITY);
create index idx_MAPTARGETID on prev_crossmaps_icdo(MAPTARGETID);
create index idx_MAPRULE on prev_crossmaps_icdo(MAPRULE);
create index idx_MAPADVICE on prev_crossmaps_icdo(MAPADVICE);





create table IF NOT EXISTS prev_crossmapsets_icdo(
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


create unique index idx_MAPSETID on prev_crossmapsets_icdo(MAPSETID);
create unique index idx_MAPSETNAME on prev_crossmapsets_icdo(MAPSETNAME);
create index idx_MAPSETTYPE on prev_crossmapsets_icdo(MAPSETTYPE);
create index idx_MAPSETSCHEMEID on prev_crossmapsets_icdo(MAPSETSCHEMEID);
create index idx_MAPSETSCHEMENAME on prev_crossmapsets_icdo(MAPSETSCHEMENAME);
create index idx_MAPSETSCHEMEVERSION on prev_crossmapsets_icdo(MAPSETSCHEMEVERSION);
create index idx_MAPSETREALMID on prev_crossmapsets_icdo(MAPSETREALMID);
create index idx_MAPSETSEPARATOR on prev_crossmapsets_icdo(MAPSETSEPARATOR);
create index idx_MAPSETRULETYPE on prev_crossmapsets_icdo(MAPSETRULETYPE);



create table IF NOT EXISTS prev_canonical(
   CONCEPTID1 numeric(18) not null,
   RELATIONSHIPTYPE numeric(18),
   CONCEPTID2 numeric(18),
   RELATIONSHIPGROUP CHAR(1)
);

create unique index idx_CONCEPTID1 on prev_canonical(CONCEPTID1);
create index idx_RELATIONSHIPTYPE on prev_canonical(RELATIONSHIPTYPE);
create index idx_CONCEPTID2 on prev_canonical(CONCEPTID2);
create index idx_RELATIONSHIPGROUP on prev_canonical(RELATIONSHIPGROUP);



create table IF NOT EXISTS prev_subsetmembers_nonhuman(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on prev_subsetmembers_nonhuman(SUBSETID);
create unique index idx_MEMBERID on prev_subsetmembers_nonhuman(MEMBERID);
create index idx_MEMBERSTATUS on prev_subsetmembers_nonhuman(MEMBERSTATUS);
create index idx_LINKEDID on prev_subsetmembers_nonhuman(LINKEDID);


create table IF NOT EXISTS prev_subsets_nonhuman(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create index idx_SUBSETID on prev_subsets_nonhuman(SUBSETID);
create unique index idx_SUBSETORIGINALID on prev_subsets_nonhuman(SUBSETORIGINALID);
create index idx_SUBSETVERSION on prev_subsets_nonhuman(SUBSETVERSION);
create index idx_SUBSETNAME on prev_subsets_nonhuman(SUBSETNAME);
create index idx_SUBSETTYPE on prev_subsets_nonhuman(SUBSETTYPE);
create index idx_LANGUAGECODE on prev_subsets_nonhuman(LANGUAGECODE);
create index idx_REALMID on prev_subsets_nonhuman(REALMID);
create index idx_CONTEXTID on prev_subsets_nonhuman(CONTEXTID);	


create table IF NOT EXISTS prev_subsetmembers_vtmvmp(
   SUBSETID numeric(18) not null,
   MEMBERID numeric(18) not null,
   MEMBERSTATUS VARCHAR(1),
   LINKEDID VARCHAR(1)
);

create index idx_SUBSETID on prev_subsetmembers_vtmvmp(SUBSETID);
create unique index idx_MEMBERID on prev_subsetmembers_vtmvmp(MEMBERID);
create index idx_MEMBERSTATUS on prev_subsetmembers_vtmvmp(MEMBERSTATUS);
create index idx_LINKEDID on prev_subsetmembers_vtmvmp(LINKEDID);


create table IF NOT EXISTS prev_subsets_vtmvmp(
   SUBSETID numeric(18) not null,
   SUBSETORIGINALID numeric(18) not null,
   SUBSETVERSION CHAR(2) not null,
   SUBSETNAME VARCHAR(128) not null,
   SUBSETTYPE CHAR(1),
   LANGUAGECODE CHAR(5),
   REALMID CHAR(1),
   CONTEXTID CHAR(1)   
);


create unique index idx_SUBSETID on prev_subsets_vtmvmp(SUBSETID);
create unique index idx_SUBSETORIGINALID on prev_subsets_vtmvmp(SUBSETORIGINALID);
create index idx_SUBSETVERSION on prev_subsets_vtmvmp(SUBSETVERSION);
create index idx_SUBSETNAME on prev_subsets_vtmvmp(SUBSETNAME);
create index idx_SUBSETTYPE on prev_subsets_vtmvmp(SUBSETTYPE);
create index idx_LANGUAGECODE on prev_subsets_vtmvmp(LANGUAGECODE);
create index idx_REALMID on prev_subsets_vtmvmp(REALMID);
create index idx_CONTEXTID on prev_subsets_vtmvmp(CONTEXTID);	



create table IF NOT EXISTS prev_crossmaps_icd9(
   MAPSETID numeric(18) not null,
   MAPCONCEPTID numeric(18) not null,  
   MAPOPTION CHAR(1) not null,
   MAPPRIORITY CHAR(1),
   MAPTARGETID numeric(18),
   MAPRULE CHAR(1),
   MAPADVICE CHAR(1)   
);
create unique index idx_MAPSETID on prev_crossmaps_icd9(MAPSETID);
create index idx_MAPCONCEPTID on prev_crossmaps_icd9(MAPCONCEPTID);
create index idx_MAPOPTION on prev_crossmaps_icd9(MAPOPTION);
create index idx_MAPPRIORITY on prev_crossmaps_icd9(MAPPRIORITY);
create index idx_MAPTARGETID on prev_crossmaps_icd9(MAPTARGETID);
create index idx_MAPRULE on prev_crossmaps_icd9(MAPRULE);
create index idx_MAPADVICE on prev_crossmaps_icd9(MAPADVICE);



create table IF NOT EXISTS prev_crossmaptargets_icd9(
   TARGETID numeric(18) not null,
   TARGETSCHEMEID VARCHAR(36) not null,  
   TARGETCODES numeric(18),
   TARGETRULE CHAR(1),
   TARGETADVICE CHAR(1)   
);
create unique index idx_TARGETID on prev_crossmaptargets_icd9(TARGETID);
create index idx_TARGETSCHEMEID on prev_crossmaptargets_icd9(TARGETSCHEMEID);
create index idx_TARGETCODES on prev_crossmaptargets_icd9(TARGETCODES);
create index idx_TARGETRULE on prev_crossmaptargets_icd9(TARGETRULE);
create index idx_TARGETADV on prev_crossmaptargets_icd9(TARGETADVICE);



create table IF NOT EXISTS prev_crossmapsets_icd9(
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


create unique index idx_MAPSETID on prev_crossmapsets_icd9(MAPSETID);
create unique index idx_MAPSETNAME on prev_crossmapsets_icd9(MAPSETNAME);
create index idx_MAPSETTYPE on prev_crossmapsets_icd9(MAPSETTYPE);
create index idx_MAPSETSCHEMEID on prev_crossmapsets_icd9(MAPSETSCHEMEID);
create index idx_MAPSETSCHEMENAME on prev_crossmapsets_icd9(MAPSETSCHEMENAME);
create index idx_MAPSETSCHEMEVERSION on prev_crossmapsets_icd9(MAPSETSCHEMEVERSION);
create index idx_MAPSETREALMID on prev_crossmapsets_icd9(MAPSETREALMID);
create index idx_MAPSETSEPARATOR on prev_crossmapsets_icd9(MAPSETSEPARATOR);
create index idx_MAPSETRULETYPE on prev_crossmapsets_icd9(MAPSETRULETYPE);

commit;    


