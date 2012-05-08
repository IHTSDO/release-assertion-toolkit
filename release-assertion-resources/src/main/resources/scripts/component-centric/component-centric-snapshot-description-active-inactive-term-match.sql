
/******************************************************************************** 
	component-centric-snapshot-description-active-inactive-term-match

	Assertion:
	No active term associated with active concept matches that of an inactive 
	description.

	Note: 	many violations of this assertion were created in prior SNOMED CT 
			releases. Consequently this implementation focuses on highlighting 
			new violations created in the currently prospective release.

	no term of an active description of a concept of which a description was edited 
	matches that of an inactive description of any active concept in snomed	

********************************************************************************/
/* 	a list of inactive descriptions of active concepts in snomed */	
	drop temporary table if exists tmp_inactive_desc;
	create temporary table if not exists tmp_inactive_desc as
	select distinct a.*
	from curr_description_s a
		join curr_concept_s b
			on a.conceptid = b.id
	where a.active = 0
	and b.active = 1;
	commit;
	
/* 	violators are active terms that match inactive terms */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: term=',a.id, ':active term matches inactive term of active concept.') 	
	from curr_description_d a
		join tmp_inactive_desc b
			on a.term = b.term
			and a.active = 1;
	commit;

	drop temporary table if exists tmp_inactive_desc;

