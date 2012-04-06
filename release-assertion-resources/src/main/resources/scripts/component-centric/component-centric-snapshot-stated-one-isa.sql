
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
	where a.active = '1'
	and a.id not in (
	select distinct(sourceid)
		from curr_stated_relationship_s
		where active = '1'
		and typeid = '116680003'	
	)		
