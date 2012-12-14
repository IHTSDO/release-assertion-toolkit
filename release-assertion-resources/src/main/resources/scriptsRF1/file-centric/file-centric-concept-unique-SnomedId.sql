
/******************************************************************************** 
	file-centric-concept-unique-SnomedId

	Assertion:
	The Concept file has unique SnomedId identifiers.

********************************************************************************/
	
/* 	view of current snapshot made by finding duplicate identifiers */
	create or replace view v_curr_snapshot as
	select a.SnomedId
	from curr_concept a
	group by a.SnomedId
	having  count(a.SnomedId) > 1;
	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: SnomedId=',a.SnomedId, ':Non unique SnomedId in current release file.')
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
