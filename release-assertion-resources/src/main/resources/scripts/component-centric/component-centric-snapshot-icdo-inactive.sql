
/******************************************************************************** 
	component-centric-snapshot-icdo-inactive

	Assertion:
	Inactive ICD-O simple map refset refers to inactive components.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Member: id=',a.id, ': Active ICD-O refset member refers to an inactive concept.') 
	
		from curr_simplemaprefset_s a
		inner join curr_concept_s b
		on a.referencedcomponentid = b.id
		where a.refsetid = '446608001'
		and a.active = '0'
		and b.active = '1'
