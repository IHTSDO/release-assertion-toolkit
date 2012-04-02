
/******************************************************************************** 
	component-centric-snapshot-historical-association-refers-to-active

	Assertion:
	Active REFERS TO concept association refset members refer to active concepts.
.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('MEMBER: id=',a.id, ': Active Historical REFERS TO concept association refset member concepts are active concepts.') 	
	from curr_associationrefset_s a
	inner join curr_concept_s b on a.referencedcomponentid = b.id
	where a.active = '1'
	and a.refsetid = '900000000000521006'
	and b.active != '1'
		