
/******************************************************************************** 
	component-centric-snapshot-snomed-rt-unique

	Assertion:
	SNOMED RT identifier simple map refset members are unique.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Member: id=',a.id, ': SNOMED RT member is not unique.') 
	
	from curr_simplemaprefset_s a
	inner join curr_concept_s b on a.referencedcomponentid = b.id
	where a.refsetid = '900000000000498005'
	group by a.referencedcomponentid, a.maptarget
	having count(*) > 1 