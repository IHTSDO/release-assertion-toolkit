
/******************************************************************************** 
	file-centric-snapshot-language-stated-relationshipunique-id

	Assertion:
	The current Stated Relationship snapshot file does not contain duplicate 
	Relationship Ids

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('RELATIONSHIP: id=',id, ': Relationship Id is repeated in the language refset snapshot file.') 
	
	from curr_stated_relationship_s
	group by id
	having count(id) > 1