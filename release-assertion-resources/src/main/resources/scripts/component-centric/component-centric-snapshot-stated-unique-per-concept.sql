
/******************************************************************************** 
	component-centric-snapshot-stated-unique-per-concept
	
	Assertion:
	No Concept has 2 stated relationships with the same type, destination and group.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',b.id, ': Concept have two stated relationships with same typeid and same destinationid within a single relationship-group.') 	
	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on a.sourceid = b.id
	where a.active = '1'
	and b.active = '1'
	group by a.sourceid, a.typeid, a.destinationid, a.relationshipgroup
	having count(*) > 1 
