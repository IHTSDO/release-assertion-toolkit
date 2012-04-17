
/******************************************************************************** 
	component-centric-snapshot-snomed-rt-ratio

	Assertion:
	There is one SNOMED RT simple map refset members per concept.

********************************************************************************/
	
	
	/* Concept maps to multiple SNOMED RT Refset Members */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept has more than one associated SNOMED RT refset member.') 
	
	from curr_simplemaprefset_s a
	inner join curr_concept_s b on a.referencedcomponentid = b.id
	where a.refsetid = '900000000000498005'
	and b.active = '1'
	and a.active = '1'
	group by (a.id)
	having count(a.id) > 1;








	/* TEST: Concept is without a SNOMED RT Refset Member mapping */

	/* Create view of all active SNOMED RT refset members */
	create or replace view v_act_srt as
		select referencedComponentid 
		from curr_simplemaprefset_s 
		where refsetid = '900000000000498005'
		and active = '1';
			

	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',id, ': Concept does not have an associated SNOMED RT refset member.') 
	from curr_concept_s a
	left join v_act_srt b on a.id = b.referencedComponentId 
	where a.active = '1'
	and b.referencedComponentId is null;

	drop view v_act_srt;

