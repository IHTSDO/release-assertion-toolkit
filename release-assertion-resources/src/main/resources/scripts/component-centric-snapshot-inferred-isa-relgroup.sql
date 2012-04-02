
/******************************************************************************** 
	component-centric-snapshot-inferred-isa-relgroup
	
	Assertion:
	All inferred is-a relationships have relationship group of 0.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.sourceid, ': Concept has an inferred is-a relationship in a non-zero relationship group.') 	
	from curr_relationship_s a
	where a.active = '1'
	and a.relationshipgroup > 0
	and typeid = '116680003'		
	
	