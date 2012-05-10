
/******************************************************************************** 
	cs-parser-relationship-new-missing-rf2

	Assertion:
	Find new Relationship Ids found is in CS_ but missing in RF2.

********************************************************************************/
	
	drop view if exists v_allid;
	drop view if exists v_newid;
	drop view if exists v_maxidtime;
	drop view if exists v_newrelationship;
	drop table if exists newmaxattribute_tmp;
	drop table if exists newinactive_tmp;
	drop table if exists missingrf2new_tmp;


	/* Prep */
	-- All distinct Ids in CS
	create view v_allid as
	select distinct(a.id) from cs_relationship a;


	-- SCTIDs that new to current release
	create view v_newid as
	select a.* from v_allid a
	left join prev_stated_relationship_s b on a.id = b.id
	where b.id is null;

	-- map all ids to latest committime
	create view v_maxidtime as
	select id, max(committime) as committime from cs_relationship 
	group by id; 

	-- all attributes of relationships that are new in current release 
	create view v_newrelationship as 
	select a.* from cs_relationship a, v_newid b 
	where a.id = b.id;  

	-- Latest timestamp of relationships thast are new in current release
	create table newmaxattribute_tmp as 
	select a.* from v_newrelationship a, v_maxidtime b
	where a.id = b.id
	and a.committime = b.committime;




	

	/* Analysis */
	-- Relationships that were created in current release but were then inactivated
	create table newinactive_tmp as 
	select * from newmaxattribute_tmp where active = 0;

	-- Relationships that were created in current release but were then inactivated
	create table missingrf2new_tmp as 
	select a.* from newmaxattribute_tmp a 
	left join curr_relationship_d b on a.id = b.id 
	where b.id is null; 

	delete from missingrf2new_tmp
	where id in (
		select id from newinactive_tmp
	);
	
	




	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('Relationship: id=',id, ': Relationship that is new in current release is referenced in change set file but not in RF2.') 
	from missingrf2new_tmp;
	
	
	
