DROP SCHEMA IF EXISTS `phoenix`;

CREATE SCHEMA phoenix;

USE phoenix;
--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` int(11) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `sno` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `birthday` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `sno_UNIQUE` (`sno`),
  KEY `user_student_fk_idx` (`user_id`),
  CONSTRAINT `user_student_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `tno` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `title` int(11) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `tno_UNIQUE` (`tno`),
  KEY `user_teacher_fk_idx` (`user_id`),
  CONSTRAINT `user_teacher_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `mno` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `mno_UNIQUE` (`mno`),
  KEY `user_manager_fk_idx` (`user_id`),
  CONSTRAINT `user_manager_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `term`
--

DROP TABLE IF EXISTS `term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` varchar(4) DEFAULT NULL,
  `semester` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idterm_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `url` varchar(256) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `teacher_course_fk_idx` (`teacher_id`),
  CONSTRAINT `teacher_course_fk` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_id` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `duration` varchar(45) DEFAULT NULL,
  `student_num` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `teacher_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `term_class_fk_idx` (`term_id`),
  KEY `course_class_fk_idx` (`course_id`),
  KEY `teacher_class_fk_idx` (`teacher_id`),
  CONSTRAINT `course_class_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `teacher_class_fk` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`),
  CONSTRAINT `term_class_fk` FOREIGN KEY (`term_id`) REFERENCES `term` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cloudware`
--

DROP TABLE IF EXISTS `cloudware`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloudware` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `web_socket` varchar(256) DEFAULT NULL,
  `service_id` varchar(45) DEFAULT NULL,
  `instance_id` varchar(45) DEFAULT NULL,
  `service_name` varchar(128) DEFAULT NULL,
  `pulsar_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_resource`
--

DROP TABLE IF EXISTS `course_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `course_course_resource_fk_idx` (`course_id`),
  KEY `resource_course_resource_fk_idx` (`resource_id`),
  CONSTRAINT `course_course_resource_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `resource_course_resource_fk` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `course_module_fk_idx` (`course_id`),
  CONSTRAINT `course_module_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module_resource`
--

DROP TABLE IF EXISTS `module_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `module_module_resource_fk_idx` (`module_id`),
  KEY `resource_module_resource_fk_idx` (`resource_id`),
  CONSTRAINT `module_module_resource_fk` FOREIGN KEY (`module_id`) REFERENCES `module` (`id`),
  CONSTRAINT `resource_module_resource_fk` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `experiment`
--

DROP TABLE IF EXISTS `experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `experiment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `cloudware_type` int(11) DEFAULT NULL,
  `publish_date` datetime DEFAULT NULL,
  `deadline_date` datetime DEFAULT NULL,
  `experiment_content` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `module_experiment_fk_idx` (`module_id`),
  CONSTRAINT `module_experiment_fk` FOREIGN KEY (`module_id`) REFERENCES `module` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `homework`
--

DROP TABLE IF EXISTS `homework`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `homework` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `cloudware_type` int(11) DEFAULT NULL,
  `publish_date` datetime DEFAULT NULL,
  `deadline_date` datetime DEFAULT NULL,
  `class_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `module_homework_fk_idx` (`module_id`),
  KEY `class_homework_fk_idx` (`class_id`),
  CONSTRAINT `class_homework_fk` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `module_homework_fk` FOREIGN KEY (`module_id`) REFERENCES `module` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `homework_resource`
--

DROP TABLE IF EXISTS `homework_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `homework_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homework_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `homework_homework_resource_fk_idx` (`homework_id`),
  KEY `resource_homework_resource_fk_idx` (`resource_id`),
  CONSTRAINT `homework_homework_resource_fk` FOREIGN KEY (`homework_id`) REFERENCES `homework` (`id`),
  CONSTRAINT `resource_homework_resource_fk` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `navlist`
--

DROP TABLE IF EXISTS `navlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `navlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(256) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `class` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_class`
--

DROP TABLE IF EXISTS `student_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `class_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `student_student_class_fk_idx` (`student_id`),
  KEY `class_student_class_fk_idx` (`class_id`),
  CONSTRAINT `class_student_class_fk` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `student_student_class_fk` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_experiment`
--

DROP TABLE IF EXISTS `student_experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_experiment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `experiment_id` int(11) DEFAULT NULL,
  `cloudware_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `student_student_experiment_fk_idx` (`student_id`),
  KEY `experiment_student_experiment_fk_idx` (`experiment_id`),
  KEY `cloudware_student_experiment_fk_idx` (`cloudware_id`),
  CONSTRAINT `cloudware_student_experiment_fk` FOREIGN KEY (`cloudware_id`) REFERENCES `cloudware` (`id`),
  CONSTRAINT `experiment_student_experiment_fk` FOREIGN KEY (`experiment_id`) REFERENCES `experiment` (`id`),
  CONSTRAINT `student_student_experiment_fk` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_homework`
--

DROP TABLE IF EXISTS `student_homework`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_homework` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `homework_id` int(11) DEFAULT NULL,
  `cloudware_id` int(11) DEFAULT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `submission_date` datetime DEFAULT NULL,
  `lastEdit_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `homework_student_unique` (`student_id`,`homework_id`),
  KEY `homework_student_homework_fk_idx` (`homework_id`),
  KEY `cloudware_student_homework_fk_idx` (`cloudware_id`),
  CONSTRAINT `cloudware_student_homework_fk` FOREIGN KEY (`cloudware_id`) REFERENCES `cloudware` (`id`),
  CONSTRAINT `homework_student_homework_fk` FOREIGN KEY (`homework_id`) REFERENCES `homework` (`id`),
  CONSTRAINT `student_student_homework_fk` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_homework_resource`
--

DROP TABLE IF EXISTS `student_homework_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_homework_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_homework_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `studentHomework_resource_fk_idx` (`student_homework_id`),
  KEY `resource_studentHomework_fk_idx` (`resource_id`),
  CONSTRAINT `resource_studentHomework_fk` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`),
  CONSTRAINT `studentHomework_resource_fk` FOREIGN KEY (`student_homework_id`) REFERENCES `student_homework` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_navlist`s
--

DROP TABLE IF EXISTS `user_navlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_navlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` int(11) DEFAULT NULL,
  `navlist_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `navlist_userNavlist_fk_idx` (`navlist_id`),
  CONSTRAINT `navlist_userNavlist_fk` FOREIGN KEY (`navlist_id`) REFERENCES `navlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dump completed on 2017-10-21  9:17:41

ALTER TABLE user ADD password2 varchar(255);

DROP TABLE IF EXISTS `user_access_record`;
CREATE TABLE `user_access_record` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `access_date` date NOT NULL COMMENT '统计日期',
  `duration` smallint(4) DEFAULT NULL COMMENT '在线时长',
  `last_time` bigint(13) DEFAULT NULL COMMENT '最后更新时间的时间戳',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态：0离线，1在线',
  `login_time` datetime DEFAULT NULL COMMENT '最近的登录时间',
  PRIMARY KEY (`access_date`,`user_id`)
);

DROP TABLE IF EXISTS `real_time_online_user`;
CREATE TABLE `real_time_online_user` (
  `online_count` int(11) DEFAULT NULL,
  `online_time_stamp` bigint(13) DEFAULT NULL,
  KEY `index_time` (`online_time_stamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `user_status`;

CREATE TABLE `user_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态:0 未确定  1：已确定',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `image_config`;
CREATE TABLE `image_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cloudware_type` int(2) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



ALTER TABLE `student` add `grade` varchar(10);
ALTER TABLE `student` add `phone` varchar(45);


ALTER TABLE course ADD folder_name varchar(15);
ALTER TABLE course ADD volume_id varchar(15);
ALTER TABLE user add volume_id varchar(15);

-- 班级和课程的关联表
create table class_course (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_id` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL,
   `term_id` int(11) DEFAULT NULL,
    `teacher_id` int(11) DEFAULT NULL,
     `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `class_class_course_fk_idx` (`class_id`),
  KEY `term_class_course_fk_idx` (`term_id`),
  KEY `course_class_course_fk_idx` (`course_id`),
  KEY `teacher_class_course_fk_idx` (`teacher_id`),
  CONSTRAINT `class_class_course_fk_idx` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `course_class_course_fk_idx` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `teacher_class_course_fk_idx` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`),
  CONSTRAINT `term_class_course_fk_idx` FOREIGN KEY (`term_id`) REFERENCES `term` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 删除班级中的字段
alter table class drop foreign key course_class_fk;
alter table class drop foreign key teacher_class_fk;
alter table class drop foreign key term_class_fk;

alter table class drop column term_id,drop column course_id,drop column teacher_id;


alter table student modify column grade varchar(45);
-- 班级和课程关联表 增加学年和学期字段
ALTER TABLE class_course add column school_year VARCHAR(10);
ALTER TABLE class_course add column semester int(1);


-- 删除班级和课程关联表 term_id 外键及column
alter table class_course drop foreign key term_class_course_fk_idx;
alter table class_course drop column term_id;

-- 删除学期表
drop table term;

-- 解除课程与老师外键关系
alter table course drop foreign key teacher_course_fk;
-- 新增作者联系方式字段
ALTER TABLE course add column author VARCHAR(50);
ALTER TABLE course add column phone VARCHAR(50);
-- 删除course表中teacher_id 字段
alter table course drop column teacher_id;

-- 章节表中添加排序id字段  1.2
ALTER TABLE module add column order_id int(4);

-- 实验表中添加顺序id字段
ALTER TABLE experiment add column order_id int(4);


CREATE TABLE `sys_license` (
  `key` varchar(255) DEFAULT NULL,
  `license` varchar(310) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0：未导入license,1：已导入license,-1：license不合法'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 实验表加字段
ALTER TABLE experiment add column is_answer int(4);
ALTER TABLE experiment add column answer_table_name VARCHAR(100);
ALTER TABLE experiment add column answer_url VARCHAR(100);
ALTER TABLE experiment add column old_answer_name VARCHAR(100);

-- 作业成绩表
CREATE TABLE `job_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_id` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL,
  `experiment_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
	`create_time` datetime DEFAULT NULL,
	`table_name` varchar(100) DEFAULT NULL,
	`file_url` varchar(255) DEFAULT NULL,
	 PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 科研项目 1.3
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT NULL,
  `bref_intro` varchar(600) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `results` longtext,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 科研文件夹
DROP TABLE IF EXISTS `t_project_doc_folder`;
CREATE TABLE `t_project_doc_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 科研文档
DROP TABLE IF EXISTS `t_project_doc`;
CREATE TABLE `t_project_doc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `folder_id` int(11) DEFAULT NULL,
  `content` longtext,
  `editor` int(11) DEFAULT NULL ,
  `creator` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `doc_name` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 科研附件
DROP TABLE IF EXISTS `t_project_doc_annex`;
CREATE TABLE `t_project_doc_annex` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doc_id` int(11) DEFAULT NULL,
  `file_name` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 科研项目成员
DROP TABLE IF EXISTS `t_project_user`;
CREATE TABLE `t_project_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `is_manager` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- token方案修改
ALTER TABLE `user` add column token_create_time bigint(13);
ALTER TABLE `user` add column is_remind tinyint(4);
ALTER TABLE `user` add column token varchar(255);

-- 科研项目数量，套餐配置表
DROP TABLE IF EXISTS `t_project_config`;
CREATE TABLE `t_project_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` tinyint(1) DEFAULT NULL,
  `package_id` int(11) DEFAULT NULL,
  `project_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE t_project MODIFY COLUMN `name` VARCHAR (45) BINARY CHARACTER
SET utf8 COLLATE utf8_bin DEFAULT NULL;
ALTER TABLE t_project_doc_folder MODIFY COLUMN `name` VARCHAR (45) BINARY CHARACTER
SET utf8 COLLATE utf8_bin DEFAULT NULL;

ALTER TABLE t_project_doc MODIFY COLUMN `doc_name` VARCHAR (45) BINARY CHARACTER
SET utf8 COLLATE utf8_bin DEFAULT NULL;
ALTER TABLE t_project_doc_annex MODIFY COLUMN `file_name` VARCHAR (80) BINARY CHARACTER
SET utf8 COLLATE utf8_bin DEFAULT NULL;


