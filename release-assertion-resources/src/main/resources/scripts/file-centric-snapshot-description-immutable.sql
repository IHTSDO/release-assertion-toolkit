
/******************************************************************************** 
	file-centric-snapshot-description-immutable

	Assertion:
	There is a 1:1 relationship between the ‘id’ and the immutable values in DESCRIPTION snapshot.

********************************************************************************/
	
/* 	view of current snapshot made by finding duplicate FSN */
	create or replace view v_curr_snapshot as
	select  a.term ,a.typeid 
	from curr_description_s a , curr_concept_s b	
	where a.conceptid = b.id
	and b.active = 1
	and a.active = 1
	group by BINARY  a.term
	having count(a.term) > 1 and a.typeid = '900000000000003001' ;

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.term, ':There is a 1:1 relationship between the ‘id’ and the immutable values in DESCRIPTION snapshot.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;

	