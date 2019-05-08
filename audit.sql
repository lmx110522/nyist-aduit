/*
SQLyog Ultimate v11.13 (64 bit)
MySQL - 5.7.23 : Database - audit
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`audit` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `audit`;

/*Table structure for table `authority` */

DROP TABLE IF EXISTS `authority`;

CREATE TABLE `authority` (
  `id` varchar(255) NOT NULL,
  `aname` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sorter` int(100) DEFAULT NULL,
  `is_ok` int(10) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `private_parent` (`parent_id`) USING BTREE,
  CONSTRAINT `authority_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `authority` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `authority` */

insert  into `authority`(`id`,`aname`,`url`,`sorter`,`is_ok`,`parent_id`) values ('	8aa6f4efb49aef2d','审核动态','#',7,1,NULL),('11eaea2445c8a050','账号管理','#',2,1,NULL),('1d1aca693d0ecd9f','系院账号管理','/accounts/xy',2,1,'11eaea2445c8a050'),('2663465a16290c3c','专业建设','/jys/jysUploadUI/2663465a16290c3c',3,1,'4a2294af2013b7e5'),('4a2294af2013b7e5','教研室上传','#',5,1,NULL),('4a3a67053ba09054','教学研究与改革','/jys/jysUploadUI/4a3a67053ba09054',6,1,'4a2294af2013b7e5'),('54645c86197b668a','教学效果','/jys/jysUploadUI/54645c86197b668a',2,1,'4a2294af2013b7e5'),('584fe6707c72407e','实践教学','/jys/jysUploadUI/584fe6707c72407e',5,1,'4a2294af2013b7e5'),('59edbdbda57502b1','申请记录','/jys/jysAllMsgUI',6,1,NULL),('635d196cfab67cd4','教研室排行榜','/jys/rankListUI',8,1,NULL),('654e9b1747372e31','教研室账号管理','/accounts/jys',1,1,'11eaea2445c8a050'),('6737bca4dca0ac1e','待我审核','/shr/enterTaskList',1,1,'	8aa6f4efb49aef2d'),('6a8ddfcd1dd41b3d','制度建设','/jys/jysUploadUI/6a8ddfcd1dd41b3d',1,1,'4a2294af2013b7e5'),('7b6abf0f369bc742','课程教材建设','/jys/jysUploadUI/7b6abf0f369bc742',4,1,'4a2294af2013b7e5'),('8007a9dd2781499e','条件保障','/jys/jysUploadUI/8007a9dd2781499e',8,1,'4a2294af2013b7e5'),('98ed5068dfa21a25','个人信息','/index',1,1,NULL),('a0206d7f27d26ef4','审核人模块分配','/accounts/shrDistribution',3,1,NULL),('afa49b7820be93e5','教师教学发展','/jys/jysUploadUI/afa49b7820be93e5',7,1,'4a2294af2013b7e5'),('b85d32246fa6cad5','审核人账号管理','/accounts/shr',3,1,'11eaea2445c8a050'),('e185e55990ee9134','教研室查看','/jys/jysViewUI',4,1,NULL),('f44f82c0e71a61a0','教学档案管理','/jys/jysUploadUI/f44f82c0e71a61a0',9,1,'4a2294af2013b7e5'),('fe7cc930186acaab','审核记录','/shr/recordUI',2,1,'	8aa6f4efb49aef2d');

/*Table structure for table `document` */

DROP TABLE IF EXISTS `document`;

CREATE TABLE `document` (
  `id` varchar(255) NOT NULL,
  `dname` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `grouping` int(10) DEFAULT NULL,
  `mid` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_ok` int(10) DEFAULT NULL COMMENT '是否删除',
  `s_uid` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`uid`) USING BTREE,
  KEY `size_id` (`mid`) USING BTREE,
  KEY `s_uid` (`s_uid`) USING BTREE,
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `module` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `document_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `document_ibfk_3` FOREIGN KEY (`s_uid`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `document` */

insert  into `document`(`id`,`dname`,`file_url`,`img_url`,`grouping`,`mid`,`uid`,`update_time`,`is_ok`,`s_uid`,`year`) values ('154285580266425','2018年软件学院家庭经济困难学生认定办法.doc','http://120.79.179.175:81/gis教研室/专业建设/2018/file/专业建设_gis教研室_2018年软件学院家庭经济困难学生认定办法.doc',NULL,1,'2663465a16290c3c','4','2018-11-22 11:03:23',1,'153708681261459',2018),('154285593602524','2016级大作业.doc','http://120.79.179.175:81/gis教研室/专业建设/2018/file/专业建设_gis教研室_2016级大作业.doc',NULL,1,'2663465a16290c3c','4','2018-11-22 11:05:36',1,'153708681261459',2018),('154285645246093','214467-106.jpg',NULL,'http://120.79.179.175:81/gis教研室/教学档案管理/2018/img/教学档案管理_gis教研室_214467-106.jpg',1,'f44f82c0e71a61a0','4','2018-11-22 11:14:12',1,'153691995900714',2018);

/*Table structure for table `module` */

DROP TABLE IF EXISTS `module`;

CREATE TABLE `module` (
  `id` varchar(255) NOT NULL,
  `mname` varchar(255) DEFAULT NULL,
  `content` text,
  `uid` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_uid` (`uid`) USING BTREE,
  CONSTRAINT `module_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `module` */

insert  into `module`(`id`,`mname`,`content`,`uid`,`score`) values ('2663465a16290c3c','专业建设','开展专业相关产业和领域人才需求分析， 研究制定或落实专业建设规划； 定期修\r\n订人才培养方案， 达到国家专业类教学质量标准以上； 建设有校级及以上特色专业或\r\n综合改革试点专业， 专业评估优秀， 积极开展专业认证； 无专业建设任务的教研室组\r\n织有效保障和支持专业建设。',NULL,10),('4a3a67053ba09054','教学研究与改革','重视教学改革研究与实践， 主持完成 1 项校级及以上教改项目， 在核心期刊发表\r\n高水平教改论文； 每 2 周一次教学研讨与交流活动， 每学年人均 8 次以上相互听课或\r\n教学观摩， 获得有校级以上教学竞赛奖； 2 年内每位教师至少参加 1 次国内外教学研\r\n讨会议， 及时了解教学改革领域的最新动态。',NULL,10),('54645c86197b668a','教学效果','根据人才培养方案和教学计划， 组织落实好教学环节各项任务， 运行有序， 档案\r\n资料齐全； 课堂教学规范， 教学纪律严格， 近三年内无教学事故； 教学效果满意度高，\r\n每学年开展有教学评价和教学质量分析， 教师评教整体在良好以上， 无不合格。',NULL,10),('584fe6707c72407e','实践教学','科学制定实践教学方案， 规范设置实践教学环节； 科教协同、 产教融合、 校企合\r\n作紧密， 具有满足教学需要的校内实验实训中心和校外实践教学基地； 推进创新创业\r\n教育改革.',NULL,10),('6a8ddfcd1dd41b3d','制度建设','学院有加强教研室建设的实施办法和相关制度； 教研室的设置， 满足专业或课程\r\n教学需要， 涵盖全部任课教师； 基层教学组织的职责和任务、 负责人条件、 权限和待\r\n遇、 考核激励机制明确； 基层教学组织的教学管理、 教研室活动制度[包括教研室会议\r\n制度(每 2 周 1 次，须有专人记录)、集体备课制度(每学期不少于 3 次，须有专人记录)、\r\n新进教师培训制度、 新开课试讲制度、 听课与观摩制度(每人每年不少于 8 次以上)、\r\n教学检查制度]青年教师导师、 外聘教师管理等制度健全。',NULL,15),('7b6abf0f369bc742','课程教材建设','建立符合专业发展的课程体系， 有规范的课程建设规划、 教学大纲和课程标准，\r\n课程内容及时更新； 建设有校级及以上在线开放课程； 选用或编写高质量教材和指导\r\n用书， 信息化教学资源丰富， 有课程教学内容优化、 教学方法和考核评价方式改革等\r\n方面的典型案例。',NULL,10),('8007a9dd2781499e','条件保障','基层教学组织职责分工明确， 内部制度健全， 有明确的发展目标和年度计划； 负\r\n责人具有博士学位或副教授职称， 教学管理经验丰富； 办公场所和相关办公设施达标，\r\n人均办公面积在 3 平方米以上， 所在院部设立有教研室工作专项经费， 支持教研室日\r\n常工作开展.',NULL,10),('afa49b7820be93e5','教师教学发展','坚持立德树人， 师德师风良好， 无违反《高等学校教师职业道德规范》 现象； 每\r\n学年教授为本专科生上课率达 100%； 加强教学梯队建设， 团队年龄、 职称、 学缘结构\r\n合理， 有校级以上教学名师、 教学新秀； 推进教学工作的传帮带， 新任教师经专门培\r\n训、 试讲合格后上岗并配有指导教师， 每学年选派有青年骨干教师 3 个月以上的进修\r\n访学。',NULL,15),('f44f82c0e71a61a0','教学档案管理','教研室要对教学档案进行分类整理， 集体保管， 包括教师个人资料、 教学资料、\r\n教研室资料等。 教师个人资料包括个人简介、 所承担课程资料、 科研课题资料、 论文\r\n及参编书样、 各类获奖证书等。 教学资料包括教案、 讲稿、 教学日历、 考试分析、 课\r\n程教学总结、 教学反馈报告等。 教研室资料包括教研室相关活动文字记录、 图片、 视\r\n频等。 严格履行档案管理的规定， 不仅建立纸质档案， 更要实现现代的信息化管理，\r\n档案要录入计算机， 便于查询， 提高工作效率。',NULL,10);

/*Table structure for table `role_authority` */

DROP TABLE IF EXISTS `role_authority`;

CREATE TABLE `role_authority` (
  `id` int(11) NOT NULL,
  `role` int(10) DEFAULT NULL COMMENT '职能',
  `aid` varchar(255) DEFAULT NULL,
  `is_ok` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `authority_id` (`aid`) USING BTREE,
  CONSTRAINT `role_authority_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `authority` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `role_authority` */

insert  into `role_authority`(`id`,`role`,`aid`,`is_ok`) values (1,0,'54645c86197b668a',1),(2,0,'584fe6707c72407e',1),(3,0,'6737bca4dca0ac1e',1),(4,0,'8007a9dd2781499e',1),(5,0,'	8aa6f4efb49aef2d',1),(6,0,'98ed5068dfa21a25',1),(7,0,'59edbdbda57502b1',1),(8,0,'11eaea2445c8a050',1),(9,0,'1d1aca693d0ecd9f',1),(10,0,'2663465a16290c3c',1),(11,0,'4a2294af2013b7e5',1),(12,0,'4a3a67053ba09054',1),(13,0,'635d196cfab67cd4',1),(14,0,'654e9b1747372e31',1),(16,0,'6a8ddfcd1dd41b3d',1),(17,0,'7b6abf0f369bc742',1),(18,0,'a0206d7f27d26ef4',1),(19,0,'afa49b7820be93e5',1),(20,0,'b85d32246fa6cad5',1),(21,0,'e185e55990ee9134',1),(22,0,'f44f82c0e71a61a0',1),(23,0,'59edbdbda57502b1',1),(24,2,'98ed5068dfa21a25',1),(25,2,'e185e55990ee9134',1),(26,0,'fe7cc930186acaab',1),(27,3,'6737bca4dca0ac1e',1),(28,3,'98ed5068dfa21a25',1),(29,3,'	8aa6f4efb49aef2d',1),(30,3,'fe7cc930186acaab',1),(31,4,'2663465a16290c3c',1),(32,4,'4a2294af2013b7e5',1),(33,4,'4a3a67053ba09054',1),(34,4,'54645c86197b668a',1),(35,4,'584fe6707c72407e',1),(36,4,'59edbdbda57502b1',1),(37,4,'635d196cfab67cd4',1),(38,4,'6a8ddfcd1dd41b3d',1),(39,4,'7b6abf0f369bc742',1),(40,4,'8007a9dd2781499e',1),(41,4,'98ed5068dfa21a25',1),(42,4,'afa49b7820be93e5',1),(43,4,'f44f82c0e71a61a0',1),(44,1,'11eaea2445c8a050',1),(45,1,'1d1aca693d0ecd9f',1),(47,1,'635d196cfab67cd4',1),(48,1,'654e9b1747372e31',1),(49,1,'98ed5068dfa21a25',1),(50,1,'a0206d7f27d26ef4',1),(51,1,'b85d32246fa6cad5',1),(52,1,'e185e55990ee9134',1),(53,2,'635d196cfab67cd4',1),(54,3,'635d196cfab67cd4',1),(55,1,'	8aa6f4efb49aef2d',1),(56,1,'fe7cc930186acaab',1);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` int(10) DEFAULT NULL COMMENT '0为超级管理员，1为教务处室主任，2为系院，3为审核员，4为教研室',
  `grouping` int(10) DEFAULT NULL,
  `mid` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `is_ok` int(10) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL COMMENT '所属教研室',
  `usernumber` varchar(100) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mid` (`mid`) USING BTREE,
  KEY `parentid` (`parent_id`) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `module` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`role`,`grouping`,`mid`,`phone`,`is_ok`,`parent_id`,`usernumber`) values ('1','李沫熙','110522',0,NULL,NULL,'13193801071',4,NULL,'lmx'),('153691995884957','审核人66','123456',3,1,'54645c86197b668a',NULL,1,NULL,'shr3'),('153691995891851','审核人4','123456',3,1,'584fe6707c72407e',NULL,1,NULL,'shr4'),('153691995893843','审核人5','123456',3,1,'6a8ddfcd1dd41b3d',NULL,1,NULL,'shr5'),('153691995895505','审核人6','123456',3,1,'7b6abf0f369bc742',NULL,1,NULL,'shr6'),('153691995897180','审核人7','123456',3,1,'8007a9dd2781499e',NULL,1,NULL,'shr7'),('153691995899085','审核人8','123456',3,1,'afa49b7820be93e5',NULL,1,NULL,'shr8'),('153691995900714','审核人9','123456',3,1,'f44f82c0e71a61a0',NULL,1,NULL,'shr9'),('153691995902330','审核人10','123456',3,1,'2663465a16290c3c',NULL,1,NULL,'shri'),('153691995904571','审核人11','123456',3,1,'2663465a16290c3c',NULL,1,NULL,'shrt'),('153708615624443','网络工程教研室','123456',4,1,NULL,NULL,1,'3','rj-wlgc'),('153708619779118','计算机学院','123456',2,1,NULL,NULL,1,NULL,'jsjxy'),('153708641233065','数字媒体教研室','123456',4,1,NULL,NULL,1,'153708619779118','rj-szmt'),('153708681261459','张','123456',3,1,'2663465a16290c3c',NULL,1,NULL,'111'),('153708683736470','王','123456',3,2,'2663465a16290c3c',NULL,1,NULL,'222'),('153708683743327','李','123456',3,2,'4a3a67053ba09054',NULL,1,NULL,'333'),('153708705172430','物联网工程','wlwgcjys',4,1,NULL,NULL,1,'153708619779118','wlw'),('153708933654609','sd','123456',3,1,'4a3a67053ba09054',NULL,1,NULL,'sda'),('153787667954482','111','123456',4,1,NULL,NULL,0,'153708619779118','11'),('153787670400423','jjkl','123456',4,1,NULL,NULL,0,'153708619779118','kjkjk'),('153787672946717','kl','123456',4,1,NULL,NULL,0,'153708619779118','kl'),('153787672980128','jk','123456',4,1,NULL,NULL,0,'153708619779118','jkj'),('153818831527826','音乐学院','123456',2,2,NULL,NULL,1,NULL,'yyxy'),('153818912934163','乐器','111111',4,2,NULL,NULL,1,'153818831527826','yyxy-yq'),('153818991779283','李军','123456',3,2,'54645c86197b668a',NULL,1,NULL,'666'),('153820546070882','张仲景国医国药学院','123456',2,1,NULL,NULL,1,NULL,'zzj'),('154289271308171','艺术学院','123456',2,1,NULL,NULL,1,NULL,'yishu'),('2','教导主任','222222',1,NULL,NULL,'111',4,NULL,'zhuren'),('3','软件学院','123456',2,1,NULL,'111',1,NULL,'rjxy'),('4','gis教研室','123456',4,1,NULL,'111',2,'3','gis'),('5','java教研室','123456',4,1,NULL,'111',1,'3','java'),('6','艺术学院','123456',2,1,NULL,'222',0,NULL,'ysxy'),('7','芭蕾舞社团','111111',4,2,NULL,'222',0,'6','blw'),('8','小提琴社团','123456',4,2,NULL,'222',0,'6','xtq'),('9','审核人2','123456',3,1,'4a3a67053ba09054','6786',0,NULL,'shr2');

/*Table structure for table `user_module` */

DROP TABLE IF EXISTS `user_module`;

CREATE TABLE `user_module` (
  `id` varchar(255) NOT NULL,
  `uid` varchar(255) DEFAULT NULL COMMENT '审核人id',
  `mid` varchar(255) NOT NULL COMMENT '对应模块',
  `content` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `is_ok` int(10) DEFAULT NULL COMMENT '0代表未审核，1代表审核中，2代表审核结束',
  `tuid` varchar(255) DEFAULT NULL COMMENT '对应上传的教研室',
  `year` int(20) DEFAULT NULL COMMENT '年份',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `persion_id` (`uid`) USING BTREE,
  KEY `model_id` (`mid`) USING BTREE,
  KEY `user_module_ibfk_3` (`tuid`) USING BTREE,
  CONSTRAINT `user_module_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_module_ibfk_2` FOREIGN KEY (`mid`) REFERENCES `module` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_module_ibfk_3` FOREIGN KEY (`tuid`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user_module` */

insert  into `user_module`(`id`,`uid`,`mid`,`content`,`score`,`is_ok`,`tuid`,`year`) values ('154286015378665','4','2663465a16290c3c',NULL,0,0,'153708681261459',2018),('154286015387036','4','f44f82c0e71a61a0','加油~',9,0,'153691995900714',2018);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
