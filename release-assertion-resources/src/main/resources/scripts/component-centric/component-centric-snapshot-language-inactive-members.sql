
/******************************************************************************** 
	component-centric-snapshot-language-inactive-members

	Assertion:
	Members are inactive for inactive descriptions in the snapshot file.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Member: id=',b.id, ': Refset member is active for an inactive description.') 
	
	from curr_description_s a
	inner join curr_langrefset_s b
	on a.id = b.referencedcomponentid
	inner join curr_concept_s c
	on a.conceptid = c.id
	where a.active = '0'
	and b.active = '1'
	and c.active = '1'
