
/******************************************************************************** 
	file-centric-concept-valid-SnomedId

	Assertion:	
	There is a valid SnomedID.

********************************************************************************/
	
/* 	view made by null SnomedID */
	create or replace view v_curr_concept1 as
	select a.conceptid 
	from curr_concept a
	where a.snomedid IS NULL;
	
/* 	view made by invalid character in SnomedID */	
	create or replace view v_curr_concept2 as
	select a.conceptid 
	from curr_concept a		
	WHERE a.snomedid not REGEXP '[A-Z0-9-]';
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is null SnomedID.') 	
	from v_curr_concept1 a;


	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is invalid character in SnomedID.') 	
	from v_curr_concept2 a;

	drop view v_curr_concept1;
	drop view v_curr_concept2;
	