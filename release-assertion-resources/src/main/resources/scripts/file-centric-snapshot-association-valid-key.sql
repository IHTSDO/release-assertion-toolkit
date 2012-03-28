
/******************************************************************************** 
	file-centric-snapshot-association-valid-key

	Assertion:
	There is a 1:1 relationship between the id and the key values in the ASSOCIATION REFSET snapshot file.

********************************************************************************/
	
/* 	view of current snapshot made by key values in ASSOCIATION REFSET */
	create or replace view v_curr_snapshot as
	select a.id , a.refsetid , a.referencedcomponentid , a.targetcomponentid
	from curr_associationrefset_s a 
	group by a.id , a.refsetid , a.referencedcomponentid , a.targetcomponentid
	having count(id) > 1 and count(a.refsetid) > 1 and count(a.referencedcomponentid ) > 1 and count(targetcomponentid) > 1;
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':Invalid keys in ASSOCIATION REFSET snapshot file.') 	
	from v_curr_snapshot a;

	drop view v_curr_snapshot;
	