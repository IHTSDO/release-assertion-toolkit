
/******************************************************************************** 
	component-centric-snapshot-concept-inactive-definitionstatusid

	Assertion:
	All inactive concepts have definition status of PRIMITIVE.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':There is an inactive concept whose definition status is not Primitive.') 	
	from curr_concept_s a 
	where active = '0'
	and definitionstatusid != '900000000000074008' -- PRIMITIVE
	