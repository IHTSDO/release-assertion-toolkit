
/******************************************************************************** 
	release-type-snapshot-validation-concept

	Assertion:
	The current Concept snapshot file is the same as that in the previous 
	release, except for the components that changed in the current release.

********************************************************************************/
	
/* 	view of current delta made by eliminating prior snapshot from current 
	snapshot */
	create or replace view v_curr_delta as
	select a.*
	from curr_concept_s a
	left outer join prev_concept_s b
		on a.id = b.id 
		and a.definitionstatusid = b.definitionstatusid
	where b.id is null
	or b.definitionstatusid is null;
	
/* 	the view of the delta should be the same as the current delta file */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Component in current release view, but not in prior release file.') 	
	from v_curr_delta a
	left outer join curr_concept_d b
		on a.id = b.id
		and a.definitionstatusid = b.definitionstatusid
	where b.id is null
	or b.definitionstatusid is null;

	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Component in prior release file but not in current release view.')
	from curr_concept_d a
	left outer join v_curr_delta b
		on a.id = b.id
		and a.definitionstatusid = b.definitionstatusid
	where b.id is null
	or b.definitionstatusid is null;

	drop view v_curr_delta;
