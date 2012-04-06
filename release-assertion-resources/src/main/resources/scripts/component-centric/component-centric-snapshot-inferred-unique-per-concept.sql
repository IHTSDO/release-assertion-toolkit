
/******************************************************************************** 
	component-centric-snapshot-inferred-unique-per-concept
	
	Assertion:
	No Concept has 2 inferred relationships with the same type, destination and group.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.sourceid, ': Concept have two inferred relationships with same typeid and same destinationid within a single relationship-group.') 	
	from curr_relationship_s a
	inner join curr_concept_s b on a.sourceid = b.id
	where a.active = '1'
	and b.active = '1'
	group by a.sourceid, a.typeid, a.destinationid, a.relationshipgroup
	having count(*) > 1 
