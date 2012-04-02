
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
		concat('Relationship: id=',a.id, ': Stated relationship has a non -SOME- modifier.') 	
	from curr_stated_relationship_s a
	where a.modifierid != '900000000000451002'
	