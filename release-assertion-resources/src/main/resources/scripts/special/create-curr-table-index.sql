drop table if exists curr_concept_f;
drop table if exists curr_concept_s;
drop table if exists curr_concept_d;
drop table if exists curr_description_f;
drop table if exists curr_description_s;
drop table if exists curr_description_d;
drop table if exists curr_relationship_s;
drop table if exists curr_relationship_d;
drop table if exists curr_relationship_f;
drop table if exists curr_stated_relationship_s;
drop table if exists curr_stated_relationship_d;
drop table if exists curr_stated_relationship_f;
drop table if exists curr_textdefinition_s;
drop table if exists curr_textdefinition_d;
drop table if exists curr_textdefinition_f;
drop table if exists curr_simplerefset_f;
drop table if exists curr_simplerefset_s;
drop table if exists curr_simplerefset_d;
drop table if exists curr_attributevaluerefset_s;
drop table if exists curr_attributevaluerefset_d;
drop table if exists curr_attributevaluerefset_f;
drop table if exists curr_associationrefset_s;
drop table if exists curr_associationrefset_d;
drop table if exists curr_associationrefset_f;
drop table if exists curr_simplemaprefset_s ;
drop table if exists curr_simplemaprefset_f;
drop table if exists curr_simplemaprefset_d;
drop table if exists curr_langrefset_d;
drop table if exists curr_langrefset_s;
drop table if exists curr_langrefset_f;


create table curr_concept_d(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create unique index idx_id on curr_concept_d(id);
create index idx_effectivetime on curr_concept_d(effectivetime);
create index idx_active on curr_concept_d(active);
create index idx_moduleid on curr_concept_d(moduleid);
create index idx_definitionstatusid on curr_concept_d(definitionstatusid);


create table curr_concept_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create index idx_id on curr_concept_f(id);
create index idx_effectivetime on curr_concept_f(effectivetime);
create index idx_active on curr_concept_f(active);
create index idx_moduleid on curr_concept_f(moduleid);
create index idx_definitionstatusid on curr_concept_f(definitionstatusid);



create table curr_concept_s(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create unique index idx_id on curr_concept_s(id);
create index idx_effectivetime on curr_concept_s(effectivetime);
create index idx_active on curr_concept_s(active);
create index idx_moduleid on curr_concept_s(moduleid);
create index idx_definitionstatusid on curr_concept_s(definitionstatusid);



create table curr_description_d(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode VARCHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(255),
   casesignificanceid VARCHAR(18)
);

create unique index idx_id on curr_description_d(id);
create index idx_effectivetime on curr_description_d(effectivetime);
create index idx_active on curr_description_d(active);
create index idx_moduleid on curr_description_d(moduleid);
create index idx_conceptid on curr_description_d(conceptid);
create index idx_languagecode on curr_description_d(languagecode);
create index idx_typeid on curr_description_d(typeid);
create index idx_term on curr_description_d(term);
create index idx_casesignificanceid on curr_description_d(casesignificanceid);


create table curr_description_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode VARCHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(255),
   casesignificanceid VARCHAR(18)
);

create index idx_id on curr_description_f(id);
create index idx_effectivetime on curr_description_f(effectivetime);
create index idx_active on curr_description_f(active);
create index idx_moduleid on curr_description_f(moduleid);
create index idx_conceptid on curr_description_f(conceptid);
create index idx_languagecode on curr_description_f(languagecode);
create index idx_typeid on curr_description_f(typeid);
create index idx_term on curr_description_f(term);
create index idx_casesignificanceid on curr_description_f(casesignificanceid);



create table curr_description_s(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode VARCHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(255),
   casesignificanceid VARCHAR(18)
);

create unique index idx_id on curr_description_s(id);
create index idx_effectivetime on curr_description_s(effectivetime);
create index idx_active on curr_description_s(active);
create index idx_moduleid on curr_description_s(moduleid);
create index idx_conceptid on curr_description_s(conceptid);
create index idx_languagecode on curr_description_s(languagecode);
create index idx_typeid on curr_description_s(typeid);
create index idx_term on curr_description_s(term);
create index idx_casesignificanceid on curr_description_s(casesignificanceid);



create table curr_relationship_d(
    id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create unique index idx_id on curr_relationship_d(id);
create index idx_effectivetime on curr_relationship_d(effectivetime);
create index idx_active on curr_relationship_d(active);
create index idx_moduleid on curr_relationship_d(moduleid);
create index idx_sourceid on curr_relationship_d(sourceid);
create index idx_destinationid on curr_relationship_d(destinationid);
create index idx_relationshipgroup on curr_relationship_d(relationshipgroup);
create index idx_typeid on curr_relationship_d(typeid);
create index idx_characteristictypeid on curr_relationship_d(characteristictypeid);
create index idx_modifierid on curr_relationship_d(modifierid);



create table curr_relationship_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create index idx_id on curr_relationship_f(id);
create index idx_effectivetime on curr_relationship_f(effectivetime);
create index idx_active on curr_relationship_f(active);
create index idx_moduleid on curr_relationship_f(moduleid);
create index idx_sourceid on curr_relationship_f(sourceid);
create index idx_destinationid on curr_relationship_f(destinationid);
create index idx_relationshipgroup on curr_relationship_f(relationshipgroup);
create index idx_typeid on curr_relationship_f(typeid);
create index idx_characteristictypeid on curr_relationship_f(characteristictypeid);
create index idx_modifierid on curr_relationship_f(modifierid);


create table curr_relationship_s(
    id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create unique index idx_id on curr_relationship_s(id);
create index idx_effectivetime on curr_relationship_s(effectivetime);
create index idx_active on curr_relationship_s(active);
create index idx_moduleid on curr_relationship_s(moduleid);
create index idx_sourceid on curr_relationship_s(sourceid);
create index idx_destinationid on curr_relationship_s(destinationid);
create index idx_relationshipgroup on curr_relationship_s(relationshipgroup);
create index idx_typeid on curr_relationship_s(typeid);
create index idx_characteristictypeid on curr_relationship_s(characteristictypeid);
create index idx_modifierid on curr_relationship_s(modifierid);


create table curr_stated_relationship_d(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create unique index idx_id on curr_stated_relationship_d(id);
create index idx_effectivetime on curr_stated_relationship_d(effectivetime);
create index idx_active on curr_stated_relationship_d(active);
create index idx_moduleid on curr_stated_relationship_d(moduleid);
create index idx_sourceid on curr_stated_relationship_d(sourceid);
create index idx_destinationid on curr_stated_relationship_d(destinationid);
create index idx_relationshipgroup on curr_stated_relationship_d(relationshipgroup);
create index idx_typeid on curr_stated_relationship_d(typeid);
create index idx_characteristictypeid on curr_stated_relationship_d(characteristictypeid);
create index idx_modifierid on curr_stated_relationship_d(modifierid);



create table curr_stated_relationship_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create index idx_id on curr_stated_relationship_f(id);
create index idx_effectivetime on curr_stated_relationship_f(effectivetime);
create index idx_active on curr_stated_relationship_f(active);
create index idx_moduleid on curr_stated_relationship_f(moduleid);
create index idx_sourceid on curr_stated_relationship_f(sourceid);
create index idx_destinationid on curr_stated_relationship_f(destinationid);
create index idx_relationshipgroup on curr_stated_relationship_f(relationshipgroup);
create index idx_typeid on curr_stated_relationship_f(typeid);
create index idx_characteristictypeid on curr_stated_relationship_f(characteristictypeid);
create index idx_modifierid on curr_stated_relationship_f(modifierid);



create table curr_stated_relationship_s(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   sourceid VARCHAR(18),
   destinationid VARCHAR(18),
   relationshipgroup CHAR(3),
   typeid VARCHAR(18),
   characteristictypeid VARCHAR(18),
   modifierid VARCHAR(18)
);

create unique index idx_id on curr_stated_relationship_s(id);
create index idx_effectivetime on curr_stated_relationship_s(effectivetime);
create index idx_active on curr_stated_relationship_s(active);
create index idx_moduleid on curr_stated_relationship_s(moduleid);
create index idx_sourceid on curr_stated_relationship_s(sourceid);
create index idx_destinationid on curr_stated_relationship_s(destinationid);
create index idx_relationshipgroup on curr_stated_relationship_s(relationshipgroup);
create index idx_typeid on curr_stated_relationship_s(typeid);
create index idx_characteristictypeid on curr_stated_relationship_s(characteristictypeid);
create index idx_modifierid on curr_stated_relationship_s(modifierid);



create table curr_textdefinition_d(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode CHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(1024),
   casesignificanceid VARCHAR(18)
);

create unique index idx_id on curr_textdefinition_d(id);
create index idx_effectivetime on curr_textdefinition_d(effectivetime);
create index idx_active on curr_textdefinition_d(active);
create index idx_moduleid on curr_textdefinition_d(moduleid);
create index idx_conceptid on curr_textdefinition_d(conceptid);
create index idx_languagecode on curr_textdefinition_d(languagecode);
create index idx_typeid on curr_textdefinition_d(typeid);
create index idx_term on curr_textdefinition_d(term);
create index idx_casesignificanceid on curr_textdefinition_d(casesignificanceid);



create table curr_textdefinition_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode CHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(1024),
   casesignificanceid VARCHAR(18)
);

create index idx_id on curr_textdefinition_f(id);
create index idx_effectivetime on curr_textdefinition_f(effectivetime);
create index idx_active on curr_textdefinition_f(active);
create index idx_moduleid on curr_textdefinition_f(moduleid);
create index idx_conceptid on curr_textdefinition_f(conceptid);
create index idx_languagecode on curr_textdefinition_f(languagecode);
create index idx_typeid on curr_textdefinition_f(typeid);
create index idx_term on curr_textdefinition_f(term);
create index idx_casesignificanceid on curr_textdefinition_f(casesignificanceid);



create table curr_textdefinition_s(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   conceptid VARCHAR(18),
   languagecode CHAR(2),
   typeid VARCHAR(18),
   term VARCHAR(1024),
   casesignificanceid VARCHAR(18)
);

create unique index idx_id on curr_textdefinition_s(id);
create index idx_effectivetime on curr_textdefinition_s(effectivetime);
create index idx_active on curr_textdefinition_s(active);
create index idx_moduleid on curr_textdefinition_s(moduleid);
create index idx_conceptid on curr_textdefinition_s(conceptid);
create index idx_languagecode on curr_textdefinition_s(languagecode);
create index idx_typeid on curr_textdefinition_s(typeid);
create index idx_term on curr_textdefinition_s(term);
create index idx_casesignificanceid on curr_textdefinition_s(casesignificanceid);



create table curr_associationrefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create unique index idx_id on curr_associationrefset_d(id);
create index idx_effectivetime on curr_associationrefset_d(effectivetime);
create index idx_active on curr_associationrefset_d(active);
create index idx_moduleid on curr_associationrefset_d(moduleid);
create index idx_refsetid on curr_associationrefset_d(refsetid);
create index idx_referencedcomponentid on curr_associationrefset_d(referencedcomponentid);
create index idx_targetcomponentid on curr_associationrefset_d(targetcomponentid);



create table curr_associationrefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create index idx_id on curr_associationrefset_f(id);
create index idx_effectivetime on curr_associationrefset_f(effectivetime);
create index idx_active on curr_associationrefset_f(active);
create index idx_moduleid on curr_associationrefset_f(moduleid);
create index idx_refsetid on curr_associationrefset_f(refsetid);
create index idx_referencedcomponentid on curr_associationrefset_f(referencedcomponentid);
create index idx_targetcomponentid on curr_associationrefset_f(targetcomponentid);



create table curr_associationrefset_s(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create unique index idx_id on curr_associationrefset_s(id);
create index idx_effectivetime on curr_associationrefset_s(effectivetime);
create index idx_active on curr_associationrefset_s(active);
create index idx_moduleid on curr_associationrefset_s(moduleid);
create index idx_refsetid on curr_associationrefset_s(refsetid);
create index idx_referencedcomponentid on curr_associationrefset_s(referencedcomponentid);
create index idx_targetcomponentid on curr_associationrefset_s(targetcomponentid);



create table curr_attributevaluerefset_d(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);

create unique index idx_id on curr_attributevaluerefset_d(id);
create index idx_effectivetime on curr_attributevaluerefset_d(effectivetime);
create index idx_active on curr_attributevaluerefset_d(active);
create index idx_moduleid on curr_attributevaluerefset_d(moduleid);
create index idx_refsetid on curr_attributevaluerefset_d(refsetid);
create index idx_referencedcomponentid on curr_attributevaluerefset_d(referencedcomponentid);
create index idx_valueid on curr_attributevaluerefset_d(valueid);



create table curr_attributevaluerefset_f(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);

create index idx_id on curr_attributevaluerefset_f(id);
create index idx_effectivetime on curr_attributevaluerefset_f(effectivetime);
create index idx_active on curr_attributevaluerefset_f(active);
create index idx_moduleid on curr_attributevaluerefset_f(moduleid);
create index idx_refsetid on curr_attributevaluerefset_f(refsetid);
create index idx_referencedcomponentid on curr_attributevaluerefset_f(referencedcomponentid);
create index idx_valueid on curr_attributevaluerefset_f(valueid);



create table curr_attributevaluerefset_s(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);

create unique index idx_id on curr_attributevaluerefset_s(id);
create index idx_effectivetime on curr_attributevaluerefset_s(effectivetime);
create index idx_active on curr_attributevaluerefset_s(active);
create index idx_moduleid on curr_attributevaluerefset_s(moduleid);
create index idx_refsetid on curr_attributevaluerefset_s(refsetid);
create index idx_referencedcomponentid on curr_attributevaluerefset_s(referencedcomponentid);
create index idx_valueid on curr_attributevaluerefset_s(valueid);


create table curr_langrefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create unique index idx_id on curr_langrefset_d(id);
create index idx_effectivetime on curr_langrefset_d(effectivetime);
create index idx_active on curr_langrefset_d(active);
create index idx_moduleid on curr_langrefset_d(moduleid);
create index idx_refsetid on curr_langrefset_d(refsetid);
create index idx_referencedcomponentid on curr_langrefset_d(referencedcomponentid);
create index idx_acceptabilityid on curr_langrefset_d(acceptabilityid);



create table curr_langrefset_f(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create index idx_id on curr_langrefset_f(id);
create index idx_effectivetime on curr_langrefset_f(effectivetime);
create index idx_active on curr_langrefset_f(active);
create index idx_moduleid on curr_langrefset_f(moduleid);
create index idx_refsetid on curr_langrefset_f(refsetid);
create index idx_referencedcomponentid on curr_langrefset_f(referencedcomponentid);
create index idx_acceptabilityid on curr_langrefset_f(acceptabilityid);


create table curr_langrefset_s(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create unique index idx_id on curr_langrefset_s(id);
create index idx_effectivetime on curr_langrefset_s(effectivetime);
create index idx_active on curr_langrefset_s(active);
create index idx_moduleid on curr_langrefset_s(moduleid);
create index idx_refsetid on curr_langrefset_s(refsetid);
create index idx_referencedcomponentid on curr_langrefset_s(referencedcomponentid);
create index idx_acceptabilityid on curr_langrefset_s(acceptabilityid);



create table curr_simplemaprefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create unique index idx_id on curr_simplemaprefset_d(id);
create index idx_effectivetime on curr_simplemaprefset_d(effectivetime);
create index idx_active on curr_simplemaprefset_d(active);
create index idx_moduleid on curr_simplemaprefset_d(moduleid);
create index idx_refsetid on curr_simplemaprefset_d(refsetid);
create index idx_referencedcomponentid on curr_simplemaprefset_d(referencedcomponentid);
create index idx_maptarget on curr_simplemaprefset_d(maptarget);



create table curr_simplemaprefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create index idx_id on curr_simplemaprefset_f(id);
create index idx_effectivetime on curr_simplemaprefset_f(effectivetime);
create index idx_active on curr_simplemaprefset_f(active);
create index idx_moduleid on curr_simplemaprefset_f(moduleid);
create index idx_refsetid on curr_simplemaprefset_f(refsetid);
create index idx_referencedcomponentid on curr_simplemaprefset_f(referencedcomponentid);
create index idx_maptarget on curr_simplemaprefset_f(maptarget);



create table curr_simplemaprefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create unique index idx_id on curr_simplemaprefset_s(id);
create index idx_effectivetime on curr_simplemaprefset_s(effectivetime);
create index idx_active on curr_simplemaprefset_s(active);
create index idx_moduleid on curr_simplemaprefset_s(moduleid);
create index idx_refsetid on curr_simplemaprefset_s(refsetid);
create index idx_referencedcomponentid on curr_simplemaprefset_s(referencedcomponentid);
create index idx_maptarget on curr_simplemaprefset_s(maptarget);



create table curr_simplerefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create unique index idx_id on curr_simplerefset_d(id);
create index idx_effectivetime on curr_simplerefset_d(effectivetime);
create index idx_active on curr_simplerefset_d(active);
create index idx_moduleid on curr_simplerefset_d(moduleid);
create index idx_refsetid on curr_simplerefset_d(refsetid);
create index idx_referencedcomponentid on curr_simplerefset_d(referencedcomponentid);



create table curr_simplerefset_f(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create index idx_id on curr_simplerefset_f(id);
create index idx_effectivetime on curr_simplerefset_f(effectivetime);
create index idx_active on curr_simplerefset_f(active);
create index idx_moduleid on curr_simplerefset_f(moduleid);
create index idx_refsetid on curr_simplerefset_f(refsetid);
create index idx_referencedcomponentid on curr_simplerefset_f(referencedcomponentid);



create table curr_simplerefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create unique index idx_id on curr_simplerefset_s(id);
create index idx_effectivetime on curr_simplerefset_s(effectivetime);
create index idx_active on curr_simplerefset_s(active);
create index idx_moduleid on curr_simplerefset_s(moduleid);
create index idx_refsetid on curr_simplerefset_s(refsetid);
create index idx_referencedcomponentid on curr_simplerefset_s(referencedcomponentid);



create table ct_2013jan_intl.curr_complexmap_s (	
    id varchar2(36) not null enable, 
	effectivetime varchar2(8), 
	active char(1), 
	moduleid varchar2(18), 
	refsetid varchar2(18), 
	referencedcomponentid varchar2(18), 
	mapgroup number, 
	mappriority number(10,0), 
	maprule varchar2(255), 
	mapadvice varchar2(255), 
	maptarget varchar2(255), 
	correlationid varchar2(18)
   ) ;
alter table ct_2013jan_intl.curr_complexmap_s add unique (id) enable;
 
 
create table ct_2013jan_intl.curr_complexmap_d (	
    id varchar2(36) not null enable, 
	effectivetime varchar2(8), 
	active char(1), 
	moduleid varchar2(18), 
	refsetid varchar2(18), 
	referencedcomponentid varchar2(18), 
	mapgroup number, 
	mappriority number(10,0), 
	maprule varchar2(255), 
	mapadvice varchar2(255), 
	maptarget varchar2(255), 
	correlationid varchar2(18)
   ) ;
alter table ct_2013jan_intl.curr_complexmap_d add unique (id) enable;
 
 
create table ct_2013jan_intl.curr_complexmap_f (	
    id varchar2(36) not null enable, 
	effectivetime varchar2(8), 
	active char(1), 
	moduleid varchar2(18), 
	refsetid varchar2(18), 
	referencedcomponentid varchar2(18), 
	mapgroup number, 
	mappriority number(10,0), 
	maprule varchar2(255), 
	mapadvice varchar2(255), 
	maptarget varchar2(255), 
	correlationid varchar2(18)
   );
   
commit;

