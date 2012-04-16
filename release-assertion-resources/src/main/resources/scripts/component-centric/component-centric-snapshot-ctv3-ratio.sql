
/******************************************************************************** 
	component-centric-snapshot-ctv3-ratio

	Assertion:
	There is one CTV3 simple map refset members per concept.

********************************************************************************/
	
	
	/* Concept maps to multiple CTV3 Refset Members */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept has more than one associated CTV3 refset member.') 
	
	from curr_simplemaprefset_s a
	inner join curr_concept_s b on a.referencedcomponentid = b.id
	where a.refsetid = '900000000000497000'
	and b.active = '1'
	and a.active = '1'
	group by a.id
	having count(a.id) > 1;




	/* Concept is without a CTV3 Refset Member mapping */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',id, ': Concept does not have an associated CTV3 refset member.') 
	from curr_concept_s
	where active = '1'
	and id not in 
		(		
			select referencedComponentid 
			from curr_simplemaprefset_s 
			where refsetid = '900000000000497000'
			and active = '1'
		);
		