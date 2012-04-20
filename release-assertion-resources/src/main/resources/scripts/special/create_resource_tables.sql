/* 
	For each table:
		1) Drop table if exists
		2) Create table
		3) Create Indices (if necessary)
*/



drop table if exists res_navigationconcepts;

create table res_navigationconcepts (
	 id		varchar(36),
	 effectiveTime	varchar(255),
	 active		varchar(255),
	 moduleId	varchar(255),
	 refsetId	varchar(255),
	 referencedComponentId	varchar(255),
	 ordernum	varchar(255),
 	linkedTo	varchar(255)
);
create index idx_navconcepts on res_navigationconcepts(referencedComponentId);
 

 
 
drop table if exists res_gbterms;
 
create table res_gbterms (
	term	varchar(255)
);
create index idx_gbtermsTerm on res_gbterms(term);




drop table if exists res_usterms;

create table res_usterms (
	term	varchar(255)
);
create index idx_ustermsTerm on res_usterms(term);




drop table if exists res_semantictags;

create table res_semantictags(
   semantictag VARCHAR(255) not null,      
   id VARCHAR(36) not null      
);
    

    
drop table if exists res_casesensitiveTerms;
    
create table res_casesensitiveTerms(
   casesensitiveTerm VARCHAR(255) not null
);
create index idx_casesensitiveTerm on res_casesensitiveTerms(casesensitiveTerm);

     