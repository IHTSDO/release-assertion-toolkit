
/******************************************************************************** 
	component-centric-snapshot-description-fsn-uppercase

	Assertion:
	The first letter of the active FSN associated with active concept should be capitalized.

********************************************************************************/
	
/* 	view of current snapshot made by finding FSN's with leading and training spaces */
	create or replace view v_curr_snapshot as
	select SUBSTRING(a.term , 1, 1) as originalcase ,  UCASE(SUBSTRING(a.term , 1, 1)) as uppercase , a.id 
	from curr_description_s  a , curr_concept_s  b
	where a.typeid = '900000000000003001'
	and a.active = 1
	and b.active = 1
	and a.conceptid = b.id; 

	/*
	select *
	from v_curr_snapshot a
	where BINARY originalcase != uppercase;
	*/
	
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: id=',a.id, ':First letter of the active FSN of active concept not capitalized.') 	
	from v_curr_snapshot a
	where BINARY originalcase != uppercase;


	drop view v_curr_snapshot;
	