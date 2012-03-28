
/******************************************************************************** 
	file-centric-snapshot-language-valid-descriptionid

	Assertion:
	All members refer to a description that is referenced in either the 
	description table or the text definition table.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('MEMBER: id=',a.id, ': Member Id refers to a description Id that is not found in either the description table nor the text definition table') 
	
 		from curr_langrefset_s a
 		left join curr_description_s b on a.referencedcomponentid = b.id
 		left join curr_textdefinition_s c on a.referencedcomponentid = c.id
		where  b.id is null
 		and c.id is null

