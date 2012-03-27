
/******************************************************************************** 
	snapshot-language-successive-states

	Assertion:
	All members inactivated in current release must have been active in the previous release.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		89,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Member Id is inactived in current release, yet was already inactive in previous release.') 
	
	from curr_langrefset_s a, prev_langrefset_s b
	where a.active = '0' and 
	      b.active != '1' and 
	      a.id = b.id and 
	      a.effectivetime = <CURRENT-RELEASE-DATE>
