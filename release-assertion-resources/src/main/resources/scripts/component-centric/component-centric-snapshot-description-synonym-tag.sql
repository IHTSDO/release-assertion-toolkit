
/******************************************************************************** 
	file-centric-snapshot-description-synonym-tag

	Assertion:
	No active synonyms associated with active concepts have semantic tags.

********************************************************************************/
	
/* 	view of current snapshot made by finding active synonyms with semantic tags */


	create or replace view v_curr_snapshot as
	select SUBSTRING(a.term,LOCATE('(',a.term)) as term  , b.id
	from curr_description_s a , curr_concept_s b
	where a.typeid in ('900000000000013009')
	and a.active = 1
	and a.conceptid = b.id
	and b.active = 1;
	
	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESC: ID=',a.id, ':Synonyms with semantic tag.') 	
	from v_curr_snapshot a;


	drop view v_curr_snapshot;

	