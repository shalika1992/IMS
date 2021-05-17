create database ims;

create table statuscategory (
  code varchar(16) not null,
  description varchar(64) not null,
  primary key (code)
)

insert into statuscategory(code,description) values('USCT','User Category');
insert into statuscategory(code,description) values('SMCT','Sample Category');
insert into statuscategory(code,description) values('RPCT','Report Category');

create table status (
  code varchar(16) not null,
  description varchar(64) not null,
  statuscategory varchar(16) not null,
  primary key (code),
  foreign key (statuscategory) references statuscategory(code)
)

insert into status(code,description,statuscategory) values('ACT','Active','USCT');
insert into status(code,description,statuscategory) values('DEACT','De-Active','USCT');
insert into status(code,description,statuscategory) values('NEW','New','USCT');
insert into status(code,description,statuscategory) values('RES','Reset','USCT');
insert into status(code,description,statuscategory) values('CHA','Changed','USCT');
insert into status(code,description,statuscategory) values('EXP','Expired','USCT');

insert into status(code,description,statuscategory) values('RECV','Recived','SMCT');
insert into status(code,description,statuscategory) values('VALD','Validated','SMCT');
insert into status(code,description,statuscategory) values('INVALD','Invalid','SMCT');
insert into status(code,description,statuscategory) values('PLASG','Plate Assigned','SMCT');
insert into status(code,description,statuscategory) values('REPR','Reported','SMCT');
insert into status(code,description,statuscategory) values('REPE','Repeated','SMCT');
insert into status(code,description,statuscategory) values('COMP','Completed','SMCT');


SET SQL_MODE='ALLOW_INVALID_DATES';

create table userrole(
  userrolecode varchar(16) not null,
  description varchar(64) not null,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (userrolecode),
  foreign key (status) references status(code)
)

insert into userrole(userrolecode,description,status,createduser,createdtime,lastupdateduser) values('SCOF','Speciment Counter Officer','ACT','admin',now(),'admin');
insert into userrole(userrolecode,description,status,createduser,createdtime,lastupdateduser) values('MDOF','Medical Officer','ACT','admin',now(),'admin');
insert into userrole(userrolecode,description,status,createduser,createdtime,lastupdateduser) values('ADMIN','Admin User','ACT','admin',now(),'admin');

create table web_section(
  sectioncode varchar(16) not null,
  description varchar(64) not null,
  sortkey int not null,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (sectioncode),
  foreign key (status) references status(code)
)

insert into web_section(sectioncode,description,sortkey,status,createduser,createdtime,lastupdateduser) values('FLMG','File Management',1,'ACT','admin',now(),'admin');
insert into web_section(sectioncode,description,sortkey,status,createduser,createdtime,lastupdateduser) values('PRMG','File Generation',2,'ACT','admin',now(),'admin');
insert into web_section(sectioncode,description,sortkey,status,createduser,createdtime,lastupdateduser) values('RPEP','Report Explorer',3,'ACT','admin',now(),'admin');
insert into web_section(sectioncode,description,sortkey,status,createduser,createdtime,lastupdateduser) values('SYCF','System Configuration',4,'ACT','admin',now(),'admin');

create table web_page(
  pagecode varchar(16) not null,
  description varchar(64) not null, 
  url varchar(128) not null,
  sortkey int not null,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (pagecode),
  foreign key (status) references status(code)
)


insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('SFUP','Sample File Upload','sampleFileUpload.htm',1,'ACT','admin',now(),'admin');
insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('SDVF','Sample Data Verification','dataVerification.htm',2,'ACT','admin',now(),'admin');


insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('PLAS','Plate Assign','viewPlateAssign.htm',3,'ACT','admin',now(),'admin');
insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('RPSL','Repeated Samples','repeatDetail.htm',4,'ACT','admin',now(),'admin');
insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('RSUP','Result Update','resultUpdate.htm',5,'ACT','admin',now(),'admin');

insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('RPGN','Report Generation','reportGeneration.htm',6,'ACT','admin',now(),'admin');
insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('USMT','User Management','userMgt.htm',7,'ACT','admin',now(),'admin');
insert into web_page(pagecode,description,url,sortkey,status,createduser,createdtime,lastupdateduser) values('ISMT','Institution Management','institutionMgt.htm',7,'ACT','admin',now(),'admin');

create table web_sectionpage(
  userrole varchar(16) not null,
  section varchar(16) not null,
  page varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (userrole,section,page),
  foreign key (userrole) references userrole(userrolecode),
  foreign key (section) references web_section(sectioncode),
  foreign key (page) references web_page(pagecode)
)

insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('SCOF','FLMG','SFUP','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','PRMG','SDVF','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','PRMG','PLAS','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','PRMG','RPSL','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','PRMG','RSUP','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','RPEP','RPGN','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','SYCF','USMT','admin',now(),'admin');
insert into web_sectionpage(userrole,section,page,createduser,createdtime,lastupdateduser) values('MDOF','SYCF','ISMT','admin',now(),'admin');

create table web_systemuser(
  username varchar(16) not null,
  password varchar(512) not null,
  userrole varchar(16) not null,
  expirydate date not null,
  fullname varchar(512) not null,
  email varchar(128) not null,
  mobile varchar(10) not null,
  noofinvalidattempt int not null default 0,
  lastloggeddate timestamp not null,
  initialloginstatus int default 0,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (username),
  foreign key (userrole) references userrole(userrolecode),
  foreign key (status) references status(code)
)

insert into web_systemuser(username,password,userrole,expirydate,fullname,email,mobile,lastloggeddate,status,createduser,createdtime,lastupdateduser) 
values('cofficer','ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb','SCOF','2025-12-31','Counter Officer','cofficer@gmail.com','0777123456',now(),'ACT','admin',now(),'admin');

insert into web_systemuser(username,password,userrole,expirydate,fullname,email,mobile,lastloggeddate,status,createduser,createdtime,lastupdateduser) 
values('mofficer','ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb','MDOF','2025-12-31','Medical Officer','mofficer@gmail.com','077712345',now(),'ACT','admin',now(),'admin');

create table institution(
  institutioncode varchar(16) not null,
  name varchar(256) not null,
  address varchar(512) not null,
  contactno varchar(16) not null,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (institutioncode),
  foreign key (status) references status(code)
)

create table excelblock(
  code varchar(8) not null,
  skip tinyint(1) not null default 0 COMMENT '0 - not skip , 1 - skip',
  primary key (code)
)

insert into excelblock(code) values('A1');
insert into excelblock(code) values('A2');
insert into excelblock(code) values('A3');
insert into excelblock(code) values('A4');
insert into excelblock(code) values('A5');
insert into excelblock(code) values('A6');
insert into excelblock(code) values('A7');
insert into excelblock(code) values('A8');
insert into excelblock(code) values('A9');
insert into excelblock(code) values('A10');
insert into excelblock(code) values('A11');
insert into excelblock(code) values('A12');
insert into excelblock(code) values('B1');
insert into excelblock(code) values('B2');
insert into excelblock(code) values('B3');
insert into excelblock(code) values('B4');
insert into excelblock(code) values('B5');
insert into excelblock(code) values('B6');
insert into excelblock(code) values('B7');
insert into excelblock(code) values('B8');
insert into excelblock(code) values('B9');
insert into excelblock(code) values('B10');
insert into excelblock(code) values('B11');
insert into excelblock(code) values('B12');
insert into excelblock(code) values('C1');
insert into excelblock(code) values('C2');
insert into excelblock(code,skip) values('C3',1);
insert into excelblock(code) values('C4');
insert into excelblock(code) values('C5');
insert into excelblock(code) values('C6');
insert into excelblock(code) values('C7');
insert into excelblock(code) values('C8');
insert into excelblock(code) values('C9');
insert into excelblock(code) values('C10');
insert into excelblock(code) values('C11');
insert into excelblock(code) values('C12');
insert into excelblock(code) values('D1');
insert into excelblock(code) values('D2');
insert into excelblock(code) values('D3');
insert into excelblock(code) values('D4');
insert into excelblock(code) values('D5');
insert into excelblock(code) values('D6');
insert into excelblock(code) values('D7');
insert into excelblock(code) values('D8');
insert into excelblock(code) values('D9');
insert into excelblock(code) values('D10');
insert into excelblock(code) values('D11');
insert into excelblock(code) values('D12');
insert into excelblock(code) values('E1');
insert into excelblock(code) values('E2');
insert into excelblock(code) values('E3');
insert into excelblock(code) values('E4');
insert into excelblock(code) values('E5');
insert into excelblock(code) values('E6');
insert into excelblock(code) values('E7');
insert into excelblock(code) values('E8');
insert into excelblock(code) values('E9');
insert into excelblock(code) values('E10');
insert into excelblock(code) values('E11');
insert into excelblock(code) values('E12');
insert into excelblock(code) values('F1');
insert into excelblock(code) values('F2');
insert into excelblock(code) values('F3');
insert into excelblock(code) values('F4');
insert into excelblock(code) values('F5');
insert into excelblock(code) values('F6');
insert into excelblock(code) values('F7');
insert into excelblock(code) values('F8');
insert into excelblock(code) values('F9');
insert into excelblock(code) values('F10');
insert into excelblock(code) values('F11');
insert into excelblock(code) values('F12');
insert into excelblock(code) values('G1');
insert into excelblock(code) values('G2');
insert into excelblock(code) values('G3');
insert into excelblock(code) values('G4');
insert into excelblock(code) values('G5');
insert into excelblock(code) values('G6');
insert into excelblock(code) values('G7');
insert into excelblock(code) values('G8');
insert into excelblock(code) values('G9');
insert into excelblock(code) values('G10');
insert into excelblock(code) values('G11');
insert into excelblock(code,skip) values('G12',1);
insert into excelblock(code) values('H1');
insert into excelblock(code) values('H2');
insert into excelblock(code) values('H3');
insert into excelblock(code) values('H4');
insert into excelblock(code) values('H5');
insert into excelblock(code) values('H6');
insert into excelblock(code) values('H7');
insert into excelblock(code) values('H8');
insert into excelblock(code) values('H9');
insert into excelblock(code) values('H10');
insert into excelblock(code) values('H11');
insert into excelblock(code,skip) values('H12',1);

SET SQL_MODE='ALLOW_INVALID_DATES';

create table sample_data(
  id int not null auto_increment,
  referenceno varchar(64),
  institutioncode varchar(16),
  name varchar(256),
  age varchar(8),
  gender varchar(1),
  symptomatic varchar(128),
  contacttype varchar(64),
  nic varchar(16),
  address varchar(512),
  district varchar(128),
  contactno varchar(16),
  secondarycontactno varchar(16),
  specimenid varchar(128),
  barcode varchar(512),
  receiveddate date,
  status varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  primary key (id),
  foreign key (institutioncode) references institution(institutioncode),
  foreign key (status) references status(code)
)

create table result(
  code varchar(16) not null,
  description varchar(64) not null,
  primary key (code)
)

insert into result(code,description) values('DTCD','Detected');
insert into result(code,description) values('NDTD','Not Detected');
insert into result(code,description) values('PEND','Pending');

create table plate(
  id int not null auto_increment,
  code varchar(8) not null,
  createddate date not null,
  primary key (id),
  unique key platecode(code , createddate)
)

create table pool(
  id int not null auto_increment,
  code varchar(8) not null,
  createddate date not null,
  primary key (id),
  unique key poolcode(code , createddate)
)

create table master_temp_data(
  id int not null auto_increment,
  institutioncode varchar(16) not null,
  name varchar(256) not null,
  age varchar(8) not null,
  gender varchar(1) not null,
  nic varchar(16) not null,
  contactno varchar(16) not null,
  serialno varchar(128) not null,
  specimenid varchar(128),
  barcode varchar(512),
  receiveddate date not null,
  status varchar(16) not null,
  plateid int not null,
  blockvalue varchar(8) not null,
  ispool tinyint(1) not null default 0 COMMENT '0 - not pool , 1 - pool',
  poolid int ,
  result varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  primary key (id),
  foreign key (institutioncode) references institution(institutioncode),
  foreign key (status) references status(code),
  foreign key (plateid) references plate(id),
  foreign key (blockvalue) references excelblock(code),
  foreign key (result) references result(code)
)

create table master_data(
  id int not null auto_increment,
  institutioncode varchar(16) not null,
  name varchar(256) not null,
  age varchar(8) not null,
  gender varchar(1) not null,
  nic varchar(16) not null,
  contactno varchar(16) not null,
  serialno varchar(128) not null,
  specimenid varchar(128),
  barcode varchar(512),
  receiveddate date not null,
  status varchar(16) not null,
  plateid int not null,
  blockvalue varchar(8) not null,
  ispool tinyint(1) not null default 0 COMMENT '0 - not pool , 1 - pool',
  poolid int ,
  result varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  primary key (id),
  foreign key (institutioncode) references institution(institutioncode),
  foreign key (status) references status(code),
  foreign key (plateid) references plate(id),
  foreign key (blockvalue) references excelblock(code),
  foreign key (result) references result(code)
)

SET SQL_MODE='ALLOW_INVALID_DATES';

create table passwordparam(
  passwordparam varchar(16) not null,
  description varchar(128) not null,
  value varchar(16) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (passwordparam)
)

insert into passwordparam(passwordparam,description,value,createduser,createdtime,lastupdateduser) values('PASEXP','Password expiry period','3','admin',now(),'admin');
insert into passwordparam(passwordparam,description,value,createduser,createdtime,lastupdateduser) values('INLATT','No of invalid login attempts','3','admin',now(),'admin');
insert into passwordparam(passwordparam,description,value,createduser,createdtime,lastupdateduser) values('IDLEXP','Idle account expiry period','5','admin',now(),'admin');
insert into passwordparam(passwordparam,description,value,createduser,createdtime,lastupdateduser) values('NOHPWD','No of history password','2','admin',now(),'admin');
insert into passwordparam(passwordparam,description,value,createduser,createdtime,lastupdateduser) values('PWEXNP','Password expiry notification period','10','admin',now(),'admin');


create table web_passwordpolicy(
  passwordpolicyid int not null,
  minimumlength int not null,
  maximumlength int not null,
  minimumspecialcharacters int not null,
  minimumuppercasecharacters int not null,
  minimumnumericalcharacters int not null,
  minimumlowercasecharacters int not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  lastupdateduser varchar(64) not null,
  lastupdatedtime timestamp not null default current_timestamp on update current_timestamp,
  primary key (passwordpolicyid)
)

insert into web_passwordpolicy(passwordpolicyid,minimumlength,maximumlength,minimumspecialcharacters,minimumuppercasecharacters,minimumnumericalcharacters,minimumlowercasecharacters,createduser,createdtime,lastupdateduser) 
values(1,1,7,0,1,0,0,'admin',now(),'admin');

create table district(
  code varchar(16) not null,
  description varchar(128) not null,
  primary key (code)
)

insert into district(code,description) values('Jaffna','Jaffna');
insert into district(code,description) values('Kilinochchi','Kilinochchi');
insert into district(code,description) values('Mannar','Mannar');
insert into district(code,description) values('Mullaitivu','Mullaitivu');
insert into district(code,description) values('Vavuniya','Vavuniya');
insert into district(code,description) values('Puttalam','Puttalam');
insert into district(code,description) values('Kurunegala','Kurunegala');
insert into district(code,description) values('Gampaha','Gampaha');
insert into district(code,description) values('Colombo','Colombo');
insert into district(code,description) values('Kalutara','Kalutara');
insert into district(code,description) values('Anuradhapura','Anuradhapura');
insert into district(code,description) values('Polonnaruwa','Polonnaruwa');
insert into district(code,description) values('Matale','Matale');
insert into district(code,description) values('Kandy','Kandy');
insert into district(code,description) values('Nuwara Eliya','Nuwara Eliya');
insert into district(code,description) values('Kegalle','Kegalle');
insert into district(code,description) values('Ratnapura','Ratnapura');
insert into district(code,description) values('Trincomalee','Trincomalee');
insert into district(code,description) values('Batticaloa','Batticaloa');
insert into district(code,description) values('Ampara','Ampara');
insert into district(code,description) values('Badulla','Badulla');
insert into district(code,description) values('Monaragala','Monaragala');
insert into district(code,description) values('Hambantota','Hambantota');
insert into district(code,description) values('Matara','Matara');
insert into district(code,description) values('Galle','Galle');

create table reject_data(
  id int not null auto_increment,
  sampleid int not null,
  referenceno varchar(64),
  institutioncode varchar(16),
  name varchar(256),
  age varchar(8),
  gender varchar(1),
  symptomatic varchar(128),
  contacttype varchar(64),
  nic varchar(16),
  address varchar(512),
  district varchar(128),
  contactno varchar(16),
  secondarycontactno varchar(16),
  receiveddate date,
  status varchar(16) not null,
  remark varchar(512) not null,
  createduser varchar(64) not null,
  createdtime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  primary key (id),
  foreign key (sampleid) references sample_data(id),
  foreign key (institutioncode) references institution(institutioncode),
  foreign key (status) references status(code)
)