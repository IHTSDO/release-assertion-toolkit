
/******************************************************************************** 
	file-centric-snapshot-association-valid-targetcomponentid

	Assertion:
	TargetComponentId refers to valid concepts in the ASSOCIATION REFSET snapshot file.

********************************************************************************/
	
/* 	view of current snapshot made by finding duplicate identifiers */
	create or replace view v_curr_snapshot as
	select a.targetcomponentid
	from curr_associationrefset_s a
	right join curr_concept_s b
	on a.targetcomponentid = b.id
	where b.id is null;
	
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.targetcomponentid, ':'Invalid TargetComponentId in the ASSOCIATION REFSET snapshot file.')
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
