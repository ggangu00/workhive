/* Sequence 저장테이블 */
CREATE TABLE COMTECOPSEQ (
	TABLE_NAME            VARCHAR2(20)  NOT NULL,
	NEXT_ID               NUMBER(30)  NULL,
CONSTRAINT COMTECOPSEQ_PK PRIMARY KEY (TABLE_NAME)
);






/* 공통분류코드 */
CREATE TABLE COMTCCMMNCLCODE (
	CL_CODE               CHAR(3)  NOT NULL,
	CL_CODE_NM            VARCHAR2(60)  NULL,
	CL_CODE_DC            VARCHAR2(200)  NULL,
	USE_AT                CHAR(1)  NULL,
	FRST_REGIST_PNTTM     DATE  NULL,
	FRST_REGISTER_ID      VARCHAR2(20)  NULL,
	LAST_UPDT_PNTTM       DATE  NULL,
	LAST_UPDUSR_ID        VARCHAR2(20)  NULL,
CONSTRAINT COMTCCMMNCLCODE_PK PRIMARY KEY (CL_CODE)
);






/* 공통코드 */
CREATE TABLE COMTCCMMNCODE (
	CODE_ID               VARCHAR2(6)  NOT NULL,
	CODE_ID_NM            VARCHAR2(60)  NULL,
	CODE_ID_DC            VARCHAR2(200)  NULL,
	USE_AT                CHAR(1)  NULL,
	CL_CODE               CHAR(3)  NULL,
	FRST_REGIST_PNTTM     DATE  NULL,
	FRST_REGISTER_ID      VARCHAR2(20)  NULL,
	LAST_UPDT_PNTTM       DATE  NULL,
	LAST_UPDUSR_ID        VARCHAR2(20)  NULL,
CONSTRAINT COMTCCMMNCODE_PK PRIMARY KEY (CODE_ID)
);

CREATE INDEX COMTCCMMNCODE_i01 ON COMTCCMMNCODE (CL_CODE  ASC);





/* 공통상세코드 */
CREATE TABLE COMTCCMMNDETAILCODE (
	CODE_ID               VARCHAR2(6)  NOT NULL,
	CODE                  VARCHAR2(15)  NOT NULL,
	CODE_NM               VARCHAR2(60)  NULL,
	CODE_DC               VARCHAR2(200)  NULL,
	USE_AT                CHAR(1)  NULL,
	FRST_REGIST_PNTTM     DATE  NULL,
	FRST_REGISTER_ID      VARCHAR2(20)  NULL,
	LAST_UPDT_PNTTM       DATE  NULL,
	LAST_UPDUSR_ID        VARCHAR2(20)  NULL,
CONSTRAINT COMTCCMMNDETAILCODE_PK PRIMARY KEY (CODE_ID,CODE)
);

CREATE INDEX COMTCCMMNDETAILCODE_i01 ON COMTCCMMNDETAILCODE (CODE_ID  ASC);





/* 권한그룹정보 */
CREATE TABLE COMTNAUTHORGROUPINFO (
	GROUP_ID              CHAR(20)  NOT NULL,
	GROUP_NM              VARCHAR2(60)  NOT NULL,
	GROUP_CREAT_DE        CHAR(20)  NOT NULL,
	GROUP_DC              VARCHAR2(100)  NULL,
CONSTRAINT COMTNAUTHORGROUPINFO_PK PRIMARY KEY (GROUP_ID)
);






/* 조직정보 */
CREATE TABLE COMTNORGNZTINFO (
	ORGNZT_ID             CHAR(20)  NOT NULL,
	ORGNZT_NM             VARCHAR2(20)  NOT NULL,
	ORGNZT_DC             VARCHAR2(100)  NULL,
CONSTRAINT COMTNORGNZTINFO_PK PRIMARY KEY (ORGNZT_ID)
);






/* 파일속성 */
CREATE TABLE COMTNFILE (
	ATCH_FILE_ID          CHAR(20)  NOT NULL,
	CREAT_DT              DATE  NOT NULL,
	USE_AT                CHAR(1)  NULL,
CONSTRAINT COMTNFILE_PK PRIMARY KEY (ATCH_FILE_ID)
);






/* 파일상세정보 */
CREATE TABLE COMTNFILEDETAIL (
	ATCH_FILE_ID          CHAR(20)  NOT NULL,
	FILE_SN               NUMBER(10)  NOT NULL,
	FILE_STRE_COURS       VARCHAR2(2000)  NOT NULL,
	STRE_FILE_NM          VARCHAR2(255)  NOT NULL,
	ORIGNL_FILE_NM        VARCHAR2(255)  NULL,
	FILE_EXTSN            VARCHAR2(20)  NOT NULL,
	FILE_CN               LONG VARCHAR  NULL,
	FILE_SIZE             NUMBER(8)  NULL,
CONSTRAINT COMTNFILEDETAIL_PK PRIMARY KEY (ATCH_FILE_ID,FILE_SN)
);

CREATE INDEX COMTNFILEDETAIL_i01 ON COMTNFILEDETAIL (ATCH_FILE_ID  ASC);





/* 메일발신관리 */
CREATE TABLE COMTHEMAILDSPTCHMANAGE (
	MSSAGE_ID             VARCHAR2(20)  NOT NULL,
	EMAIL_CN              LONG VARCHAR  NULL,
	SNDR                  VARCHAR2(50)  NOT NULL,
	RCVER                 VARCHAR2(50)  NOT NULL,
	SJ                    VARCHAR2(60)  NOT NULL,
	SNDNG_RESULT_CODE     CHAR(1)  NULL,
	DSPTCH_DT             CHAR(20)  NOT NULL,
	ATCH_FILE_ID          CHAR(20)  NULL,
CONSTRAINT COMTHEMAILDSPTCHMANAGE_PK PRIMARY KEY (MSSAGE_ID)
);

CREATE INDEX COMTHEMAILDSPTCHMANAGE_i01 ON COMTHEMAILDSPTCHMANAGE (SNDR  ASC);





/* 일반회원 */
CREATE TABLE COMTNGNRLMBER (
	MBER_ID               VARCHAR2(20)  NOT NULL,
	PASSWORD              VARCHAR2(200)  NOT NULL,
	PASSWORD_HINT         VARCHAR2(100)  NULL,
	PASSWORD_CNSR         VARCHAR2(100)  NULL,
	IHIDNUM               VARCHAR2(200)  NULL,
	MBER_NM               VARCHAR2(50)  NOT NULL,
	ZIP                   VARCHAR2(6)  NOT NULL,
	ADRES                 VARCHAR2(100)  NOT NULL,
	AREA_NO               VARCHAR2(4)  NOT NULL,
	MBER_STTUS            VARCHAR2(15)  NULL,
	DETAIL_ADRES          VARCHAR2(100)  NULL,
	END_TELNO             VARCHAR2(4)  NOT NULL,
	MBTLNUM               VARCHAR2(20)  NOT NULL,
	GROUP_ID              CHAR(20)  NULL,
	MBER_FXNUM            VARCHAR2(20)  NULL,
	MBER_EMAIL_ADRES      VARCHAR2(50)  NULL,
	MIDDLE_TELNO          VARCHAR2(4)  NOT NULL,
	SBSCRB_DE             DATE  NULL,
	SEXDSTN_CODE          CHAR(1)  NULL,
	ESNTL_ID              CHAR(20)  NOT NULL,
	LOCK_AT               CHAR(1) NULL,
	LOCK_CNT              NUMBER(3) NULL,
	LOCK_LAST_PNTTM       DATE NULL,
	CHG_PWD_LAST_PNTTM    DATE NULL,
CONSTRAINT COMTNGNRLMBER_PK PRIMARY KEY (MBER_ID)
);

CREATE INDEX COMTNGNRLMBER_i01 ON COMTNGNRLMBER (GROUP_ID  ASC);





/* 업무사용자정보 */
CREATE TABLE COMTNEMPLYRINFO (
	EMPLYR_ID             VARCHAR2(20)  NOT NULL,
	ORGNZT_ID             CHAR(20)  NULL,
	USER_NM               VARCHAR2(60)  NOT NULL,
	PASSWORD              VARCHAR2(200)  NOT NULL,
	EMPL_NO               VARCHAR2(20)  NULL,
	IHIDNUM               VARCHAR2(200)  NULL,
	SEXDSTN_CODE          CHAR(1)  NULL,
	BRTHDY                CHAR(20)  NULL,
	FXNUM                 VARCHAR2(20)  NULL,
	HOUSE_ADRES           VARCHAR2(100)  NOT NULL,
	PASSWORD_HINT         VARCHAR2(100)  NOT NULL,
	PASSWORD_CNSR         VARCHAR2(100)  NOT NULL,
	HOUSE_END_TELNO       VARCHAR2(4)  NOT NULL,
	AREA_NO               VARCHAR2(4)  NOT NULL,
	DETAIL_ADRES          VARCHAR2(100)  NULL,
	ZIP                   VARCHAR2(6)  NOT NULL,
	OFFM_TELNO            VARCHAR2(20)  NULL,
	MBTLNUM               VARCHAR2(20)  NULL,
	EMAIL_ADRES           VARCHAR2(50)  NULL,
	OFCPS_NM              VARCHAR2(60)  NULL,
	HOUSE_MIDDLE_TELNO    VARCHAR2(4)  NOT NULL,
	GROUP_ID              CHAR(20)  NULL,
	PSTINST_CODE          CHAR(8)  NULL,
	EMPLYR_STTUS_CODE     CHAR(1)  NOT NULL,
	ESNTL_ID              CHAR(20)  NOT NULL,
	CRTFC_DN_VALUE        VARCHAR2(100)  NULL,
	SBSCRB_DE             DATE  NULL,
	LOCK_AT               CHAR(1) NULL,
	LOCK_CNT              NUMBER(3) NULL,
	LOCK_LAST_PNTTM       DATE NULL,
	CHG_PWD_LAST_PNTTM    DATE NULL,
CONSTRAINT COMTNEMPLYRINFO_PK PRIMARY KEY (EMPLYR_ID)
);

CREATE INDEX COMTNEMPLYRINFO_i01 ON COMTNEMPLYRINFO (ORGNZT_ID  ASC);
CREATE INDEX COMTNEMPLYRINFO_i02 ON COMTNEMPLYRINFO (GROUP_ID  ASC);





/* 기업회원 */
CREATE TABLE COMTNENTRPRSMBER (
	ENTRPRS_MBER_ID       VARCHAR2(20)  NOT NULL,
	ENTRPRS_SE_CODE       CHAR(8)  NULL,
	BIZRNO                VARCHAR2(10)  NULL,
	JURIRNO               VARCHAR2(13)  NULL,
	CMPNY_NM              VARCHAR2(60)  NOT NULL,
	CXFC                  VARCHAR2(50)  NULL,
	ZIP                   VARCHAR2(6)  NOT NULL,
	ADRES                 VARCHAR2(100)  NOT NULL,
	ENTRPRS_MIDDLE_TELNO  VARCHAR2(4)  NOT NULL,
	FXNUM                 VARCHAR2(20)  NULL,
	INDUTY_CODE           CHAR(1)  NULL,
	APPLCNT_NM            VARCHAR2(50)  NOT NULL,
	APPLCNT_IHIDNUM       VARCHAR2(200)  NULL,
	SBSCRB_DE             DATE  NULL,
	ENTRPRS_MBER_STTUS    VARCHAR2(15)  NULL,
	ENTRPRS_MBER_PASSWORD  VARCHAR2(200)  NULL,
	ENTRPRS_MBER_PASSWORD_HINT  VARCHAR2(100)  NOT NULL,
	ENTRPRS_MBER_PASSWORD_CNSR  VARCHAR2(100)  NOT NULL,
	GROUP_ID              CHAR(20)  NULL,
	DETAIL_ADRES          VARCHAR2(100)  NULL,
	ENTRPRS_END_TELNO     VARCHAR2(4)  NOT NULL,
	AREA_NO               VARCHAR2(4)  NOT NULL,
	APPLCNT_EMAIL_ADRES   VARCHAR2(50)  NOT NULL,
	ESNTL_ID              CHAR(20)  NOT NULL,
	LOCK_AT               CHAR(1) NULL,
	LOCK_CNT              NUMBER(3) NULL,
	LOCK_LAST_PNTTM       DATE NULL,
	CHG_PWD_LAST_PNTTM    DATE NULL,
CONSTRAINT COMTNENTRPRSMBER_PK PRIMARY KEY (ENTRPRS_MBER_ID)
);

CREATE INDEX COMTNENTRPRSMBER_i01 ON COMTNENTRPRSMBER (GROUP_ID  ASC);





/* 권한정보 */
CREATE TABLE COMTNAUTHORINFO (
	AUTHOR_CODE           VARCHAR2(30)  NOT NULL,
	AUTHOR_NM             VARCHAR2(60)  NOT NULL,
	AUTHOR_DC             VARCHAR2(200)  NULL,
	AUTHOR_CREAT_DE       CHAR(20)  NOT NULL,
CONSTRAINT COMTNAUTHORINFO_PK PRIMARY KEY (AUTHOR_CODE)
);






/* 롤 계층구조 */
CREATE TABLE COMTNROLES_HIERARCHY (
	PARNTS_ROLE           VARCHAR2(30)  NOT NULL,
	CHLDRN_ROLE           VARCHAR2(30)  NOT NULL,
CONSTRAINT COMTNROLES_HIERARCHY_PK PRIMARY KEY (PARNTS_ROLE,CHLDRN_ROLE)
);

CREATE UNIQUE INDEX COMTNROLES_HIERARCHY_i01 ON COMTNROLES_HIERARCHY (PARNTS_ROLE  ASC);
CREATE INDEX COMTNROLES_HIERARCHY_i02 ON COMTNROLES_HIERARCHY (CHLDRN_ROLE  ASC);





/* 롤정보 */
CREATE TABLE COMTNROLEINFO (
	ROLE_CODE             VARCHAR2(50)  NOT NULL,
	ROLE_NM               VARCHAR2(60)  NOT NULL,
	ROLE_PTTRN            VARCHAR2(300)  NULL,
	ROLE_DC               VARCHAR2(200)  NULL,
	ROLE_TY               VARCHAR2(80)  NULL,
	ROLE_SORT             VARCHAR2(10)  NULL,
	ROLE_CREAT_DE         CHAR(20)  NOT NULL,
CONSTRAINT COMTNROLEINFO_PK PRIMARY KEY (ROLE_CODE)
);






/* 권한롤관계 */
CREATE TABLE COMTNAUTHORROLERELATE (
	AUTHOR_CODE           VARCHAR2(30)  NOT NULL,
	ROLE_CODE             VARCHAR2(50)  NOT NULL,
	CREAT_DT              DATE  NULL,
CONSTRAINT COMTNAUTHORROLERELATE_PK PRIMARY KEY (AUTHOR_CODE,ROLE_CODE)
);

CREATE INDEX COMTNAUTHORROLERELATE_i01 ON COMTNAUTHORROLERELATE (AUTHOR_CODE  ASC);
CREATE INDEX COMTNAUTHORROLERELATE_i02 ON COMTNAUTHORROLERELATE (ROLE_CODE  ASC);





/* IDS */
CREATE TABLE IDS (
	IDGEN_TABLE_NM        VARCHAR2(20)  NOT NULL,
	IDGEN_NEXT_ID         NUMBER(30)  NOT NULL,
CONSTRAINT IDS_PK PRIMARY KEY (IDGEN_TABLE_NM)
);






/* 사용자정보뷰 */
CREATE VIEW COMVNUSERMASTER ( 
	ESNTL_ID, USER_ID, PASSWORD, USER_NM, USER_ZIP, USER_ADRES, USER_EMAIL, GROUP_ID, USER_SE, ORGNZT_ID
)
AS
	SELECT
		ESNTL_ID, MBER_ID, PASSWORD, MBER_NM, ZIP, ADRES, MBER_EMAIL_ADRES, ' ', 'GNR' AS USER_SE, ' ' ORGNZT_ID
	FROM COMTNGNRLMBER
	UNION ALL
	SELECT
		ESNTL_ID, EMPLYR_ID, PASSWORD, USER_NM, ZIP, HOUSE_ADRES, EMAIL_ADRES, GROUP_ID, 'USR' AS USER_SE, ORGNZT_ID
	FROM COMTNEMPLYRINFO
	UNION ALL
	SELECT
		ESNTL_ID, ENTRPRS_MBER_ID, ENTRPRS_MBER_PASSWORD, CMPNY_NM, ZIP, ADRES, APPLCNT_EMAIL_ADRES, ' ', 'ENT' AS USER_SE, ' ' ORGNZT_ID
	FROM COMTNENTRPRSMBER 
ORDER BY ESNTL_ID;




/* 휴일관리 */
CREATE TABLE COMTNRESTDE (
	RESTDE_NO             NUMBER(6)  NOT NULL,
	RESTDE                CHAR(8)  NULL,
	RESTDE_NM             VARCHAR2(60)  NULL,
	RESTDE_DC             VARCHAR2(200)  NULL,
	RESTDE_SE_CODE        VARCHAR2(2)  NULL,
	FRST_REGIST_PNTTM     DATE  NULL,
	FRST_REGISTER_ID      VARCHAR2(20)  NULL,
	LAST_UPDT_PNTTM       DATE  NULL,
	LAST_UPDUSR_ID        VARCHAR2(20)  NULL,
CONSTRAINT COMTNRESTDE_PK PRIMARY KEY (RESTDE_NO)
);






/* 우편번호 */
CREATE TABLE COMTCZIP (
	ZIP                   VARCHAR2(6)  NOT NULL,
	SN                    NUMBER(10)  NOT NULL,
	CTPRVN_NM             VARCHAR2(20)  NULL,
	SIGNGU_NM             VARCHAR2(20)  NULL,
	EMD_NM                VARCHAR2(60)  NULL,
	LI_BULD_NM            VARCHAR2(60)  NULL,
	LNBR_DONG_HO          VARCHAR2(20)  NULL,
	FRST_REGIST_PNTTM     DATE  NULL,
	FRST_REGISTER_ID      VARCHAR2(20)  NULL,
	LAST_UPDT_PNTTM       DATE  NULL,
	LAST_UPDUSR_ID        VARCHAR2(20)  NULL,
CONSTRAINT COMTCZIP_PK PRIMARY KEY (ZIP,SN)
);






/* 도로명주소 */
CREATE TABLE COMTRDNMADRZIP (
	RDMN_CODE             VARCHAR2(12) NOT NULL,
	SN                    NUMBER(10) NOT NULL,
	CTPRVN_NM             VARCHAR2(20) NULL,
	SIGNGU_NM             VARCHAR2(20) NULL,
	RDMN                  VARCHAR2(100) NULL,
	BDNBR_MNNM            VARCHAR2(5) NULL,
	BDNBR_SLNO            VARCHAR2(5) NULL,
	BULD_NM               VARCHAR2(60) NULL,
	DETAIL_BULD_NM        VARCHAR2(60) NULL,
	ZIP                   VARCHAR2(6) NOT NULL,
	FRST_REGIST_PNTTM     DATE NULL,
	FRST_REGISTER_ID      VARCHAR2(20) NULL,
	LAST_UPDT_PNTTM       DATE NULL,
	LAST_UPDUSR_ID        VARCHAR2(20) NULL,
CONSTRAINT COMTRDNMADRZIP_PK PRIMARY KEY (SN)
);






/* 바로가기 메뉴 등록 */
CREATE TABLE COMTNBKMKMENUMANAGERESULT (
	MENU_ID               NUMBER(20)  NOT NULL,
	EMPLYR_ID             VARCHAR2(20)  NOT NULL,
	MENU_NM               VARCHAR2(60)  NOT NULL,
	PROGRM_STRE_PATH      VARCHAR2(100)  NOT NULL,
CONSTRAINT COMTNBKMKMENUMANAGERESULT_PK PRIMARY KEY (MENU_ID,EMPLYR_ID)
);






/* 사용자보안설정 */
CREATE TABLE COMTNEMPLYRSCRTYESTBS (
	SCRTY_DTRMN_TRGET_ID  VARCHAR2(20)  NOT NULL,
	MBER_TY_CODE          CHAR(5)  NULL,
	AUTHOR_CODE           VARCHAR2(30)  NOT NULL 
);

CREATE INDEX COMTNEMPLYRSCRTYESTBS_i04 ON COMTNEMPLYRSCRTYESTBS (AUTHOR_CODE  ASC);





/* 사이트맵 */
CREATE TABLE COMTNSITEMAP (
	MAPNG_CREAT_ID        VARCHAR2(30)  NOT NULL,
	CREATR_ID             VARCHAR2(20)  NOT NULL,
	MAPNG_FILE_NM         VARCHAR2(60)  NOT NULL,
	MAPNG_FILE_PATH       VARCHAR2(100)  NOT NULL,
CONSTRAINT COMTNSITEMAP_PK PRIMARY KEY (MAPNG_CREAT_ID)
);






/* 프로그램목록 */
CREATE TABLE COMTNPROGRMLIST (
	PROGRM_FILE_NM        VARCHAR2(60)  NOT NULL,
	PROGRM_STRE_PATH      VARCHAR2(100)  NOT NULL,
	PROGRM_KOREAN_NM      VARCHAR2(60)  NULL,
	PROGRM_DC             VARCHAR2(200)  NULL,
	URL                   VARCHAR2(100)  NOT NULL,
CONSTRAINT COMTNPROGRMLIST_PK PRIMARY KEY (PROGRM_FILE_NM)
);






/* 프로그램변경내역 */
CREATE TABLE COMTHPROGRMCHANGEDTLS (
	PROGRM_FILE_NM        VARCHAR2(60)  NOT NULL,
	REQUST_NO             NUMBER(10)  NOT NULL,
	RQESTER_ID            VARCHAR2(20)  NOT NULL,
	CHANGE_REQUST_CN      VARCHAR2(1000)  NULL,
	REQUST_PROCESS_CN     LONG VARCHAR  NULL,
	OPETR_ID              VARCHAR2(20)  NULL,
	PROCESS_STTUS_CODE    VARCHAR2(15)  NOT NULL,
	PROCESS_DE            CHAR(20)  NULL,
	RQESTDE               CHAR(20)  NULL,
	REQUST_SJ             VARCHAR2(60)  NOT NULL,
CONSTRAINT COMTHPROGRMCHANGEDTLS_PK PRIMARY KEY (PROGRM_FILE_NM,REQUST_NO)
);

CREATE INDEX COMTHPROGRMCHANGEDTLS_i01 ON COMTHPROGRMCHANGEDTLS (PROGRM_FILE_NM ASC);





/* 메뉴정보 */
CREATE TABLE COMTNMENUINFO (
	MENU_NM               VARCHAR2(60)  NOT NULL,
	PROGRM_FILE_NM        VARCHAR2(60)  NOT NULL,
	MENU_NO               NUMBER(20)  NOT NULL,
	UPPER_MENU_NO         NUMBER(20)  NULL,
	MENU_ORDR             NUMBER(5)  NOT NULL,
	MENU_DC               VARCHAR2(250)  NULL,
	RELATE_IMAGE_PATH     VARCHAR2(100)  NULL,
	RELATE_IMAGE_NM       VARCHAR2(60)  NULL,
CONSTRAINT COMTNMENUINFO_PK PRIMARY KEY (MENU_NO)
);

CREATE INDEX COMTNMENUINFO_i02 ON COMTNMENUINFO (UPPER_MENU_NO  ASC);





/* 사이트맵 */
CREATE TABLE COMTNSITEMAP (
	MAPNG_CREAT_ID        VARCHAR2(30)  NOT NULL,
	CREATR_ID             VARCHAR2(20)  NOT NULL,
	MAPNG_FILE_NM         VARCHAR2(60)  NOT NULL,
	MAPNG_FILE_PATH       VARCHAR2(100)  NOT NULL,
CONSTRAINT COMTNSITEMAP_PK PRIMARY KEY (MAPNG_CREAT_ID)
);






/* 메뉴생성내역 */
CREATE TABLE COMTNMENUCREATDTLS (
	MENU_NO               NUMBER(20)  NOT NULL,
	AUTHOR_CODE           VARCHAR2(30)  NOT NULL,
	MAPNG_CREAT_ID        VARCHAR2(30)  NULL,
CONSTRAINT COMTNMENUCREATDTLS_PK PRIMARY KEY (MENU_NO,AUTHOR_CODE)
);

CREATE INDEX COMTNMENUCREATDTLS_i02 ON COMTNMENUCREATDTLS (MENU_NO  ASC);
CREATE INDEX COMTNMENUCREATDTLS_i03 ON COMTNMENUCREATDTLS (MAPNG_CREAT_ID  ASC);
CREATE INDEX COMTNMENUCREATDTLS_i04 ON COMTNMENUCREATDTLS (AUTHOR_CODE  ASC);





