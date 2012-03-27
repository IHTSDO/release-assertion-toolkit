
/******************************************************************************** 
	snapshot-language-unique-fsn

	Assertion:
	Every concept's FSN exists exactly once in each language refset.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		89,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept Id's FSN doesn't exist in each language refset or exists more than once in a given language refset.') 
	
	from curr_description_s a 
	inner join  curr_langrefset_s b on a.id = b.referencedcomponentid 
	where a.typeid = '900000000000003001' -- fsn
	GROUP BY b.referencedcomponentid, b.refsetid
	having count(b.referencedcomponentid) != 1