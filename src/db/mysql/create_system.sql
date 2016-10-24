DROP TABLE IF EXISTS SYS_ORGANIZATION;
DROP TABLE IF EXISTS SYS_ORG_ROLE_RELATION;
DROP TABLE IF EXISTS SYS_ROLE;
DROP TABLE IF EXISTS SYS_USER;
DROP TABLE IF EXISTS SYS_USER_ROLE_RELATION;
DROP TABLE IF EXISTS SYS_OPERATION_RECORD;
DROP TABLE IF EXISTS SYS_MENUS;
DROP TABLE IF EXISTS SYS_ROLE_PERMISSION_RELATION;
DROP TABLE IF EXISTS HHOST_STATE;
DROP TABLE IF EXISTS AAPP_STATE;
DROP TABLE IF EXISTS OONLINE_OPERATOR;

CREATE TABLE SYS_ORGANIZATION(
	IID BIGINT,
	NNAME varchar(100),
	CCODE varchar(60),
	CCERTIFICATE_TYPE varchar(10),
	CCERTIFICATE_NUMBER varchar(60),
	LLINKMAN varchar(60),
	PPHONE varchar(20),
	FFAX varchar(40),
	EEMAIL varchar(40),
	ZZIP varchar(40),
	AADDRESS varchar(104),
	TTYPE BIGINT,
	SSTATE varchar(24),
	RREMARK varchar(1024),
	PPARENT BIGINT,
	PPASSWORD varchar(40),
	PRIMARY KEY(IID)
);
INSERT INTO SYS_ORGANIZATION(IID,NNAME,CCERTIFICATE_TYPE,CCERTIFICATE_NUMBER,LLINKMAN,PPHONE,FFAX,
EEMAIL,ZZIP,AADDRESS,TTYPE,SSTATE,RREMARK,PPARENT,PPASSWORD,CCODE) 
values(100000,'组织','1','1','1','1','1','1','1','1','1','1','1','0','1','1');

CREATE TABLE OONLINE_OPERATOR(
	IID BIGINT,
	OPERATOR_COUNT Integer,
	SESSION_COUNT Integer,
	CCREATE_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);

CREATE TABLE BBACKUP_DATA_HISTORY (
	IID BIGINT,
	FFILE_NAME VARCHAR(128),
	TTYPE INTEGER,
	PPATH VARCHAR(128),
	CCREATE_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);

CREATE TABLE BBACKUP_LOG_HISTORY (
	IID BIGINT,
	FFILE_NAME VARCHAR(128),
	PPATH VARCHAR(128),
	CCREATE_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);

CREATE TABLE HHOST_STATE (
	IID BIGINT,
	NNAME VARCHAR(128),
	VVERSION VARCHAR(64),
	CCPU INTEGER,
	MMEMORY BIGINT,
	TTOTAL_STORAGE BIGINT,
	UUSED_STORAGE BIGINT,
	CCREATE_TIME VARCHAR(32),
	UUP_TIME INTEGER,
	SSTART_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);

DROP TABLE IF EXISTS AAPP_STATE;
CREATE TABLE AAPP_STATE (
	IID BIGINT,
	HEAP_MEMORY BIGINT,
	NON_HEAP_MEMORY BIGINT,
	THREAD_COUNT INTEGER,
	SSTART_TIME VARCHAR(32),
	UUP_TIME INTEGER,
	CCREATE_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_ORG_ROLE_RELATION(
	IID BIGINT,
	OORGANIZATION_ID BIGINT,
	RROLE_ID BIGINT,
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_ROLE_PERMISSION_RELATION(
	IID BIGINT,
	RROLE_ID BIGINT,
	PPERMISSION_ID BIGINT,
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_ROLE(
	IID BIGINT,
	NNAME varchar(100),
	TTYPE INTEGER,
	DDESC varchar(1024),
	CCREATE_TIME VARCHAR(32),
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_USER_ROLE_RELATION(
	IID BIGINT,
	UUSER_ID BIGINT,
	RROLE_ID BIGINT,
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_USER(
	IID BIGINT,
	LLOGIN_NAME varchar(100),
	PPASSWORD varchar(40),
	NNAME varchar(100),
	CCOMPANY varchar(100),
	PPHONE varchar(60),
	EEMAIL varchar(40),
	FFAX varchar(40),
	AADDRESS varchar(60),
	ZZIP varchar(40),
	RREMARK varchar(104),
	OORGANIZATION_ID BIGINT,
	LLANGUAGE VARCHAR(16),
	PRIMARY KEY(IID)
);
INSERT INTO SYS_USER(IID,LLOGIN_NAME,PPASSWORD,NNAME,LLANGUAGE,OORGANIZATION_ID) values(100000,'admin','21232f297a57a5a743894a0e4a801fc3','系统管理员','zh_CN','100000');


CREATE TABLE SYS_OPERATION_RECORD(
	IID BIGINT,
	LLOGIN_NAME varchar(100),
	TTIME varchar(40),
	DDESC varchar(1024),
	RREMARK varchar(1024),
	PRIMARY KEY(IID)
);


CREATE TABLE SYS_MENUS(
	IID BIGINT,
	NNAME varchar(100),
	UURL varchar(100),
	DDESC varchar(1024),
	PPARENTID BIGINT,
	CCODE BIGINT,
	PRIMARY KEY(IID)
);



DROP TABLE IF EXISTS MMULTI_LANGUAGE;
CREATE TABLE MMULTI_LANGUAGE(
	IID BIGINT,
	RRECORD_ID BIGINT,
	LLANGUAGE VARCHAR(50),
	KKEY VARCHAR(255),
	CCONTENT VARCHAR(255),
	PRIMARY KEY(IID)
);
