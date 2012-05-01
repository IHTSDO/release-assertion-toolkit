
/******************************************************************************** 
	file-centric-snapshot-language-unique-pt

	Assertion:
	Every active concept has one and only one active preferred term.

********************************************************************************/
	/* testing for multiple perferred terms */
	select  	
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
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

	/* active concepts for which there are no GB refset preferred members */
	select  	
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept has no en-GB preferred term.') 
	from v_condesc a left join  v_refset_gb b
	on a.conceptid = b.conceptid
	where b.conceptid is null;

	/* active concepts for which there are no US refset preferred members */
	select  	
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept has no en-US preferred term') 
	from v_condesc a left join  v_refset_us b
	on a.conceptid = b.conceptid
	where b.conceptid is null;	


	drop view if exists v_condesc;
	drop view if exists v_refset_gb;
	drop view if exists v_refset_us;
	
	commit;
	
	