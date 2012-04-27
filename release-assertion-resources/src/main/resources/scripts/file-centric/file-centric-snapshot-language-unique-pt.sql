
/******************************************************************************** 
	file-centric-snapshot-language-unique-pt

	Assertion:
	Every active concept has one and only one active preferred term.

********************************************************************************/
<<<<<<< .mine
	/* testing for multiple perferred terms */
=======
	

	/* TEST: Active Concept doesn't contain an active Synonym */

	/* View of all active synonyms */
	create or replace view v_active_synonym_view as	
		select a.id, a.conceptid from curr_description_s a
		where a.typeid = '900000000000013009'
		and a.active = '1';

>>>>>>> .r302
	select  	
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
<<<<<<< .mine
		concat('CONCEPT: id=',a.id, ': Concept has multiple active preferred terms.') 
	from curr_concept_s a 
		join curr_description_s b
			on a.id = b.conceptid
			and a.active = b.active
			and b.typeid = '900000000000013009' /* synonym */
		join curr_langrefset_s c
			on b.id = c.referencedcomponentid
			and b.active = c.active
			and c.acceptabilityid = '900000000000548007' /* preferred */
=======
		concat('CONCEPT: id=',a.id, ': Concept does not have a Synonym.') 
	
	from curr_concept_s a
	left join v_active_synonym_view b on b.conceptid = a.id
>>>>>>> .r302
	where a.active = '1'
	group by c.refsetid, c.referencedcomponentid
	having (count(c.refsetid) > 1 and count(c.referencedcomponentid) >1);


	/* testing for the absence of preferred terms */

	/* active syonyms of active concepts */
	create or replace view v_condesc as
	select a.id as conceptid, b.id as descriptionid, a.active 
	from (curr_concept_s a 
		join curr_description_s b
			on a.id = b.conceptid
			and a.active = b.active
			and b.typeid = '900000000000013009') /* synonym */
	where a.active = '1';

	/* view of GB language refset */
	create or replace view v_refset_gb as
	select a.referencedcomponentid as descriptionid, b.conceptid
	from curr_langrefset_s a join curr_description_s b
	on a.referencedcomponentid = b.id
	and a.active = b.active
	and a.acceptabilityid = '900000000000548007'
	and a.refsetid = '900000000000508004'
	and b.typeid = '900000000000013009'
	where b.active = '1';

	/* view of US language refset */
	create or replace view v_refset_us as
	select a.referencedcomponentid as descriptionid, b.conceptid
	from curr_langrefset_s a join curr_description_s b
	on a.referencedcomponentid = b.id
	and a.active = b.active
	and a.acceptabilityid = '900000000000548007'
	and a.refsetid = '900000000000509007'
	and b.typeid = '900000000000013009'
	where b.active = '1';

<<<<<<< .mine
	/* active concepts for which there are no GB refset preferred members */
	select  	
=======
	
	/* TEST: Active Concept does not have an active Preferred Term in any refset */

	/* View of all active preferred terms */
	create or replace view v_active_act_pref_con_view as
		select * from curr_langrefset_s 
		where acceptabilityid = '900000000000548007'
		and active = '1';


	/* View of all active synonyms with active preferred terms */
	create or replace view v_active_pref_syn_match_view as
		select a.conceptid, b.*	from v_active_synonym_view a
		inner join v_active_act_pref_con_view b on b.referencedComponentid = a.id;

	select 
>>>>>>> .r302
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept has no en-GB preferred term.') 
	from v_condesc a left join  v_refset_gb b
	on a.conceptid = b.conceptid
	where b.conceptid is null;

<<<<<<< .mine
	/* active concepts for which there are no US refset preferred members */
	select  	
=======
 	from curr_concept_s a
	left join v_active_pref_syn_match_view b on a.id = b.conceptid
	where a.active = '1'
	and b.conceptid is null;

	
	
	
	
	
	
	
	
	/* TEST: Concept has 2+ Preferred Terms for a given refset */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
>>>>>>> .r302
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept has no en-US preferred term') 
	from v_condesc a left join  v_refset_us b
	on a.conceptid = b.conceptid
	where b.conceptid is null;	

<<<<<<< .mine

	drop view if exists v_condesc;
	drop view if exists v_refset_gb;
	drop view if exists v_refset_us;
=======
	from v_active_pref_syn_match_view a 
	inner join curr_concept_s c on a.conceptid = c.id
	where c.active = '1'
	GROUP BY c.id, a.refsetid
	having count(c.id) > 1;
>>>>>>> .r302
	
	commit;
	
<<<<<<< .mine
	=======
	
	
	
		
	
	
	/* TEST: Concept does not have an active Preferred Term in each possible refset */
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',c.id, ': Concept does not have an Preferred Term in each possible refset.') 
	
	from v_active_pref_syn_match_view a 
	inner join curr_concept_s c on a.conceptid = c.id
	where c.active = '1'
	group by c.id
	having count(a.refsetid) < (select count(distinct(refsetid)) from curr_langrefset_s);


	drop view v_active_synonym_view;
	drop view v_active_act_pref_con_view;
	drop view v_active_pref_syn_match_view;
>>>>>>> .r302
