
/******************************************************************************** 
	component-centric-snapshot-inferred-rel-groups

	Assertion:
	Relationship groups contain at least 2 relationships.
.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Relationship: id=',a.id, ': Relationship is only active inferred relationship in a relationship group.') 	
	from curr_relationship_s a
	where a.relationshipgroup != 0 and
	a.active ='1'
	group by a.sourceid, a.relationshipgroup
	having count(*) = 1
