
/******************************************************************************** 
	component-centric-snapshot-description-unique-term-in-concept

	Assertion:
	For a given concept, all active description terms are unique.

********************************************************************************/
	
/* 	view of current snapshot made by finding FSN's without semantic tags */
	create or replace view v_curr_snapshot as
	select a.*
	from curr_description_s a 
	where a.active = 1
	group by BINARY a.term , a.conceptid
	having count(a.term) > 1 and count(a.conceptid) > 1
	order by a.term , a.conceptid;
	
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: Term=', a.term, ':Non unique description terms.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;

	