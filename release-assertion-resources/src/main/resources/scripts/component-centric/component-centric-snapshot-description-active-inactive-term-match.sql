
/******************************************************************************** 
	component-centric-snapshot-description-active-inactive-term-match

	Assertion:
	No active term matches that of an inactive Description.

********************************************************************************/
	
/* 	view of current snapshot made by finding FSN's not ending with closing parantheses */
	
	create or replace view v_curr_snapshot_1 as
	select distinct(term)  
	from curr_description_d a
	where a.active = 1;
	
	create or replace view v_curr_snapshot_2 as
	select distinct(term) 
	from curr_description_d a
	where a.active = 0;


/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: term=',a.term, ':Active Term matches with Inactive Term.') 	
	from v_curr_snapshot_1 a , v_curr_snapshot_2 b
	where a.term = b.term;


	drop view v_curr_snapshot_1;
	drop view v_curr_snapshot_2;

	