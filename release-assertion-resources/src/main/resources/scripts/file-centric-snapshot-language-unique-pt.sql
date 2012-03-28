
/******************************************************************************** 
	snapshot-language-unique-pt

	Assertion:
	Every concept's Preferred Term exists exactly once in each language refset.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept Id's Preferred Term doesn't exist in each language refset or exists more than once in a given language refset.') 
	
	from curr_description_s a 
	INNER JOIN curr_langrefset_s b on a.id = b.referencedComponentid
        where b.acceptabilityid = '900000000000548007' -- preferred
        GROUP BY b.referencedcomponentid, b.refsetid
        having count(b.referencedcomponentid) != 1