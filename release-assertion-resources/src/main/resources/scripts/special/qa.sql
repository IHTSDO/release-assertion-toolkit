create table qa_report(
   runid BIGINT,
   assertionuuid CHAR(36),
   assertiontext VARCHAR(255),
   result CHAR(1),
   count BIGINT
);    

create table qa_result(
   runid BIGINT,
   assertionuuid CHAR(36),
   assertiontext VARCHAR(255),
   details VARCHAR(255)
);

create table qa_run(
   effectivetime DATETIME,
   runid BIGINT not null auto_increment,
   primary key (runid)
);

create unique index idx_runid on qa_run(runid);
    