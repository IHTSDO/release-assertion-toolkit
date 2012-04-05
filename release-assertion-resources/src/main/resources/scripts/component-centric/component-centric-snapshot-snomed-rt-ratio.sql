
/******************************************************************************** 
	file-centric-snapshot-snomed-rt-ratio

	Assertion:
	There is one SNOMED RT simple map refset members per concept.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept does not have a single associated SNOMED RT refset member.') 
	
		from curr_simplemaprefset_s a
		inner join curr_concept_s b
		on a.referencedcomponentid = b.id
		where a.refsetid = '900000000000498005'
		and b.active = '1'
		group by a.referencedComponentId, a.maptarget
		having count(*) != 1
		