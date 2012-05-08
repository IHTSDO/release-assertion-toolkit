
/******************************************************************************** 
	component-centric-snapshot-description-active-inactive-term-match

	Assertion:
	No active term associated with active concept matches that of an inactive 
	description.

	Note: 	many violations of this assertion were created in prior SNOMED CT 
			releases. Consequently this implementation focuses on highlighting 
			new violations created in the currently prospective release.	

********************************************************************************/
/* 	a list of active concepts that have been edited for the current prospective 
	release
*/
	drop temporary table if exists tmp_active_con;
	create temporary table if not exists tmp_active_con as	
	select b.* 
	from res_concepts_edited a
		join curr_description_d b
			on a.conceptid = b.id
			and b.active = 1;
	commit;
	
/* 	a list of active descriptions belonging to concepts of which descriptions have 
	been edited for the current prospective release (i.e. all the descrniptions
	of these concepts - not only the edited descriptions).
*/	
	drop temporary table if exists tmp_active_desc;
	create temporary table if not exists tmp_active_desc as
	select distinct a.*
	from curr_description_s a
		join curr_description_d b
			on a.conceptid = b.conceptid
		join tmp_active_con c
			on a.conceptid = c.conceptid
	and a.active = 1;
	commit;

/* 	a list of inactive descriptions belonging to concepts of which descriptions have 
	been edited for the current prospective release
*/	
	drop temporary table if exists tmp_inactive_desc;
	create temporary table if not exists tmp_inactive_desc as
	select distinct a.*
	from curr_description_s a
		join curr_description_d b
			on a.conceptid = b.conceptid
		join tmp_active_con c
			on a.conceptid = c.conceptid	
	and a.active = 0;
	commit;
	
/* 	violators are active terms that match inactive terms */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: term=',a.term, ':Active Term matches with Inactive Term.') 	
	from tmp_active_desc a
		join tmp_inactive_desc b
			on a.term = b.term;
	
	commit;

	drop temporary table if exists tmp_active_con;
	drop temporary table if exists tmp_active_desc;
	drop temporary table if exists tmp_inactive_desc;

