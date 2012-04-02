
/******************************************************************************** 
	component-centric-snapshot-stated-one-isa

	Assertion:
	All concepts have at least one stated is-a relationship.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept does not have a stated is-a relationship.') 	
	from curr_concept_s a
	left join curr_stated_relationship_s b on b.sourceid = a.id
	where 	a.active = '1'
	having count(b.typeid = '116680003') = 0 and count(b.active = '1') = 0	
		
