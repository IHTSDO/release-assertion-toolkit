
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
			concat('CONCEPT: id=',a.id, ': Concept does not have an Preferred Term in any refset.') 
	from curr_concept_s a
	left join active_synonym_view b on b.conceptid = a.id
	where a.active = '1'
	and  b.conceptid is null;


	drop view active_synonym_view;

	
	
	
	
	
	
	/* TEST: All active Synonyms for active Concepts does not exist in the any language refset */

	/* Create view of all active Synonyms for all active concepts */
	create or replace view active_con_syn_view as
		select a.id from curr_description_s a
		inner join curr_concept_s b on b.id = a.conceptid
		where a.typeid = '900000000000013009'
		and a.active = '1'
		and b.active = '1';


	/* Create view of all active Preferred Terms in the language refset table */
	create or replace view active_preferred_view as
		select a.referencedcomponentid from curr_langrefset_s a
		where a.acceptabilityid = '900000000000548007'
		and a.active = '1';

		
		
	/* Synonym does not contain a an active Preferred Term in the language refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
			concat('CONCEPT: id=',a.id, ': Concept does not have an Preferred Term in any refset.') 
	from active_con_syn_view a
	left join active_preferred_view b on b.referencedcomponentid = a.id
	where  b.referencedcomponentid is null;


	drop view active_preferred_view;
	drop view active_con_syn_view;
		
		
		
		
		
		
	
	
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
	GROUP BY a.referencedcomponentid
	having count(distinct(a.refsetid)) < (select count(distinct(refsetid)) from curr_langrefset_s);
	
        