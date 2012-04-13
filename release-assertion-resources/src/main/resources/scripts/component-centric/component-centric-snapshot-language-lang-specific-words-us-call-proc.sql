
/******************************************************************************** 
	component-centric-snapshot-language-lang-specific-words-us-call-proc

	Assertion:
	Calling procedure testing that terms that contain EN-US
	language-specific words are in the same US language refset.

********************************************************************************/

	create or replace view v_curr_snapshot as
	select a.id, a.term
		from curr_description_s a 
		inner join curr_langrefset_s b on a.id = b.referencedComponentId
		and a.active = '1'
		and b.active = '1'
		and b.refsetid = '900000000000509007';		

	
	call  usTerm_procedure();

	drop view v_curr_snapshot;