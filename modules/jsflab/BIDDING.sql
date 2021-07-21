
CREATE SCHEMA BIDDING;
create table BIDDING.SIGNUP(
emailID varchar(20) PRIMARY KEY,
first_name varchar(20),
last_name varchar(20),
Address varchar(50),
City varchar(20),
State varchar(20),
Company_name varchar(30),
Designation varchar(20),
created_date date,
password varchar(50),
user_name varchar(50))
ALTER TABLE BIDDING.SIGNUP ADD COLUMN verified_user varchar(1);
;
select * from BIDDING.SIGNUP;

commit;
GRANT INSERT ON BIDDING.SIGNUP  TO PUBLIC;
GRANT SELECT ON BIDDING.SIGNUP  TO PUBLIC;
ALTER TABLE BIDDING.SIGNUP ADD COLUMN contact_num varchar(13);
ALTER TABLE BIDDING.SIGNUP ADD COLUMN emailID varchar(60);
GRANT UPDATE ON BIDDING.SIGNUP  TO PUBLIC;

select * from BIDDING.SIGNUP;
desc

ALTER TABLE SIG.SIGNUP ADD COLUMN created_date date;
ALTER TABLE SIG.SIGNUP ADD COLUMN password varchar(50);

ALTER TABLE BIDDING.SIGNUP
DROP COLUMN emailID ;
alter table BIDDING.SIGNUP modify emailID varchar(60);
commit;
select * from SIG.SIGNUP;
CREATE SEQUENCE order_entry_id
AS Int
START WITH 1;

CREATE SEQUENCE SI_MAIN_TAB
AS INT
START WITH 1;

CREATE SEQUENCE SI_SECOND_TAB
AS Int
START WITH 1;

CREATE SEQUENCE SI_THIRD_TAB
AS Int
START WITH 1;

CREATE SEQUENCE BIDDING.BID_REFERENCE
AS Int
START WITH 1;
GRANT USAGE ON SEQUENCE BIDDING.BID_REFERENCE  TO PUBLIC;

create table SIG.SI_MAIN_TAB(
id int PRIMARY KEY,
name varchar(50),
description varchar(150))
;
create table BIDDING.Bidding_User(
user_name varchar(50) PRIMARY KEY,
service_tax varchar(50),
office_address varchar(100),
designation varchar(50),
varified varchar(1),
trust_score int);

create table SIG.SI_SECOND_TAB(
s_id int PRIMARY KEY,
name varchar(50),
description varchar(150),
id int )
;

create table SIG.SI_THIRD_TAB(
t_id int PRIMARY KEY,
name varchar(50),
description varchar(150),
id int,
s_id int)
;

create table SIG.SI_CATEGORY(
gname varchar(50),
description varchar(500),
manufacturer varchar(50),
id int ,
s_id int ,
t_id int,
parent_name varchar(50))
;
ALTER TABLE SIG.SI_CATEGORY ADD COLUMN hierarchy varchar(1000);
// Granting sequence access'
GRANT USAGE ON SEQUENCE [ schemaName. ] SQL92Identifier TO grantees;

GRANT INSERT ON SIG.SI_MAIN_TAB  TO PUBLIC;
GRANT SELECT ON SIG.SI_MAIN_TAB  TO PUBLIC;
<--Important command to set authorization values-->
CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(
    'derby.database.sqlAuthorization', 'true');
comit;
CREATE SEQUENCE SIG.SI_MAIN_TAB
AS INT
START WITH 1;
CREATE SEQUENCE SIG.SI_SECOND_TAB
AS INT
START WITH 1;
GRANT INSERT ON SIG.SI_SECOND_TAB  TO PUBLIC;
GRANT SELECT ON SIG.SI_SECOND_TAB  TO PUBLIC;

CREATE SEQUENCE SIG.SI_THIRD_TAB
AS INT
START WITH 1;
GRANT INSERT ON SIG.SI_THIRD_TAB  TO PUBLIC;
GRANT SELECT ON SIG.SI_THIRD_TAB  TO PUBLIC;
select SIG.SI_MAIN_TAB.nextval from SIG;
 select SEQUENCE SIG.SI_MAIN_TAB;
    
 SELECT CURRENTVALUE FROM SYS.SYSSEQUENCES WHERE SEQUENCENAME='SIG.SI_MAIN_TAB';
 
SELECT Next Value FOR SI_MAIN_TAB ;
 VALUES NEXT VALUE FOR SIG.SI_MAIN_TAB;
GRANT USAGE ON SEQUENCE SIG.SI_MAIN_TAB  TO APP;
GRANT USAGE ON SEQUENCE SIG.SI_SECOND_TAB  TO APP;
GRANT USAGE ON SEQUENCE SIG.SI_THIRD_TAB  TO APP;
GRANT SELECT ON Table SIG.SI_CATEGORY  TO APP;
GRANT INSERT ON Table SIG.SI_CATEGORY  TO APP;

<-- User 'APP' does not have USAGE permission on SEQUENCE 'SIG'.'SI_MAIN_TAB'-->

ALTER TABLE SIG.SI_CATEGORY ADD COLUMN next varchar(5);
create table BIDDING.PRODUCT(
product_id varchar(200) ,
product_image_path varchar(200) ,
manufacturer_image_path varchar(200),
basic_information varchar(1500),
product_description varchar(1000),
product_detail_overview varchar(3000),
product_manufacturer_id varchar(100),
product_manufacturer_name varchar(100),
variant_id varchar(100),
keyword varchar(1500),
hierarchy varchar(300),
rating int,
price int,
availability varchar(1),
number_sold int,
no_times_searched int);
GRANT INSERT ON Table BIDDING.PRODUCT  TO APP;
GRANT SELECT ON BIDDING.PRODUCT  TO PUBLIC;
GRANT INSERT ON Table BIDDING.PRODUCT  TO APP;

create table BIDDING.Bidding_approval_system(
Bid_reference int,
user_name varchar(50),
user_approved varchar(1),
comments varchar(500)
);
GRANT INSERT ON Table BIDDING.Bidding_approval_system  TO APP;
GRANT SELECT ON Table BIDDING.Bidding_approval_system  TO PUBLIC;
GRANT INSERT ON Table BIDDING.Bidding_approval_system  TO APP;


create table BIDDING.BiddingHistory(
bid_reference int ,
price int,
bid_by varchar(50));
GRANT INSERT ON Table BIDDING.BiddingHistory  TO APP;
GRANT SELECT ON Table BIDDING.BiddingHistory  TO PUBLIC;
GRANT INSERT ON Table BIDDING.BiddingHistory  TO APP;

create table BIDDING.Bidding(
bid_reference int ,
starting_date date ,
end_date date,
product_name varchar(100),
itemdescription varchar(500),
status varchar(80),
condition varchar(80),
posted_by varchar(80),
location varchar(100),
product_manufacturer_name varchar(100),
warranty varchar(3),
product_id varchar(250),
quantity int,
images_associated varchar(300),
start_time double,
end_time double);

drop table BIDDING.Bidding;
GRANT INSERT ON Table BIDDING.BIDDING  TO PUBLIC;
GRANT SELECT ON BIDDING.BIDDING  TO PUBLIC;
GRANT UPDATE ON BIDDING.BIDDING  TO PUBLIC;

GRANT INSERT ON Table BIDDING.BIDDING_USER  TO PUBLIC;
GRANT SELECT ON BIDDING.BIDDING_USER  TO PUBLIC;
GRANT UPDATE ON BIDDING.BIDDING_USER  TO PUBLIC;

create table BIDDING>

GRANT INSERT ON Table SIG.PRODUCT  TO APP;
GRANT SELECT ON SIG.PRODUCT  TO PUBLIC;
GRANT INSERT ON Table SIG.SIGNUP  TO APP;

Select count(*) from BIDDING.SIGNUP where user_name ='gamerboy1001@gmail.com';
select * from Bidding.SIGNUP;
Select * from BIDDING.Bidding_approval_system where user_name = 'jaimataDi' and bid_reference = 13224234;

select * from hire_me_usr_reg;

**********************invoice change start
select * from invoice_owner_registration;
select * from invoice_bill_invoice;
select * from invoice_customer_registration;
select * from invoice_surcharges;
select * from invoice_bill_overview;
truncate table invoice_customer_registration;
truncate table invoice_owner_registration;

ALTER TABLE invoice_bill_overview ADD COLUMN total_overall_cost int;
rollback
commit;

CREATE TABLE sample.DB_ERP_INTEGRATER
   (ID INT, 
	VENDOR_NAME VARCHAR(100), 
	PO_AMOUNT VARCHAR(100), 
	PO_NO VARCHAR(256)
   );
insert into sample.DB_ERP_INTEGRATER (  values (1,"A", "500","1");

INSERT INTO table_name (ID, VENDOR_NAME, PO_AMOUNT, PO_NO)
VALUES (1, "A", "500", "1");


commit;
