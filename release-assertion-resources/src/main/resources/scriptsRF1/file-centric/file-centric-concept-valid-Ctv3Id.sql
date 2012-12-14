
/******************************************************************************** 
	file-centric-concept-valid-Ctv3Id

	Assertion:	
	There is a valid Ctv3ID.

********************************************************************************/
	
/* 	view made by null Ctv3ID */
	create or replace view v_curr_concept1 as
	select a.conceptid 
	from curr_concept a
	where a.CTV3ID IS NULL;
	
/* 	view made by invalid character in Ctv3ID */	
	create or replace view v_curr_concept2 as
	select a.conceptid 
	from curr_concept a
	WHERE a.Ctv3Id not REGEXP '[A-Za-z0-9.]';  
 
 	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is null Ctv3ID.') 	
	from v_curr_concept1 a;


	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is invalid character in Ctv3ID.') 	
	from v_curr_concept2 a;

	drop view v_curr_concept1;
	drop view v_curr_concept2;
	