
/******************************************************************************** 
	file-centric-snapshot-language-unique-fsn

	Assertion:
	Every concept's FSN exists exactly once in each language refset.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept does not contain exactly one fully specified name in a given language refset.') 
	
	from curr_description_s a 
	inner join  curr_langrefset_s b on a.id = b.referencedcomponentid 
	where a.typeid = '900000000000003001'
	GROUP BY b.referencedcomponentid, b.refsetid
	having count(b.referencedcomponentid) != 1