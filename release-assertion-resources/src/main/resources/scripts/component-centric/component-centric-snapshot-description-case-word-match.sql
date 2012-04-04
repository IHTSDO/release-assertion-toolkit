
/******************************************************************************** 
	component-centric-snapshot-description-case-word-match

	Assertion:
	Case-sensitive terms that share initial words also share caseSignificanceId value.

********************************************************************************/
	
/* 	view of current snapshot made by finding all the casesitive term for active concepts */


	create or replace view v_curr_snapshot_1 as
	select SUBSTRING_INDEX(term, ' ', 1) as firstword , a.conceptid , a.id , a.term , a.casesignificanceid
	from  curr_description_s a , curr_concept_s b
	where a.casesignificanceid = 900000000000017005
	and a.active = 1
	and b.active = 1
	and a.conceptid = b.id;
	
	create or replace view v_curr_snapshot_2 as
	select a.conceptid , a.term ,  a.casesignificanceid 
	from v_curr_snapshot_1 a , curr_description_s b , curr_concept_s c
	where b.casesignificanceid = 900000000000020002
	and b.active = 1
	and c.active = 1
	and b.conceptid = c.id
	and a.conceptid = b.conceptid
	and BINARY SUBSTRING_INDEX(b.term, ' ', 1) = firstword;	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ':Terms not sharing case-sensitivity.') 	
	from v_curr_snapshot_2 a;


	drop view v_curr_snapshot_1;
	drop view v_curr_snapshot_2;

	