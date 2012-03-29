
/******************************************************************************** 
	file-centric-snapshot-attribute-value-valid-referencedcomponentid

	Assertion:
	Referencedcomponentid refers to valid concepts in the ATTRIBUTEVALUE snapshot file.

********************************************************************************/
	
/* 	view of current snapshot made by finding invalid referencedcomponentid */
	create or replace view v_curr_snapshot as
	select a.referencedcomponentid
	from curr_attributevaluerefset_s a
	left join curr_concept_s b
	on a.referencedcomponentid = b.id
	where b.id is null;
	
	
	delete from v_curr_snapshot 
	where referencedcomponentid in(
		select id
		from curr_description_s);
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.referencedcomponentid, ':Invalid Referencedcomponentid in ATTRIBUTEVALUE REFSET snapshot.') 	
	from v_curr_snapshot a;
	
	
	drop view v_curr_snapshot;
