create database wms_db
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

use wms_db;

# 创建数据表

# 创建供应商信息表
create table wms_supplier
(
	SUPPLIER_ID int not null auto_increment,
    SUPPLIER_NAME varchar(30) not null,
    SUPPLIER_PERSON varchar(10) not null,
    SUPPLIER_TEL varchar(20) not null,
    SUPPLIER_EMAIL varchar(20) not null,
    SUPPLIER_ADDRESS varchar(30) not null,
    primary key(SUPPLIER_ID)
)engine=innodb;

# 创建客户信息表
create table wms_customer
(
	CUSTOMER_ID int not null auto_increment,
    CUSTOMER_NAME varchar(30) not null,
    CUSTOMER_PERSON varchar(10) not null,
    CUSTOMER_TEL varchar(20) not null,
    CUSTOMER_EMAIL varchar(20) not null,
    CUSTOMER_ADDRESS varchar(30) not null,
    primary key(CUSTOMER_ID)
 )engine=innodb;
 
 # 创建货物信息表
 create table wms_goods
 (
	GOOD_ID int not null auto_increment,
    GOOD_NAME varChar(30) not null,
    GOOD_RYPE varchar(20),
    GOOD_SIZE varchar(20),
    GOOD_VALUE double not null,
    primary key(GOOD_ID)
 )engine=innodb;
 
 # 创建仓库信息表
 create table wms_respository
 (
	REPO_ID int not null auto_increment,
    REPO_ADDRESS varchar(30) not null,
    REPO_STATUS varchar(20) not null,
    REPO_AREA varchar(20) not null,
    REPO_DESC varchar(50),
    primary key(REPO_ID)
 )engine=innodb;
 
 # 创建仓库管理员信息表
 create table wms_repo_admin
 (
	REPO_ADMIN_ID int not null auto_increment,
    REPO_ADMIN_NAME varchar(10) not null,
    REPO_ADMIN_SEX varchar(10) not null,
    REPO_ADMIN_TEL varchar(20) not null,
    REPO_ADMIN_ADDRESS varchar(30) not null,
    REPO_ADMIN_BIRTH datetime not null,
    REPO_ADMIN_REPOID int,
    primary key(REPO_ADMIN_ID),
    foreign key (REPO_ADMIN_REPOID) references wms_respository(REPO_ID)
)engine=innodb;

# 创建入库记录表
create table wms_record_in
(
	RECORD_ID int not null auto_increment,
    RECORD_SUPPLIERID int not null,
    RECORD_GOODID int not null,
    RECORD_NUMBER int not null,
    RECORD_TIME datetime not null,
    RECORD_PERSON varchar(10) not null,
    RECORD_REPOSITORYID int not null,
    primary key(RECORD_ID),
    foreign key(RECORD_SUPPLIERID) references wms_supplier(SUPPLIER_ID),
    foreign key(RECORD_GOODID) references wms_goods(GOOD_ID),
    foreign key(RECORD_REPOSITORYID) references wms_respository(REPO_ID)
)engine=innodb;

# 创建出库记录表
create table wms_record_out
(
	RECORD_ID int not null auto_increment,
    RECORD_CUSTOMERID int not null,
    RECORD_GOODID int not null,
    RECORD_NUMBER int not null,
    RECORD_TIME datetime not null,
    RECORD_PERSON varchar(10) not null,
    RECORD_REPOSITORYID int not null,
    primary key(RECORD_ID),
    foreign key(RECORD_CUSTOMERID) references wms_customer(CUSTOMER_ID),
    foreign key(RECORD_GOODID) references wms_goods(GOOD_ID),
    foreign key(RECORD_REPOSITORYID) references wms_respository(REPO_ID)
)engine=innodb;

# 创建库存记录表
create table wms_record_storage
(
	RECORD_GOODID int not null auto_increment,
    RECORD_REPOSITORY int not null,
    RECORD_NUMBER int not null,
    primary key(RECORD_GOODID),
    foreign key (RECORD_GOODID) references wms_goods(GOOD_ID),
    foreign key (RECORD_REPOSITORY) references wms_respository(REPO_ID)
)engine=innodb;

# 创建系统用户信息表
create table wms_user
(
	USER_ID int not null auto_increment,
    USER_USERNAME varchar(30) not null,
    USER_PASSWORD varchar(40) not null,
    primary key (USER_ID)
)engine=innodb;

# 创建用户角色表
create table wms_roles
(
	ROLE_ID int not null auto_increment,
    ROLE_NAME varchar(20) not null,
    ROLE_DESC varchar(30),
    ROLE_URL_PREFIX varchar(20) not null,
    primary key(ROLE_ID)
)engine=innodb;

# 创建URL权限表
create table wms_action
(
	ACTION_ID int not null auto_increment,
    ACTION_NAME varchar(30) not null,
    ACTION_DESC varchar(30),
    ACTION_PARAM varchar(50) not null,
    primary key(ACTION_ID)
)engine=innodb;

# 用户 - 角色关联表
create table wms_user_role
(
	ROLE_ID int not null,
    USER_ID int not null,
    primary key(ROLE_ID,USER_ID),
    foreign key(ROLE_ID) references wms_roles(ROLE_ID),
    foreign key(USER_ID) references wms_user(USER_ID)
)engine=innodb;

# 角色 - URL权限关联表
create table wms_role_action
(
	ACTION_ID int not null,
    ROLE_ID int not null,
    primary key(ACTION_ID,ROLE_ID),
    foreign key(ROLE_ID) references wms_roles(ROLE_ID),
    foreign key(ACTION_ID) references wms_action(ACTION_ID)
)engine=innodb;

# 系统登入登出记录表
create table wms_access_record
(
	RECORD_ID int auto_increment primary key,
    USER_ID int not null,
    USER_NAME varchar(50) not null,
    ACCESS_TYPE varchar(30) not null,
    ACCESS_TIME datetime not null,
    ACCESS_IP varchar(45) not null
);

# 用户系统操作记录表
create table wms_operation_record
(
	RECORD_ID int auto_increment primary key,
    USER_ID int not null,
    USER_NAME varchar(50) not null,
    OPERATION_NAME varchar(30) not null,
    OPERATION_TIME datetime not null,
    OPERATION_RESULT varchar(15) not null
);


# 导入数据

# 导入系统用户信息
INSERT INTO `wms_user` VALUES (1001,'admin','6f5379e73c1a9eac6163ab8eaec7e41c'),(1018,'王皓','50f202f4862360e55635b0a9616ded13'),(1019,'李富荣','c4b3af5a5ab3e3d5aac4c5a5436201b8');

# 导入系统角色信息
INSERT INTO `wms_roles` VALUES (1,'systemAdmin',NULL,'systemAdmin'),(2,'commonsAdmin',NULL,'commonsAdmin');

# 导入 系统用户 - 角色 信息
INSERT INTO `wms_user_role` VALUES (1,1001),(2,1018),(2,1019);

# 导入URL权限信息
INSERT INTO `wms_action` VALUES (1,'addSupplier',NULL,'/supplierManage/addSupplier'),(2,'deleteSupplier',NULL,'/supplierManage/deleteSupplier'),(3,'updateSupplier',NULL,'/supplierManage/updateSupplier'),(4,'selectSupplier',NULL,'/supplierManage/getSupplierList'),(5,'getSupplierInfo',NULL,'/supplierManage/getSupplierInfo'),(6,'importSupplier',NULL,'/supplierManage/importSupplier'),(7,'exportSupplier',NULL,'/supplierManage/exportSupplier'),(8,'selectCustomer',NULL,'/customerManage/getCustomerList'),(9,'addCustomer',NULL,'/customerManage/addCustomer'),(10,'getCustomerInfo',NULL,'/customerManage/getCustomerInfo'),(11,'updateCustomer',NULL,'/customerManage/updateCustomer'),(12,'deleteCustomer',NULL,'/customerManage/deleteCustomer'),(13,'importCustomer',NULL,'/customerManage/importCustomer'),(14,'exportCustomer',NULL,'/customerManage/exportCustomer'),(15,'selectGoods',NULL,'/goodsManage/getGoodsList'),(16,'addGoods',NULL,'/goodsManage/addGoods'),(17,'getGoodsInfo',NULL,'/goodsManage/getGoodsInfo'),(18,'updateGoods',NULL,'/goodsManage/updateGoods'),(19,'deleteGoods',NULL,'/goodsManage/deleteGoods'),(20,'importGoods',NULL,'/goodsManage/importGoods'),(21,'exportGoods',NULL,'/goodsManage/exportGoods'),(22,'selectRepository',NULL,'/repositoryManage/getRepositoryList'),(23,'addRepository',NULL,'/repositoryManage/addRepository'),(24,'getRepositoryInfo',NULL,'/repositoryManage/getRepository'),(25,'updateRepository',NULL,'/repositoryManage/updateRepository'),(26,'deleteRepository',NULL,'/repositoryManage/deleteRepository'),(27,'importRepository',NULL,'/repositoryManage/importRepository'),(28,'exportRepository',NULL,'/repositoryManage/exportRepository'),(29,'selectRepositoryAdmin',NULL,'/repositoryAdminManage/getRepositoryAdminList'),(30,'addRepositoryAdmin',NULL,'/repositoryAdminManage/addRepositoryAdmin'),(31,'getRepositoryAdminInfo',NULL,'/repositoryAdminManage/getRepositoryAdminInfo'),(32,'updateRepositoryAdmin',NULL,'/repositoryAdminManage/updateRepositoryAdmin'),(33,'deleteRepositoryAdmin',NULL,'/repositoryAdminManage/deleteRepositoryAdmin'),(34,'importRepositoryAd,om',NULL,'/repositoryAdminManage/importRepositoryAdmin'),(35,'exportRepository',NULL,'/repositoryAdminManage/exportRepositoryAdmin'),(36,'getUnassignRepository',NULL,'/repositoryManage/getUnassignRepository'),(37,'getStorageListWithRepository',NULL,'/storageManage/getStorageListWithRepository'),(38,'getStorageList',NULL,'/storageManage/getStorageList'),(39,'addStorageRecord',NULL,'/storageManage/addStorageRecord'),(40,'updateStorageRecord',NULL,'/storageManage/updateStorageRecord'),(41,'deleteStorageRecord',NULL,'/storageManage/deleteStorageRecord'),(42,'importStorageRecord',NULL,'/storageManage/importStorageRecord'),(43,'exportStorageRecord',NULL,'/storageManage/exportStorageRecord'),(44,' stockIn',NULL,'/stockRecordManage/stockIn'),(45,'stockOut',NULL,'/stockRecordManage/stockOut'),(46,'searchStockRecord',NULL,'/stockRecordManage/searchStockRecord'),(47,'getAccessRecords',NULL,'/systemLog/getAccessRecords'),(48,'selectUserOperationRecords',null,'/systemLog/selectUserOperationRecords');

# 导入 角色 - URL权限 信息
INSERT INTO `wms_role_action` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(39,1),(40,1),(41,1),(42,1),(43,1),(44,1),(45,1),(4,2),(38,2),(43,2),(46,1),(47,1),(48,1);

# 导入供应商信息
INSERT INTO `wms_supplier` VALUES (1013,'浙江奇同电器有限公司','王泽伟','13777771126','86827868@126.com','中国 浙江 温州市龙湾区 龙湾区永强大道1648号'),(1014,'醴陵春天陶瓷实业有限公司','温仙容','13974167256','23267999@126.com','中国 湖南 醴陵市 东正街15号'),(1015,'洛阳嘉吉利饮品有限公司','郑绮云','26391678','22390898@qq.com','中国 广东 佛山市顺德区 北滘镇怡和路2号怡和中心14楼');

# 导入客户信息
INSERT INTO `wms_customer` VALUES (1214,'醴陵荣旗瓷业有限公司','陈娟','17716786888','23369888@136.com','中国 湖南 醴陵市 嘉树乡玉茶村柏树组'),(1215,'深圳市松林达电子有限公司','刘明','85263335-820','85264958@126.com','中国 广东 深圳市宝安区 深圳市宝安区福永社区桥头村桥塘路育'),(1216,'郑州绿之源饮品有限公司 ','赵志敬','87094196','87092165@qq.com','中国 河南 郑州市 郑州市嘉亿东方大厦609');

# 导入货物信息
INSERT INTO `wms_goods` VALUES (103,'五孔插座西门子墙壁开关','电器','86*86',1.85),(104,'陶瓷马克杯','陶瓷','9*9*11',3.5),(105,'精酿苹果醋','饮料','312ml',300);

# 导入仓库信息
INSERT INTO `wms_respository` VALUES (1003,'北京顺义南彩工业园区彩祥西路9号','可用','11000㎡','提供服务完整'),(1004,'广州白云石井石潭路大基围工业区','可用','1000㎡','物流极为便利'),(1005,' 香港北区文锦渡路（红桥新村）','可用','5000.00㎡',NULL);

# 导入仓库管理员信息
INSERT INTO `wms_repo_admin` VALUES (1018,'王皓','女','12345874526','中国佛山','2016-12-09 00:00:00',1004),(1019,'李富荣','男','1234','广州','2016-12-07 00:00:00',1003);

# 导入入库记录
INSERT INTO `wms_record_in` VALUES (15,1015,105,1000,'2016-12-31 00:00:00','admin',1004),(16,1015,105,200,'2017-01-02 00:00:00','admin',1004);

# 导入出库记录
INSERT INTO `wms_record_out` VALUES (7,1214,104,750,'2016-12-31 00:00:00','admin',1003);

# 导入库存信息
INSERT INTO `wms_record_storage` VALUES (103,1005,10000),(104,1003,1750),(105,1004,2000);