
/******************************************************************************** 
	file-centric-snapshot-association-valid-referencedcomponentid

	Assertion:
	Referencedcomponentid refers to valid concepts in the ASSOCIATION REFSET snapshot file.

********************************************************************************/
	
/* 	view of current snapshot made by finding invalid referencedcomponentid */
	create or replace view v_curr_snapshot as
	select a.referencedcomponentid
	from curr_associationrefset_s a
	left join curr_description_s b
	on (a.referencedcomponentid = b.id
	or a.referencedcomponentid = b.conceptid)
	where (b.id is null or b.conceptid is null);
	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.referencedcomponentid, ':Invalid Referencedcomponentid in ASSOCIATION REFSET snapshot.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
