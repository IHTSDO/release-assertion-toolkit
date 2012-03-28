
/******************************************************************************** 
	file-centric-snapshot-language-successive-states

	Assertion:
	All members inactivated in current release must have been active in the previous release.

********************************************************************************/
	
	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('MEMBER: id=',a.id, ': Member Id is inactived in current release, yet was already inactive in previous release.') 
	
	from curr_langrefset_s a
	inner join prev_langrefset_s b on a.id = b.id 
	where a.active = '0' and 
	      b.active = '0' and 
	      a.effectivetime = <CURRENT-RELEASE-DATE>
