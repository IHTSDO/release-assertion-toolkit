/* 
	For each execution table:
		Create only if table does not already exist
	For qa_run only:  Must initialize the table with first runid
*/


create table if not exists qa_run (
	effectivetime DATETIME,
	runid BIGINT not null auto_increment,
	primary key (runid)
);



create table if not exists qa_result (
	runid BIGINT,
	assertionuuid CHAR(36),
	assertiontext VARCHAR(255),
	details VARCHAR(255)
);


create table if not exists qa_report (
	runid BIGINT,
	assertionuuid CHAR(36),
	assertiontext VARCHAR(255),
	result CHAR(1),
	count BIGINT
);









/* 
	For each resource table:
		1) Drop table if exists
		2) Create table
		3) Create Indices (if necessary)
*/


drop table if exists res_navigationconcept;

create table res_navigationconcept (
	 id		varchar(36),
	 effectiveTime	varchar(255),
	 active		varchar(255),
	 moduleId	varchar(255),
	 refsetId	varchar(255),
	 referencedComponentId	varchar(255),
	 ordernum	varchar(255),
 	linkedTo	varchar(255)
);
create index idx_navconcepts on res_navigationconcept(referencedComponentId);


drop table if exists res_gbterm;

create table res_gbterm (
	term	varchar(255)
);
create index idx_gbtermsTerm on res_gbterm(term);


drop table if exists res_usterm;

create table res_usterm (
	term	varchar(255)
);
create index idx_ustermsTerm on res_usterm(term);


drop table if exists res_semantictag;

create table res_semantictag(
   semantictag VARCHAR(255) not null,      
   id VARCHAR(36) not null      
);


drop table if exists res_casesensitiveTerm;

create table res_casesensitiveTerm(
   casesensitiveTerm VARCHAR(255) not null
);
create index idx_casesensitiveTerm on res_casesensitiveTerm(casesensitiveTerm);

insert IGNORE into qa_run values('2002-01-01', 1);     