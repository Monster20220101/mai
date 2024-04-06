/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : mai

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2024-04-06 22:59:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `address_id` bigint(20) NOT NULL COMMENT '地址ID',
  `address_region` varchar(200) DEFAULT NULL COMMENT '地址所属区域 省-市-区县乡',
  `address_detail` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `address_name` varchar(32) DEFAULT NULL COMMENT '收货人姓名',
  `address_tel` varchar(32) DEFAULT NULL COMMENT '收货人电话',
  `address_type` varchar(20) DEFAULT NULL COMMENT '类型 家/学校/公司',
  `address_isdefault` int(1) DEFAULT '0' COMMENT '是否为默认地址 0否 1是',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`address_id`),
  KEY `address_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地址表';

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `author_id` bigint(20) NOT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cmt
-- ----------------------------
DROP TABLE IF EXISTS `cmt`;
CREATE TABLE `cmt` (
  `cmt_id` bigint(20) NOT NULL,
  `cmt_score` int(1) DEFAULT NULL COMMENT '评分1~5',
  `cmt_text` varchar(500) DEFAULT NULL COMMENT '评论内容',
  `cmt_isanon` int(1) DEFAULT NULL COMMENT '用户是否选择匿名评论 0否 1是',
  `user_nickname` varchar(32) DEFAULT NULL COMMENT '用户昵称',
  `user_head` varchar(200) DEFAULT NULL COMMENT '用户头像',
  `product_id` bigint(20) DEFAULT NULL COMMENT '评论的产品id',
  `imgs_id` bigint(20) DEFAULT NULL COMMENT '用户评论返图',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cmt_id`),
  KEY `cmt_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

-- ----------------------------
-- Table structure for imgs
-- ----------------------------
DROP TABLE IF EXISTS `imgs`;
CREATE TABLE `imgs` (
  `img_id` bigint(20) NOT NULL COMMENT '图片自己的id 不重复',
  `imgs_id` bigint(20) DEFAULT NULL COMMENT '图片所属图片集id',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片路径',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片表';

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` bigint(20) NOT NULL,
  `order_price` double(11,2) DEFAULT NULL,
  `order_status` int(1) DEFAULT '0' COMMENT '0未发货 1已发货 2已签收 3已评论',
  `order_address` varchar(200) DEFAULT NULL COMMENT '该订单对应地址信息',
  `user_id` varchar(200) DEFAULT NULL,
  `store_name` varchar(32) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product_name` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  KEY `order_user_id` (`user_id`),
  KEY `order_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` bigint(20) NOT NULL,
  `product_flag` int(1) DEFAULT '0' COMMENT '是否下架 0否 1是',
  `product_name` varchar(200) DEFAULT NULL,
  `product_price` double(11,2) DEFAULT NULL,
  `product_sales` int(11) DEFAULT '0' COMMENT '销量',
  `product_intro` varchar(4000) DEFAULT NULL COMMENT '简介',
  `product_type` varchar(200) DEFAULT NULL COMMENT '类型',
  `imgs_id` bigint(20) DEFAULT NULL COMMENT '图片',
  `store_name` varchar(32) DEFAULT NULL COMMENT '所属商家',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Table structure for product_author
-- ----------------------------
DROP TABLE IF EXISTS `product_author`;
CREATE TABLE `product_author` (
  `product_id` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(200) NOT NULL COMMENT '手机号 不重复',
  `user_flag` int(1) DEFAULT '0' COMMENT '是否封禁 0否 1是',
  `user_nickname` varchar(32) DEFAULT '' COMMENT '昵称 不重复',
  `user_password` varchar(32) NOT NULL,
  `user_email` varchar(200) DEFAULT NULL,
  `user_head` varchar(200) DEFAULT NULL,
  `user_status` int(1) DEFAULT '0' COMMENT '0用户 1申请商家中 2商家 3管理员',
  `user_isvip` int(1) DEFAULT '0' COMMENT '是否vip 0否 1是',
  `user_intro` varchar(500) DEFAULT '' COMMENT '用户自我介绍',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
