--新建database(MS)
DROP DATABASE IF EXISTS `MS`;
CREATE DATABASE `MS` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `MS`;

--用户表，信息包含id(主键自增长)，用户名，密码，电话，邮箱，地址，
--身份(管理员m，普通用户c,超级管理员s),是否登陆(登陆t,未登陆f)
drop table IF EXISTS person;
CREATE TABLE `person` (
  `id` int(5) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `password` varchar(6) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `email` varchar(30) NOT NULL,
  `address` varchar(30) default NULL,
  `head` varchar(50) default NULL,
  `isLogin` char(1) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

--后台设置超级管理员的信息
INSERT INTO PERSON (NAME,PASSWORD,PHONE,EMAIL,ADDRESS,ISLOGIN) 
VALUES('abc','123456','12345678901','abc@qq.com','china','f');



--角色表
DROP TABLE IF EXISTS ROLE;
CREATE TABLE ROLE(
	ID int(5) NOT NULL auto_increment,
	USER_ID int(5) NOT NULL COMMENT '用户id',
	ROLE_NAME VARCHAR(30) NOT NULL COMMENT '角色名',
	PRIMARY KEY  (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色信息表';

INSERT INTO ROLE (USER_ID,ROLE_NAME) VALUES (1,'master');


--角色菜单关联表
DROP TABLE IF EXISTS ROLE_MENU_REL;
CREATE TABLE ROLE_MENU_REL(
	ROLE_NAME VARCHAR(30) NOT NULL COMMENT '角色名',
	MENU_ID VARCHAR(5) NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';
--添加联合主键，同一角色不能添加同一个菜单
ALTER TABLE ROLE_MENU_REL ADD CONSTRAINT PK_ROLE_MENU_REL_ROLEID_MENUID PRIMARY KEY(ROLE_NAME,MENU_ID); 

--给超级管理员初始化分配管理菜单的项目，添加菜单，分配菜单
insert into role_menu_rel values('master','1');
insert into role_menu_rel values('master','2');
insert into role_menu_rel values('master','3');
insert into role_menu_rel values('master','4');

--用户菜单关联表
DROP TABLE IF EXISTS USER_MENU_REL;
CREATE TABLE USER_MENU_REL(
	USER_ID VARCHAR(5) NOT NULL COMMENT '用户ID',
	MENU_ID VARCHAR(5) NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户菜单关联表';
--添加联合主键，同一用户不能添加同一个菜单
ALTER TABLE USER_MENU_REL ADD CONSTRAINT PK_USER_MENU_REL_USERID_MENUID PRIMARY KEY(USER_ID,MENU_ID); 
--ALTER TABLE USER_MENU_REL DROP PRIMARY KEY; 


--菜单表，初始化给master管理菜单的权限
DROP TABLE IF EXISTS MENU_INFO;
CREATE TABLE MENU_INFO(
  ID int(5) NOT NULL auto_increment,
  MENU varchar(30) NOT NULL,
  URL varchar(100) default NULL,
  HIGHERMENU varchar(30) default NULL,
  PRIMARY KEY  (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='菜单表'; 

--插入菜单信息
INSERT INTO MENU_INFO (MENU,URL,HIGHERMENU) VALUES('菜单管理',null,null);
INSERT INTO MENU_INFO (MENU,URL,HIGHERMENU) VALUES('添加菜单','http://localhost:8088/MS/html/menu/addMenu.html','1');
INSERT INTO MENU_INFO (MENU,URL,HIGHERMENU) VALUES('分配菜单权限','http://localhost:8088/MS/html/menu/addUserRoleMenu.html','1');
INSERT INTO MENU_INFO (MENU,URL,HIGHERMENU) VALUES('查看所有菜单','http://localhost:8088/MS/html/menu/listMenu.html','1');

--登陆后超级管理员可以添加如下菜单
--用户管理
--查看用户信息 http://localhost:8088/MS/person/listPerson?page=1 
--添加用户 http://localhost:8088/MS/html/regist.html

--物料管理
--申请采购 http://localhost:8088/MS/html/account/apply.html
--查看申请明细 http://localhost:8088/MS/html/account/listApply.html

--任务管理
--新增任务 http://localhost:8088/MS/html/task/allocatTask.html
--查看任务明细 http://localhost:8088/MS/html/task/listTask.html

--操作记录
--查看所有记录 http://localhost:8088/MS/html/process/listProcess.html











--新建任务分配表
--任务进行状态1-未开始，2-进行中，3-已完成
DROP TABLE IF EXISTS TASK;
CREATE TABLE TASK(
  `id` int(5) NOT NULL auto_increment,
  `userId` varchar(5) NOT NULL comment '需要完成任务的对象',
  `content` TEXT default NULL comment '任务详情',
  `startTime` varchar(20) default NULL comment '任务开始时间',
  `endTime` varchar(20) default NULL comment '任务结束时间',
  `goods` varchar(50) default NULL comment '采购物品',
  `amount` double(10,2) default null comment '购买数量',
  `unit` varchar(10) default null comment '单位',
  `state` char(1) default '1' comment '任务进行状态',
  `memo` varchar(100) default NULL comment '备注说明',
  PRIMARY KEY  (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务表';



--新建物料申请表
--状态，1-发送申请，2-审批通过，3-购买完成，4-申请驳回
DROP TABLE IF EXISTS APPLY_INFO;
CREATE TABLE APPLY_INFO(
  `id` int(5) NOT NULL auto_increment,
  `userName` varchar(30) NOT NULL comment '申请人',
  `goods` varchar(50) default NULL comment '购买物品',
  `amount` double(10,2) default 1 comment '购买数量',
  `unit` varchar(10) default '个' comment '单位',
  `price` double(10,2) default NULL comment '单价',
  `total` double(10,2) default NULL comment '总价',
  `usefor` varchar(200) default NULL comment '用途',
  `askdate` varchar(20) default NULL comment '申请日期',
  `state` varchar(20) default '1' comment '状态',
  `memo` varchar(100) default NULL comment '备注说明',
  PRIMARY KEY  (`id`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='物料申请表';
