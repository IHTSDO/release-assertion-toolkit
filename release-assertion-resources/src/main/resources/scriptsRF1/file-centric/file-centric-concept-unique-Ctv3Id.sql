
/******************************************************************************** 
	file-centric-concept-unique-Ctv3Id

	Assertion:
	The Concept file has unique Ctv3Id identifiers.

********************************************************************************/
	
/* 	view of concept made by finding duplicate ctv3id identifiers */
	create or replace view v_curr_snapshot as
	select a.ctv3id
	from curr_concept a
	group by a.ctv3id
	having  count(a.ctv3id) > 1;
	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: ctv3id=',a.ctv3id, ':Non unique ctv3id in current release file.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;
