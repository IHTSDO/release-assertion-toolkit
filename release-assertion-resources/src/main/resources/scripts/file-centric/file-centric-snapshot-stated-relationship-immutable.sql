
/******************************************************************************** 
	file-centric-snapshot-stated-relationship-immutable

	Assertion:
	There is a 1:1 relationship between the id and the immutable values in STATED RELATIONSHIP snapshot

********************************************************************************/
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ':There is a 1-to-1 relationship between the id and the immutable values in Stated Relationship snapshot.') 	
	from curr_stated_relationship_s a 
	group by a.id , a.sourceid , a.typeid , a.destinationid
	having count(a.id) > 1;