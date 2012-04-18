
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
	

	/* TEST: Active Concept doesn't contain an active Synonym */

	/* View of all active synonyms */
	create or replace view active_synonym_view as	
		select a.id, a.conceptid from curr_description_s a
		where a.typeid = '900000000000013009'
		and a.active = '1';

	select  	
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Concept does not have a Synonym.') 
	
	from curr_concept_s a
	left join active_synonym_view b on b.conceptid = a.id
	where a.active = '1'
	and  b.conceptid is null;






	
	/* TEST: Active Concept does not have an active Preferred Term in any refset */

	/* View of all active preferred terms */
	create or replace view active_act_pref_con_view as
		select * from curr_langrefset_s 
		where acceptabilityid = '900000000000548007'
		and active = '1';


	/* View of all active synonyms with active preferred terms */
	create or replace view active_pref_syn_match_view as
		select a.conceptid, b.*	from active_synonym_view a
		inner join active_act_pref_con_view b on b.referencedComponentid = a.id;

	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Concept does not have a Preferred Term in any refset.') 

 	from curr_concept_s a
	left join active_pref_syn_match_view b on a.id = b.conceptid
	where a.active = '1'
	and b.conceptid is null;

	
	
	
	
	
	
	
	
	/* TEST: Concept has 2+ Preferred Terms for a given refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept has multiple Preferred Terms within a given refset.') 

	from active_pref_syn_match_view a 
	inner join curr_concept_s c on a.conceptid = c.id
	where c.active = '1'
	GROUP BY c.id, a.refsetid
	having count(c.id) > 1;
	
	
	
	
	
		
	
	
	/* TEST: Concept does not have an active Preferred Term in each possible refset */
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept does not have an Preferred Term in each possible refset.') 
	
	from active_pref_syn_match_view a 
	inner join curr_concept_s c on a.conceptid = c.id
	where c.active = '1'
	group by c.id
	having count(a.refsetid) < (select count(distinct(refsetid)) from curr_langrefset_s);


	drop view active_synonym_view;
	drop view active_act_pref_con_view;
	drop view active_pref_syn_match_view;
