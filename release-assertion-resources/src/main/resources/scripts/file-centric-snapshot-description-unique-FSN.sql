
/******************************************************************************** 
	file-centric-snapshot-description-unique-FSN

	Assertion:
	Active Fully Specified Name is unique in DESCRIPTION snapshot.

********************************************************************************/
	
/* 	view of current snapshot made by finding duplicate FSN */
	create or replace view v_curr_snapshot as
	select  a.typeid , a.term
	from curr_description_s a		
	group by a.term
	having count(a.term) > 1 and a.typeid = '900000000000003001';


	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':Active Fully Specified Name is unique in DESCRIPTION snapshot.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
