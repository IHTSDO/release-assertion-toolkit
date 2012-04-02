
/******************************************************************************** 
	component-centric-snapshot-inferred-inactive-concepts
	
	Assertion:
	All inferred relationships in which the source concept is inactive, are inactive relationships.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Relationship: id=',a.id, ': Active inferred relationship is associated with an inactive concept.') 	
	from curr_relationship_s a
	inner join curr_concept_s b on a.sourceid = b.id
	where a.active = '1'
	and b.active = '0'
	
	