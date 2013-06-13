
/******************************************************************************** 
	component-centric-snapshot-language-existing-term

	Assertion:
	Active descriptions have a reference in language refset.

	limiting the implementation to active descriptions snapshot

********************************************************************************/

/* 	violators are descriptions that has not a reference in language refset  */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESCRIPTION: id=',b.id, ': Description has not a reference in language refset.')
	from curr_description_s b
		left join curr_langrefset_s c
			on b.id = c.referencedcomponentid
		where b.active=1 and c.id is null;
	commit;
	
	