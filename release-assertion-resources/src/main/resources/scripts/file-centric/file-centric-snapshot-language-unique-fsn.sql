
/******************************************************************************** 
	file-centric-snapshot-language-unique-fsn

	Assertion:
	Every concept's FSN exists exactly once in each language refset.

********************************************************************************/
	
	
	/* Concept has FSN that is defined 2+ times for a given refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept has FSN that is defined more than one time within a given refset.') 
	from curr_description_s a 
	inner join curr_langrefset_s b on a.id = b.referencedcomponentid 
	inner join curr_concept_s c on a.conceptid = c.id
	where c.active = '1'
	and b.active = '1'
	and a.active = '1'
	and a.typeid = '900000000000003001'
	GROUP BY b.referencedcomponentid, b.refsetid
	having count(b.referencedcomponentid) > 1;
	
	
	
	/* Concept does not have an FSN defined */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Concept does not have an FSN defined.') 
	from curr_concept_s a 
	inner join curr_description_s b on b.conceptid = a.id
	where b.typeid = '900000000000003001'
	having count(b.id) = 0;
		
	
	
	/* Concept does not have an FSN in any refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept does not have an FSN in any refset.') 
	from curr_description_s a 
	left join curr_langrefset_s b on a.id = b.referencedcomponentid 
	inner join curr_concept_s c on a.conceptid = c.id
	where b.id is null
	and a.active = '1'	
	and c.active = '1'
	and a.typeid = '900000000000003001';
		
	
	
	/* Concept does not have an FSN in each possible refset */
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept does not have an FSN in each possible refset.') 
	from curr_langrefset_s a
	inner join curr_description_s b on b.id = a.referencedcomponentid 
	inner join curr_concept_s c on b.conceptid = c.id
	where a.active = '1'
	and b.active = '1'
	and c.active = '1'
	and b.typeid = '900000000000003001'
	GROUP BY a.referencedcomponentid
	having count(distinct(a.refsetid)) < (select count(distinct(refsetid)) from curr_langrefset_s);
	
