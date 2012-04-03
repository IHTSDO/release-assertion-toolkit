
/******************************************************************************** 
	component-centric-snapshot-description-active-inactive-term-match

	Assertion:
	No active term matches that of an inactive Description.

********************************************************************************/
	
/* 	view of current snapshot made by finding FSN's not ending with closing parantheses */
	create or replace view v_curr_snapshot as
	select a.* 
	from curr_description_s a , curr_concept_s b
	where a.typeid in ('900000000000003001')	
	and a.conceptid = b.id
	and b.active =1 
	and a.active = 1
	and a.term not like '%)';


	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':Active Term matches with Inactive Term.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;

	