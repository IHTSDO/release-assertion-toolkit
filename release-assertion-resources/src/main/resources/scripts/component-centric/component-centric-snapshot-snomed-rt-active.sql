
/******************************************************************************** 
	file-centric-snapshot-snomed-rt-active

	Assertion:
	SNOMED RT identifier simple map refset refers to active components.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Member: id=',a.id, ': Active SNOMED RT refset member refers to an inactive concept.') 

	from curr_simplemaprefset_s a
	inner join curr_concept_s b
	on a.referencedcomponentid = b.id
	where a.refsetid = '900000000000498005'
	and a.active = '1'
	and b.active = '0'
