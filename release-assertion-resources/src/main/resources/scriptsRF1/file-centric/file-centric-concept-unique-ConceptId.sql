
/******************************************************************************** 
	file-centric-concept-unique-ConceptId

	Assertion:
	The Concept file has unique identifiers.

********************************************************************************/
	
/* 	view of concept made by finding duplicate identifiers */
	create or replace view v_curr_snapshot as
	select a.conceptid
	from curr_concept a
	group by a.conceptid
	having  count(a.conceptid) > 1;
	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ':Non unique id in current release file.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
