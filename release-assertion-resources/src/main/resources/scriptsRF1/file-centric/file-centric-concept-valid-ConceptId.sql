
/******************************************************************************** 
	file-centric-concept-valid-ConceptId

	Assertion:	
	Concept file has valid conceptId.

********************************************************************************/
	
/* 	view made by null Conceptid */
	create or replace view v_curr_concept1 as
	select a.conceptid 
	from curr_concept a
	where a.conceptid IS NULL;
	
	/* 	view made by invalid character in Conceptid */		
	create or replace view v_curr_concept2 as
	select a.* 
	from curr_concept a
	WHERE conceptid REGEXP '^[[:alpha:]]+$';
		 
	
	
	/* 	view made by invalid partitionid in Conceptid */		
	create or replace view v_curr_concept3 as
	select a.conceptid 
	from curr_concept a
	WHERE Substr(ConceptId,-3,2) != '00';  
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.ConceptId, ':Null CONCEPTId.') 	
	from v_curr_concept1 a;
	
	/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.ConceptId, ':invalid character in Conceptid.') 	
	from v_curr_concept2 a;
	
		/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.ConceptId, ':invalid partitionid in Conceptid.') 	
	from v_curr_concept3 a;

	drop view v_curr_concept1;
	drop view v_curr_concept2;
	drop view v_curr_concept3;