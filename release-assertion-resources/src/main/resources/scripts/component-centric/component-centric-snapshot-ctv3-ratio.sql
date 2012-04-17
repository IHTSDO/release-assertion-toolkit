
/******************************************************************************** 
	component-centric-snapshot-ctv3-ratio

	Assertion:
	There is one CTV3 simple map refset members per concept.

********************************************************************************/
	
	
	/* TEST: Concept maps to multiple CTV3 Refset Members */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept has more than one associated CTV3 refset member.') 
	
	from curr_simplemaprefset_s a
	inner join curr_concept_s b on a.referencedcomponentid = b.id
	where a.refsetid = '900000000000497000'
	group by a.id
	having count(a.id) > 1;






	/* TEST: Concept is without a CTV3 Refset Member mapping */

	/* Create view of all active CTV3 refset members */
	create or replace view v_act_ctv3 as
		select referencedComponentid 
		from curr_simplemaprefset_s 
		where refsetid = '900000000000497000';
			

	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',id, ': Concept does not have an associated CTV3 refset member.') 
	from curr_concept_s a
	left join v_act_ctv3 b on a.id = b.referencedComponentId 
	where b.referencedComponentId is null;

	drop view v_act_ctv3;