
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
		where referencedcomponentid not in (select id from curr_description_s) and
		referencedcomponentid not in (select id from curr_textdefinition_s) 
