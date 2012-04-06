
/******************************************************************************** 
	component-centric-snapshot-language-unique-term

	Assertion:
	Terms in a language refset are unique for a given concept.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.conceptid, ': Concept has duplicate terms within a single language refset.') 
	from curr_description_s a
	inner join curr_langrefset_s b on a.id = b.referencedcomponentid
	inner join curr_concept_s c	on a.conceptid = c.id
	where a.active = '1'
	and b.active = '1'
	and c.active = '1'
	GROUP BY a.conceptid, b.refsetid, a.term
	having  count(*) > 1
