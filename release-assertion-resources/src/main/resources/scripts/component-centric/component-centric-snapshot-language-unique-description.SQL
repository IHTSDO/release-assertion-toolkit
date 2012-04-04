
/******************************************************************************** 
	component-centric-snapshot-language-unique-description

	Assertion:
	There is one and only one active Definition per concept per dialect in 
	the snapshot file.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept does not contain exactly one definition for a given dialect.') 
	
	from curr_concept_s a
	inner join curr_textdefinition_s b
	on a.id = b.conceptid
	where a.active = '1' 
	and b.active = '1'
	GROUP BY b.conceptid, b.languagecode
	having count(*) != 1;
