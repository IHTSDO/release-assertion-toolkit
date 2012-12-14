
/******************************************************************************** 
	file-centric-concept-valid-IsPrimitive

	Assertion:	
	There is no null IsPrimitive and valid value should be {0,1}.

**************************
******************************************************/
	
/* 	view made by null isprimitive */
	create or replace view v_curr_concept1 as
	select a.conceptid 
	from curr_concept a
	where a.ISPRIMITIVE IS NULL;
	
	
	create or replace view v_curr_concept2 as
	select a.conceptid 
	from curr_concept a
	where a.ISPRIMITIVE not in (0,1);	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is no null IsPrimitive.') 	
	from v_curr_concept1 a;


	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: conceptid=',a.conceptid, ':There is invalid value for IsPrimitive.') 	
	from v_curr_concept2 a;


	drop view v_curr_concept1;
	drop view v_curr_concept2;
	
	