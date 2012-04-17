
/******************************************************************************** 
	component-centric-snapshot-stated-inactive-modifier
	
	Assertion:
	Stated relationship modifier is always Some.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('RELATIONSHIP: id=',a.id, ': Stated relationship has a non -SOME- modifier.') 	
	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on a.sourceid = b.id
	where a.active = '1'
	and b.active = '1'
	and a.modifierid != '900000000000451002'
	