
/******************************************************************************** 
	component-centric-snapshot-language-lang-specific-words-gb-call-proc

	Assertion:
	Calling procedure testing that terms that contain EN-GB 
	language-specific words are in the same GB language refset.

********************************************************************************/

	create or replace view v_curr_snapshot as
	select a.id, a.term
		from curr_description_s a 
		inner join curr_langrefset_s b on a.id = b.referencedComponentId
		and a.active = '1'
		and b.active = '1'
		and b.refsetid = '900000000000508004';		


	call  gbTerm_procedure();

	drop view v_curr_snapshot;
