
/******************************************************************************** 
	component-centric-snapshot-report.sql

	Assertion:
	Report Creation...

********************************************************************************/
	
		insert into qa_report (runid, assertionuuid, assertiontext, result ,count)
		select b.runid , b.assertionuuid , b.assertiontext , 'F' , count(b.assertionuuid)  
		from qa_result b 
		where b.runid = <RUNID>;
