
/******************************************************************************** 
	component-centric-snapshot-concept-primitive

	Assertion:
	All Concepts having only one defining relationship have 'definitionstatus' 
	PRIMITIVE.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ':Concept has only one defining relationship but is not primitive.') 	
	from curr_concept_s a 
	inner join curr_relationship_s b
	on a.id = b.sourceid
	where b.characteristictypeid = '900000000000011006' -- inferred
	and a.definitionstatusid != '900000000000074008' -- primitive
	having count(b.sourceid) = 1
	