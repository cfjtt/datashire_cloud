USE phoenix;

SET FOREIGN_KEY_CHECKS=0;
truncate table user;
truncate table student;
truncate table teacher;
truncate table manager;
truncate table course;
truncate table resource;
truncate table course_resource;
truncate table term;
truncate table module;
truncate table module_resource;
truncate table class;
truncate table experiment;
truncate table homework;
truncate table resource;
truncate table homework_resource;
truncate table cloudware;
truncate table student_experiment;
truncate table student_class;
truncate table student_homework;
truncate table student_homework_resource;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO user (id, role, username, password,password2) VALUES (8, '3', 'manager', '$2a$10$uAJTvFgzFfVN6SNZlwprU.wy1U.LoEQc0OUwpXtiReEmqp3tb4rhK','eurlanda');    -- 8

insert into manager (id, user_id, mno, name, gender, email, phone) values (1, 8, 'manager', '上帝', 1, 'abc@gmail.com', '1234');

alter table user AUTO_INCREMENT=10000;

alter table course AUTO_INCREMENT=20000;

INSERT INTO `image_config` VALUES ('1', '2', 'docker:192.168.137.215:5000/deeplabs/jupyter:v4.1');
INSERT INTO `image_config` VALUES ('2', '1', 'docker:192.168.137.215:5000/deeplabs/rstudio:v4.0');
INSERT INTO `image_config` VALUES ('3', '3', 'docker:192.168.137.215:5000/cloudwarelabs/base:v2.0');
INSERT INTO `image_config` VALUES ('4', '4', 'docker:192.168.137.215:5000/deeplabs/hadoop:v4.0');
INSERT INTO `image_config` VALUES ('5', '5', 'docker:192.168.137.215:5000/deeplabs/jupyter:v4.1');
INSERT INTO `image_config` VALUES ('6', '6', 'docker:192.168.137.215:5000/cloudwarelabs/base:v2.0');

-- 科研项目 1.3
INSERT INTO `t_project_config` VALUES ('1', '1', '3', '5');
INSERT INTO `t_project_config` VALUES ('2', '2', '3', '20');