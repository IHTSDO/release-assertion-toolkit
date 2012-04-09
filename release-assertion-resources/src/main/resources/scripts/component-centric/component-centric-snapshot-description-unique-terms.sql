
/******************************************************************************** 
	component-centric-snapshot-description-unique-terms

	Assertion:
	Active Terms associated with active concepts are unique in Description hierarchy.

********************************************************************************/
	
/* 	view of current snapshot made by finding active terms associated with active concepts */
	create or replace view v_curr_snapshot_1 as
	select a.* 
	from curr_description_s a , curr_concept_s b
	where a.active = 1
	and a.conceptid = b.id
	and b.active = 1;
	
/* 	view of current snapshot made by matching active terms with inactive terms for active concepts */	
	create or replace view v_curr_snapshot_2 as
	select a.conceptid , a.term as synonym , c.term as fsn , a.id as synonymid, c.id as fsnid , SUBSTRING_INDEX(c.term, '(', -1) as hierarchytag
	from curr_description_s a , curr_concept_s b , v_curr_snapshot_1 c
	where a.active = 1
	and b.active = 1
	and c.typeid =  '900000000000003001'
	and a.conceptid = b.id	
	and a.conceptid = c.conceptid
	and a.typeid != '900000000000003001';	
	
	create or replace view v_curr_snapshot_3 as
	select b.fsn , b.hierarchytag  , b.fsnid
	from res_semantictags a , v_curr_snapshot_2 b
	where a.semantictag = replace(b.hierarchytag, ')' , '')
	group by BINARY b.fsn , b.hierarchytag
	having count(b.fsn) > 1 and count(b.hierarchytag) > 1;
	
	create or replace view v_curr_snapshot_4 as
	select b.synonym , b.hierarchytag , b.synonymid
	from res_semantictags a , v_curr_snapshot_2 b
	where a.semantictag = replace(b.hierarchytag, ')' , '')
	group by BINARY b.synonym , b.hierarchytag
	having count(b.synonym) > 1 and count(b.hierarchytag) > 1;


/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESCRIPTION: term=',a.fsnid, ':Term not unique in Description hierarchy.') 	
	from v_curr_snapshot_3 a ;

/* 	inserting exceptions in the result table */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('DESCRIPTION: term=',a.synonymid, ':Term not unique in Description hierarchy.') 	
	from v_curr_snapshot_4 a ;
	

	drop view v_curr_snapshot_1;
	drop view v_curr_snapshot_2;
	drop view v_curr_snapshot_3;	
	drop view v_curr_snapshot_4;
		