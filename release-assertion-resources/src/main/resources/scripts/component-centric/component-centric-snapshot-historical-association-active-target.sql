
/******************************************************************************** 
	component-centric-snapshot-historical-association-active-target

	Assertion:
	Active historical association refset members have active concepts as targets.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('MEMBER: id=',a.id, ': Active Historical refset member is active, but maps to Target Component that is inactive concept.') 	
	from curr_associationrefset_s a
	inner join curr_concept_s b on a.targetcomponentid = b.id
	where a.active = '1'
	and b.active = '0' 
	and a.effectivetime =  <CURRENT-RELEASE-DATE>
		