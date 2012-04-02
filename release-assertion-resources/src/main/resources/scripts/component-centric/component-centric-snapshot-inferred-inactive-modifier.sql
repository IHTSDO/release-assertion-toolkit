
/******************************************************************************** 
	component-centric-snapshot-inferred-inactive-modifier
	
	Assertion:
	Inferred relationship modifier is always SOME.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Relationship: id=',a.id, ': Inferred relationship has a non -SOME- modifier.') 	
	from curr_relationship_s a
	where a.modifierid != '900000000000451002'
	