
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
		concat('Concept: id=',a.id, ': Concept contains more than one definition for a given dialect.') 
	select b.*
		from curr_concept_s a
		inner join curr_textdefinition_s b
		on a.id = b.conceptid
		where a.active = '1' 
		and b.active = '1'
		GROUP BY b.conceptid, b.languagecode
		having count(*) > (
				select count(distinct (languagecode)) 
				from curr_textdefinition_s 
				where active = '1'
				and typeid = '900000000000550004'
			);
			
			
			

	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Concept: id=',a.id, ': Concept does not contain a definition for a given dialect.') 
	select * 
		from curr_concept_s 
		where active = '1'
		and  id not in (
			select conceptid 
			from curr_textdefinition_s 
			where active = '1' 
			and typeid = '900000000000550004'
			GROUP BY conceptid, languagecode
			having count(*) = (
				select count(distinct (languagecode)) 
				from curr_textdefinition_s 
				where active = '1'
				and typeid = '900000000000550004'
			)
		);