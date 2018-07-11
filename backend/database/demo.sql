
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` varchar(32) NOT NULL COMMENT '项目ID',
  `name` varchar(64) DEFAULT NULL COMMENT '项目名称',
  `svn_url` varchar(512) DEFAULT NULL COMMENT '项目文档的svn地址',
  `round` varchar(10) DEFAULT NULL COMMENT '测试周期的第几轮',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `finish_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_test_report`;
CREATE TABLE `t_test_report` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `package` varchar(64) DEFAULT NULL COMMENT '用例包名称',
  `module` varchar(64) DEFAULT NULL COMMENT '模块名称',
  `executor` varchar(10) DEFAULT NULL COMMENT '执行人',
  `priorities` varchar(16) DEFAULT NULL COMMENT '优先级列表以逗号分隔',
  `total` INT DEFAULT NULL COMMENT '用例总数',
  `pass` INT  DEFAULT NULL COMMENT '通过数',
  `fail` INT  DEFAULT NULL COMMENT '失败数',
  `finish_rate` DOUBLE DEFAULT NULL COMMENT '完成率',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

