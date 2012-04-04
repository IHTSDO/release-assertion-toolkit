
/******************************************************************************** 
	component-centric-snapshot-language-active-members

	Assertion:
	Members are active for active descriptions in the snapshot file.

********************************************************************************/
	
/* 	view of current snapshot of language refset referencedcomponentids of active members */

	create or replace view v_curr_snapshot_active as
	select referencedcomponentid
	from curr_langrefset_s 
	where active = '1';




/* 	view of current snapshot of language refset referencedcomponentids of inactive only members */

	create or replace view v_curr_snapshot_inactive_only as
	select referencedcomponentid
	from curr_langrefset_s a
	where a.referencedComponentId not in (
		select referencedComponentId 
		from v_curr_snapshot_active 
	);	

	
	
	
/* 	inserting exceptions in the result table */

	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Description: id=',a.id, ': Description is active but has no active members in language refset.') 
	from curr_description_s a
	inner join curr_concept_s c
	on a.conceptid = c.id
	inner join v_curr_snapshot_inactive_only b
	on a.id = b.referencedComponentId
	where a.active = '1' 
	and c.active = '1';



	drop view v_curr_snapshot_active;
	drop view v_curr_snapshot_inactive_only;

	
