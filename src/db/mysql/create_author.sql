DROP TABLE　IF EXISTS AAUTHOR;
CREATE TABLE AAUTHOR(
	IID BIGINT NOT NULL AUTO_INCREMENT,
	NNAME VARCHAR(128),
	AAGE BIGINT,
	SSEX BIGINT,
	BBIRTHDAY VARCHAR(128),
	CCREATE_TIME VARCHAR(40),
	MMODIFY_TIME VARCHAR(40)
);