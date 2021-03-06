
/******************************************************************************** 
	cs-parser-concept-basic-counts

	Assertion:
	Before more complex operations, ensure proper counts exist.

********************************************************************************/
	
	drop view if exists v_allid;
	drop view if exists v_newid;
	drop view if exists v_existingid;
	drop view if exists v_totalcount;
	drop view if exists v_maxidtime;
	drop view if exists v_maxidtimecount;

	
	-- All distinct Ids in CS
	create view v_allid as
	select count(distinct(a.id)) as allidcount from cs_concept a;
	
	
	-- Count of Ids that existed in previous release
	create view v_existingid as
	select count(*) as existingidcount 
	from v_allid a, prev_concept_s b
	where a.allidcount = b.id;
		
	-- Count of Ids that are new in current release
	create view v_newid as
	select count(*) as newidcount from v_allid a
	left join prev_concept_s b on a.allidcount = b.id
	where b.id is null;
	
	-- Sum of new and existing Ids 
	create view v_totalcount as
	select (a.newidcount + b.existingidcount) as totalcount 
	from v_newid a, v_existingid b;
	 
	-- Map all ids to latest committime
	create view v_maxidtime as
	select id, max(commitTime) as commitTime from cs_concept 
	group by id; 
	
	-- Count of latest committime Ids 
	create view v_maxidtimecount as
	select count(*) as maxidtimecount from v_maxidtime;


	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('The number of mapped distinct CommitTime-Ids to distinct Ids from the concept changeset file is incorrect') 	
	from v_allid a, v_maxidtimecount b
	where a.allidcount != b.maxidtimecount;
	
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('The Sum of new and existing Ids to distinct Ids from the concept changeset file is incorrect') 	
	from v_allid a, v_totalcount b
	where a.allidcount != b.totalcount;
	
	
	
	
	