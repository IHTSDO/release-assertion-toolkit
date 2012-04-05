
/******************************************************************************** 
	file-centric-snapshot-inferred-relationship-immutable

	Assertion:
	There is a 1:1 relationship between the id and the immutable values in Inferred Relationship snapshot

********************************************************************************/
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ':There is a 1-to-1 relationship between the id and the immutable values in Inferred Relationship snapshot.') 	
	from curr_relationship_s a 
	where a.active = '1'
	group by a.id , a.sourceid , a.typeid , a.destinationid
	having count(a.id) > 1;