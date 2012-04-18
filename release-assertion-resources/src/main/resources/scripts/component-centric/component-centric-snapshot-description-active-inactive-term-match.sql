
/******************************************************************************** 
	component-centric-snapshot-description-active-inactive-term-match

	Assertion:
	No active term associated with active concept matches that of an inactive Description.

********************************************************************************/
	
/* 	view of current snapshot made by finding active terms associated with active concepts */
	create or replace view v_curr_snapshot_1 as
	select term  
	from curr_description_s a , curr_concept_s b
	where a.active = 1
	and a.conceptid = b.id
	and b.active = 1;
	
/* 	view of current snapshot made by matching active terms with inactive terms for active concepts */	
	create or replace view v_curr_snapshot_2 as
	select * from v_curr_snapshot_1
	where term in (
	select distinct(term)  
	from curr_description_s a , curr_concept_s b
	where a.active = 0
	and a.conceptid = b.id
	and b.active = 1); 

/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: term=',a.term, ':Active Term matches with Inactive Term.') 	
	from v_curr_snapshot_2 a ;


	drop view v_curr_snapshot_1;
	drop view v_curr_snapshot_2;

	