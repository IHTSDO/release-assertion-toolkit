
/******************************************************************************** 
	file-centric-snapshot-description-synonym-tag

	Assertion:
	For each active FSN associated with active concept there is a synonym that has the same text.

********************************************************************************/
	
/* 	view of current snapshot made by finding active FSN's and removing tags */


	create table v_curr_snapshot as
	select SUBSTRING_INDEX(term, '(', 1) as term , a.id , a.conceptid
	from curr_description_s a , curr_concept_s b
	where a.typeid in ('900000000000003001')
	and a.active = 1
	and a.conceptid = b.id
	and b.active = 1;
	
/* 	finding all the active FSN's match with corresponding synonym */	
	
	create table v_curr_snapshot_2 as
	select a.* 
	from v_curr_snapshot a ,  curr_description_s b
	where b.typeid in ('900000000000013009')
	and a.conceptid = b.conceptid
	and a.term = b.term;
	 
	select *
	from v_curr_snapshot a 
	left join v_curr_snapshot_2 b
	on a.id = b.id
	where b.id is null;
	

select a.targetcomponentid
	from curr_associationrefset_s a
	left join curr_concept_s b
	on a.targetcomponentid = b.id
	where b.id is null;
	

	

	
/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: term=',a.term, ':Synonyms with semantic tag.') 	
	from v_curr_snapshot a;


	drop table v_curr_snapshot;

	