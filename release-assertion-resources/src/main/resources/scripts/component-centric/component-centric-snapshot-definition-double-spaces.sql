
/******************************************************************************** 
	file-centric-snapshot-definition-double-spaces

	Assertion:
	No active Terms contain double spaces.

********************************************************************************/
	
/* 	view of current snapshot made by finding terms containing double spaces */
	create or replace view v_curr_snapshot as
	select a.term from curr_textdefinition_s a , curr_concept_s b
	where a.active = 1
	and a.conceptid = b.id
	and b.active = 1
	and a.term like '%  %'; 

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':Active Terms containing double spaces.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;

	