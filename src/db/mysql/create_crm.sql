DROP TABLE IF EXISTS CCRM_CUSTOMER;
CREATE TABLE CCRM_CUSTOMER(
	IID BIGINT,
	SSTATE varchar(60),
	IIDENTITY_TYPE INTEGER,
	IIDENTITY_NUMBER varchar(60),
	NNAME varchar(60),
	DDEFINED_ID varchar(40),
	PPASSWORD varchar(40),
	TTYPE INTEGER,
	LLEVEL INTEGER,
	CCREDIT_DEGREE INTEGER,
	PPHONE varchar(20),
	AADDRESS varchar(80),
	DDEFINED_ADDRESS varchar(80),
	CCREATE_TIME varchar(40),
	MMODIFY_TIME varchar(40),
	PPARENT_GROUP varchar(40),
	AALIAS varchar(100),
	GGENDER varchar(80),
	BBIRTH_DATE varchar(40),
	MMARRIED varchar(80),
	RRELIGION varchar(40),
	FFAVOURITE varchar(100),
	OOTHER_PHONE varchar(40),
	AADDRESS_ALIAS varchar(80),
	MMAIL_ADDR varchar(104),
	ZZIP varchar(60),
	LLINK_NAME varchar(24),
	NNEIGHBOR varchar(100),
	EEMAIL varchar(40),
	FFAX varchar(40),
	
	RREMARK varchar(1024),
	WWORK_UNIT varchar(100),
	UUNIT_KIND varchar(200),
	OOFFICE_PHONE varchar(60),
	JJOB_TYPE varchar(80),
	EEDUCATION_LEVEL varchar(200),
	IINCOME_LEVEL varchar(80),
	AAGENT_ID varchar(80),
	AACCOUNT_BANK varchar(200),
	AACCOUNT_NAME varchar(104),
	AACCOUNT_ID varchar(104),
	PRIMARY KEY(IID)
);

DROP TABLE　IF EXISTS CCRM_INTEREST;
CREATE TABLE CCRM_INTEREST(
	IID BIGINT,
	CCUSTOMER_ID BIGINT,
	BBOOK_ID BIGINT,
	IINTEREST_DESC VARCHAR(1024),
	CCREATE_TIME varchar(40),
	MMODIFY_TIME varchar(40),
	
);