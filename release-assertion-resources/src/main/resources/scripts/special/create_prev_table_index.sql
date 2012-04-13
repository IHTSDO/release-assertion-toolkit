use localpostqatest;

create table prev_associationrefset_d(
    id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create unique index idx_id on prev_associationrefset_d(id);
create index idx_effectivetime on prev_associationrefset_d(effectivetime);
create index idx_active on prev_associationrefset_d(active);
create index idx_moduleid on prev_associationrefset_d(moduleid);
create index idx_refsetid on prev_associationrefset_d(refsetid);
create index idx_referencedcomponentid on prev_associationrefset_d(referencedcomponentid);
create index idx_targetcomponentid on prev_associationrefset_d(targetcomponentid);



create table prev_associationrefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create index idx_id on prev_associationrefset_f(id);
create index idx_effectivetime on prev_associationrefset_f(effectivetime);
create index idx_active on prev_associationrefset_f(active);
create index idx_moduleid on prev_associationrefset_f(moduleid);
create index idx_refsetid on prev_associationrefset_f(refsetid);
create index idx_referencedcomponentid on prev_associationrefset_f(referencedcomponentid);
create index idx_targetcomponentid on prev_associationrefset_f(targetcomponentid);



create table prev_associationrefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   targetcomponentid VARCHAR(18)
);

create unique index idx_id on prev_associationrefset_s(id);
create index idx_effectivetime on prev_associationrefset_s(effectivetime);
create index idx_active on prev_associationrefset_s(active);
create index idx_moduleid on prev_associationrefset_s(moduleid);
create index idx_refsetid on prev_associationrefset_s(refsetid);
create index idx_referencedcomponentid on prev_associationrefset_s(referencedcomponentid);
create index idx_targetcomponentid on prev_associationrefset_s(targetcomponentid);



create table prev_attributevaluerefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);
    
create unique index idx_id on prev_attributevaluerefset_d(id);
create index idx_effectivetime on prev_attributevaluerefset_d(effectivetime);
create index idx_active on prev_attributevaluerefset_d(active);
create index idx_moduleid on prev_attributevaluerefset_d(moduleid);
create index idx_refsetid on prev_attributevaluerefset_d(refsetid);
create index idx_referencedcomponentid on prev_attributevaluerefset_d(referencedcomponentid);
create index idx_valueid on prev_attributevaluerefset_d(valueid);



create table prev_attributevaluerefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);

create index idx_id on prev_attributevaluerefset_f(id);
create index idx_effectivetime on prev_attributevaluerefset_f(effectivetime);
create index idx_active on prev_attributevaluerefset_f(active);
create index idx_moduleid on prev_attributevaluerefset_f(moduleid);
create index idx_refsetid on prev_attributevaluerefset_f(refsetid);
create index idx_referencedcomponentid on prev_attributevaluerefset_f(referencedcomponentid);
create index idx_valueid on prev_attributevaluerefset_f(valueid);



create table prev_attributevaluerefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   valueid VARCHAR(18)
);

create unique index idx_id on prev_attributevaluerefset_s(id);
create index idx_effectivetime on prev_attributevaluerefset_s(effectivetime);
create index idx_active on prev_attributevaluerefset_s(active);
create index idx_moduleid on prev_attributevaluerefset_s(moduleid);
create index idx_refsetid on prev_attributevaluerefset_s(refsetid);
create index idx_referencedcomponentid on prev_attributevaluerefset_s(referencedcomponentid);
create index idx_valueid on prev_attributevaluerefset_s(valueid);



create table prev_concept_d(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create unique index idx_id on prev_concept_d(id);
create index idx_effectivetime on prev_concept_d(effectivetime);
create index idx_active on prev_concept_d(active);
create index idx_moduleid on prev_concept_d(moduleid);
create index idx_definitionstatusid on prev_concept_d(definitionstatusid);



create table prev_concept_f(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create index idx_id on prev_concept_f(id);
create index idx_effectivetime on prev_concept_f(effectivetime);
create index idx_active on prev_concept_f(active);
create index idx_moduleid on prev_concept_f(moduleid);
create index idx_definitionstatusid on prev_concept_f(definitionstatusid);

    

create table prev_concept_s(
   id VARCHAR(18) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   definitionstatusid VARCHAR(18)
);

create unique index idx_id on prev_concept_s(id);
create index idx_effectivetime on prev_concept_s(effectivetime);
create index idx_active on prev_concept_s(active);
create index idx_moduleid on prev_concept_s(moduleid);
create index idx_definitionstatusid on prev_concept_s(definitionstatusid);



create table prev_description_d(
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

create unique index idx_id on prev_description_d(id);
create index idx_effectivetime on prev_description_d(effectivetime);
create index idx_active on prev_description_d(active);
create index idx_moduleid on prev_description_d(moduleid);
create index idx_conceptid on prev_description_d(conceptid);
create index idx_languagecode on prev_description_d(languagecode);
create index idx_typeid on prev_description_d(typeid);
create index idx_term on prev_description_d(term);
create index idx_casesignificanceid on prev_description_d(casesignificanceid);

    

create table prev_description_f(
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

create index idx_id on prev_description_f(id);
create index idx_effectivetime on prev_description_f(effectivetime);
create index idx_active on prev_description_f(active);
create index idx_moduleid on prev_description_f(moduleid);
create index idx_conceptid on prev_description_f(conceptid);
create index idx_languagecode on prev_description_f(languagecode);
create index idx_typeid on prev_description_f(typeid);
create index idx_term on prev_description_f(term);
create index idx_casesignificanceid on prev_description_f(casesignificanceid);


    
create table prev_description_s(
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

create unique index idx_id on prev_description_s(id);
create index idx_effectivetime on prev_description_s(effectivetime);
create index idx_active on prev_description_s(active);
create index idx_moduleid on prev_description_s(moduleid);
create index idx_conceptid on prev_description_s(conceptid);
create index idx_languagecode on prev_description_s(languagecode);
create index idx_typeid on prev_description_s(typeid);
create index idx_term on prev_description_s(term);
create index idx_casesignificanceid on prev_description_s(casesignificanceid);



create table prev_langrefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create unique index idx_id on prev_langrefset_d(id);
create index idx_effectivetime on prev_langrefset_d(effectivetime);
create index idx_active on prev_langrefset_d(active);
create index idx_moduleid on prev_langrefset_d(moduleid);
create index idx_refsetid on prev_langrefset_d(refsetid);
create index idx_referencedcomponentid on prev_langrefset_d(referencedcomponentid);
create index idx_acceptabilityid on prev_langrefset_d(acceptabilityid);



create table prev_langrefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create index idx_id on prev_langrefset_f(id);
create index idx_effectivetime on prev_langrefset_f(effectivetime);
create index idx_active on prev_langrefset_f(active);
create index idx_moduleid on prev_langrefset_f(moduleid);
create index idx_refsetid on prev_langrefset_f(refsetid);
create index idx_referencedcomponentid on prev_langrefset_f(referencedcomponentid);
create index idx_acceptabilityid on prev_langrefset_f(acceptabilityid);


create table prev_langrefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   acceptabilityid VARCHAR(18)
);

create unique index idx_id on prev_langrefset_s(id);
create index idx_effectivetime on prev_langrefset_s(effectivetime);
create index idx_active on prev_langrefset_s(active);
create index idx_moduleid on prev_langrefset_s(moduleid);
create index idx_refsetid on prev_langrefset_s(refsetid);
create index idx_referencedcomponentid on prev_langrefset_s(referencedcomponentid);
create index idx_acceptabilityid on prev_langrefset_s(acceptabilityid);

    
    
    
create table prev_relationship_d(
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

create unique index idx_id on prev_relationship_d(id);
create index idx_effectivetime on prev_relationship_d(effectivetime);
create index idx_active on prev_relationship_d(active);
create index idx_moduleid on prev_relationship_d(moduleid);
create index idx_sourceid on prev_relationship_d(sourceid);
create index idx_destinationid on prev_relationship_d(destinationid);
create index idx_relationshipgroup on prev_relationship_d(relationshipgroup);
create index idx_typeid on prev_relationship_d(typeid);
create index idx_characteristictypeid on prev_relationship_d(characteristictypeid);
create index idx_modifierid on prev_relationship_d(modifierid);

    
    
create table prev_relationship_f(
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

create index idx_id on prev_relationship_f(id);
create index idx_effectivetime on prev_relationship_f(effectivetime);
create index idx_active on prev_relationship_f(active);
create index idx_moduleid on prev_relationship_f(moduleid);
create index idx_sourceid on prev_relationship_f(sourceid);
create index idx_destinationid on prev_relationship_f(destinationid);
create index idx_relationshipgroup on prev_relationship_f(relationshipgroup);
create index idx_typeid on prev_relationship_f(typeid);
create index idx_characteristictypeid on prev_relationship_f(characteristictypeid);
create index idx_modifierid on prev_relationship_f(modifierid);


    
    
create table prev_relationship_s(
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

create unique index idx_id on prev_relationship_s(id);
create index idx_effectivetime on prev_relationship_s(effectivetime);
create index idx_active on prev_relationship_s(active);
create index idx_moduleid on prev_relationship_s(moduleid);
create index idx_sourceid on prev_relationship_s(sourceid);
create index idx_destinationid on prev_relationship_s(destinationid);
create index idx_relationshipgroup on prev_relationship_s(relationshipgroup);
create index idx_typeid on prev_relationship_s(typeid);
create index idx_characteristictypeid on prev_relationship_s(characteristictypeid);
create index idx_modifierid on prev_relationship_s(modifierid);


    

create table prev_simplemaprefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create unique index idx_id on prev_simplemaprefset_d(id);
create index idx_effectivetime on prev_simplemaprefset_d(effectivetime);
create index idx_active on prev_simplemaprefset_d(active);
create index idx_moduleid on prev_simplemaprefset_d(moduleid);
create index idx_refsetid on prev_simplemaprefset_d(refsetid);
create index idx_referencedcomponentid on prev_simplemaprefset_d(referencedcomponentid);
create index idx_maptarget on prev_simplemaprefset_d(maptarget);





create table prev_simplemaprefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create index idx_id on prev_simplemaprefset_f(id);
create index idx_effectivetime on prev_simplemaprefset_f(effectivetime);
create index idx_active on prev_simplemaprefset_f(active);
create index idx_moduleid on prev_simplemaprefset_f(moduleid);
create index idx_refsetid on prev_simplemaprefset_f(refsetid);
create index idx_referencedcomponentid on prev_simplemaprefset_f(referencedcomponentid);
create index idx_maptarget on prev_simplemaprefset_f(maptarget);




create table prev_simplemaprefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18),
   maptarget VARCHAR(32)
);

create unique index idx_id on prev_simplemaprefset_s(id);
create index idx_effectivetime on prev_simplemaprefset_s(effectivetime);
create index idx_active on prev_simplemaprefset_s(active);
create index idx_moduleid on prev_simplemaprefset_s(moduleid);
create index idx_refsetid on prev_simplemaprefset_s(refsetid);
create index idx_referencedcomponentid on prev_simplemaprefset_s(referencedcomponentid);
create index idx_maptarget on prev_simplemaprefset_s(maptarget);




create table prev_simplerefset_d(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create unique index idx_id on prev_simplerefset_d(id);
create index idx_effectivetime on prev_simplerefset_d(effectivetime);
create index idx_active on prev_simplerefset_d(active);
create index idx_moduleid on prev_simplerefset_d(moduleid);
create index idx_refsetid on prev_simplerefset_d(refsetid);
create index idx_referencedcomponentid on prev_simplerefset_d(referencedcomponentid);


    

create table prev_simplerefset_f(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create index idx_id on prev_simplerefset_f(id);
create index idx_effectivetime on prev_simplerefset_f(effectivetime);
create index idx_active on prev_simplerefset_f(active);
create index idx_moduleid on prev_simplerefset_f(moduleid);
create index idx_refsetid on prev_simplerefset_f(refsetid);
create index idx_referencedcomponentid on prev_simplerefset_f(referencedcomponentid);




create table prev_simplerefset_s(
   id VARCHAR(36) not null,
   effectivetime CHAR(8),
   active CHAR(1),
   moduleid VARCHAR(18),
   refsetid VARCHAR(18),
   referencedcomponentid VARCHAR(18)
);

create unique index idx_id on prev_simplerefset_s(id);
create index idx_effectivetime on prev_simplerefset_s(effectivetime);
create index idx_active on prev_simplerefset_s(active);
create index idx_moduleid on prev_simplerefset_s(moduleid);
create index idx_refsetid on prev_simplerefset_s(refsetid);
create index idx_referencedcomponentid on prev_simplerefset_s(referencedcomponentid);



create table prev_stated_relationship_d(
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

create unique index idx_id on prev_stated_relationship_d(id);
create index idx_effectivetime on prev_stated_relationship_d(effectivetime);
create index idx_active on prev_stated_relationship_d(active);
create index idx_moduleid on prev_stated_relationship_d(moduleid);
create index idx_sourceid on prev_stated_relationship_d(sourceid);
create index idx_destinationid on prev_stated_relationship_d(destinationid);
create index idx_relationshipgroup on prev_stated_relationship_d(relationshipgroup);
create index idx_typeid on prev_stated_relationship_d(typeid);
create index idx_characteristictypeid on prev_stated_relationship_d(characteristictypeid);
create index idx_modifierid on prev_stated_relationship_d(modifierid);



create table prev_stated_relationship_f(
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

create index idx_id on prev_stated_relationship_f(id);
create index idx_effectivetime on prev_stated_relationship_f(effectivetime);
create index idx_active on prev_stated_relationship_f(active);
create index idx_moduleid on prev_stated_relationship_f(moduleid);
create index idx_sourceid on prev_stated_relationship_f(sourceid);
create index idx_destinationid on prev_stated_relationship_f(destinationid);
create index idx_relationshipgroup on prev_stated_relationship_f(relationshipgroup);
create index idx_typeid on prev_stated_relationship_f(typeid);
create index idx_characteristictypeid on prev_stated_relationship_f(characteristictypeid);
create index idx_modifierid on prev_stated_relationship_f(modifierid);




create table prev_stated_relationship_s(
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

create unique index idx_id on prev_stated_relationship_s(id);
create index idx_effectivetime on prev_stated_relationship_s(effectivetime);
create index idx_active on prev_stated_relationship_s(active);
create index idx_moduleid on prev_stated_relationship_s(moduleid);
create index idx_sourceid on prev_stated_relationship_s(sourceid);
create index idx_destinationid on prev_stated_relationship_s(destinationid);
create index idx_relationshipgroup on prev_stated_relationship_s(relationshipgroup);
create index idx_typeid on prev_stated_relationship_s(typeid);
create index idx_characteristictypeid on prev_stated_relationship_s(characteristictypeid);
create index idx_modifierid on prev_stated_relationship_s(modifierid);

    

create table prev_textdefinition_d(
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

create unique index idx_id on prev_textdefinition_d(id);
create index idx_effectivetime on prev_textdefinition_d(effectivetime);
create index idx_active on prev_textdefinition_d(active);
create index idx_moduleid on prev_textdefinition_d(moduleid);
create index idx_conceptid on prev_textdefinition_d(conceptid);
create index idx_languagecode on prev_textdefinition_d(languagecode);
create index idx_typeid on prev_textdefinition_d(typeid);
create index idx_term on prev_textdefinition_d(term);
create index idx_casesignificanceid on prev_textdefinition_d(casesignificanceid);


    
create table prev_textdefinition_f(
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

create index idx_id on prev_textdefinition_f(id);
create index idx_effectivetime on prev_textdefinition_f(effectivetime);
create index idx_active on prev_textdefinition_f(active);
create index idx_moduleid on prev_textdefinition_f(moduleid);
create index idx_conceptid on prev_textdefinition_f(conceptid);
create index idx_languagecode on prev_textdefinition_f(languagecode);
create index idx_typeid on prev_textdefinition_f(typeid);
create index idx_term on prev_textdefinition_f(term);
create index idx_casesignificanceid on prev_textdefinition_f(casesignificanceid);

        
create table prev_textdefinition_s(
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

create unique index idx_id on prev_textdefinition_s(id);
create index idx_effectivetime on prev_textdefinition_s(effectivetime);
create index idx_active on prev_textdefinition_s(active);
create index idx_moduleid on prev_textdefinition_s(moduleid);
create index idx_conceptid on prev_textdefinition_s(conceptid);
create index idx_languagecode on prev_textdefinition_s(languagecode);
create index idx_typeid on prev_textdefinition_s(typeid);
create index idx_term on prev_textdefinition_s(term);
create index idx_casesignificanceid on prev_textdefinition_s(casesignificanceid);    
    