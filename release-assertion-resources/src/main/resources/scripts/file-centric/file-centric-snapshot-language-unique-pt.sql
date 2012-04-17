
/******************************************************************************** 
	file-centric-snapshot-language-unique-pt

	Assertion:
	Every concept's Preferred Term exists exactly once in each language refset.
	
	Four Tests:
	1) Concept has Preferred Term that is defined 2+ times for a given refset
	2) Active Concept doesn't contain an active Synonym
	3) All active Synonyms for active Concepts does not exist in the any language refset 
	4) Concept does not have an active Preferred Term in each possible refset

********************************************************************************/
	
        
	/* TEST: Concept has Preferred Term that is defined 2+ times for a given refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept has Preferred Term that is defined more than one time within a given refset.') 
	from curr_description_s a 
	INNER JOIN curr_langrefset_s b on a.id = b.referencedComponentid
	inner join curr_concept_s c on a.conceptid = c.id
        where b.acceptabilityid = '900000000000548007'	
	and c.active = '1'
	and b.active = '1'
	and a.active = '1'
	GROUP BY b.referencedcomponentid, b.refsetid
	having count(b.referencedcomponentid) > 1;
	
	
	
	
	
	
	
	/* TEST: Active Concept doesn't contain an active Synonym */
	
	/* View of all active synonyms */
	create or replace view active_synonym_view as	
		select a.conceptid from curr_description_s a
		where a.typeid = '900000000000013009'
		and a.active = '1';
	
	
	/* Active Concepts without active synonyms */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
			concat('CONCEPT: id=',a.id, ': Concept does not have a Synonym.') 
	from curr_concept_s a
	left join active_synonym_view b on b.conceptid = a.id
	where a.active = '1'
	and  b.conceptid is null;


	drop view active_synonym_view;

	
	
	
	
	
	
	
	
	/* TEST: Concept does not contain an active Preferred Term in any language refset */

	/* Create view of all active Pref Terms */
	
	create or replace view active_act_pref_con_view as
		select distinct(a.conceptid) from curr_description_s a
			inner join curr_langrefset_s b on a.id = b.referencedcomponentid
			where a.typeid = '900000000000013009'
			and a.active = '1'
			and b.acceptabilityid = '900000000000548007'
			and b.active = '1';
		
		
		
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
			concat('CONCEPT: id=',a.id, ': Concept does not have a Preferred Term in any refset.') 
	from curr_concept_s a
	left join active_act_pref_con_view b on a.id = b.conceptid	
	where a.active = '1'
	and b.conceptid is null;


	drop view active_act_pref_con_view;
		
		
		
		
		
		
		
		
	
	
	/* TEST: Concept does not have an active Preferred Term in each possible refset */
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept does not have an Preferred Term in each possible refset.') 
	from curr_langrefset_s a
	inner join curr_description_s b on b.id = a.referencedcomponentid 
	inner join curr_concept_s c on b.conceptid = c.id
	where a.active = '1'
	and b.active = '1'
	and c.active = '1'
	and a.acceptabilityid = '900000000000548007'
	and b.typeid = '900000000000013009'
	group by b.id
	having count(a.refsetid) < (select count(distinct(refsetid)) from curr_langrefset_s);
        