
/******************************************************************************** 
	component-centric-snapshot-stated-rel-groups

	Assertion:
	Relationship groups contain at least 2 stated relationships.
.

********************************************************************************/
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',b.id, ': Concept contains a relationship group with a single active stated member.') 	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on b.id = a.sourceid
	where a.relationshipgroup != 0 
	and a.active ='1'
	and b.active = '1'
	group by a.sourceid, a.relationshipgroup
	having count(*) = 1
