/*
 Navicat Premium Data Transfer

 Source Server         : 10.10.26.212
 Source Server Type    : MySQL
 Source Server Version : 50741
 Source Host           : 10.10.26.212:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50741
 File Encoding         : 65001

 Date: 03/04/2024 11:00:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (14, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '[]', 2, '127.0.0.1', '2024-04-01 16:11:48');
INSERT INTO `sys_log` VALUES (15, 'admin', '新增管理员', 'com.sandy.fw.admin.controller.UserController.save()', '[{\"createBy\":1,\"createTime\":1711959144539,\"email\":\"test1@qq.com\",\"id\":6,\"nickName\":\"test1\",\"password\":\"{bcrypt}$2a$10$1GB1sf7tUhD.77pwaoOPqug0mOwm.t4l6Gpa3xb5T0vVvQI3cx8he\",\"phone\":\"12312312312\",\"roleIdList\":[1],\"status\":1,\"userName\":\"test1\"}]', 294, '127.0.0.1', '2024-04-01 16:12:25');
INSERT INTO `sys_log` VALUES (16, 'admin', '删除管理员', 'com.sandy.fw.admin.controller.UserController.delete()', '[[6]]', 91, '127.0.0.1', '2024-04-01 16:12:42');
INSERT INTO `sys_log` VALUES (17, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 5, '127.0.0.1', '2024-04-01 16:15:04');
INSERT INTO `sys_log` VALUES (18, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 2, '127.0.0.1', '2024-04-01 16:51:00');
INSERT INTO `sys_log` VALUES (19, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":10,\"menuName\":\"修改\",\"orderNum\":0,\"parentId\":8,\"path\":\"\",\"perms\":\"sys:menu:update\",\"type\":2}]', 32, '127.0.0.1', '2024-04-01 17:28:26');
INSERT INTO `sys_log` VALUES (20, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":11,\"menuName\":\"删除\",\"orderNum\":0,\"parentId\":8,\"path\":\"\",\"perms\":\"sys:menu:delete\",\"type\":2}]', 19, '127.0.0.1', '2024-04-01 17:29:11');
INSERT INTO `sys_log` VALUES (21, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 4, '127.0.0.1', '2024-04-01 17:29:18');
INSERT INTO `sys_log` VALUES (22, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"icon-notice\",\"id\":12,\"menuName\":\"操作日志\",\"orderNum\":0,\"parentId\":1,\"path\":\"sys/log\",\"perms\":\"sys:log:page\",\"type\":1}]', 59, '127.0.0.1', '2024-04-01 17:37:49');
INSERT INTO `sys_log` VALUES (23, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 3, '127.0.0.1', '2024-04-01 17:38:01');
INSERT INTO `sys_log` VALUES (24, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"icon-xiangqufill\",\"id\":13,\"menuName\":\"测试目录\",\"orderNum\":0,\"parentId\":0,\"path\":\"\",\"perms\":\"\",\"type\":0}]', 21, '127.0.0.1', '2024-04-01 18:04:12');
INSERT INTO `sys_log` VALUES (25, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"icon-role\",\"id\":14,\"menuName\":\"测试菜单\",\"orderNum\":0,\"parentId\":13,\"path\":\"/test\",\"perms\":\"test\",\"type\":1}]', 18, '127.0.0.1', '2024-04-01 18:04:40');
INSERT INTO `sys_log` VALUES (26, 'admin', '删除菜单', 'com.sandy.fw.admin.controller.MenuController.delete()', '[13]', 10, '127.0.0.1', '2024-04-01 18:04:48');
INSERT INTO `sys_log` VALUES (27, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":15,\"menuName\":\"测试权限\",\"orderNum\":0,\"parentId\":14,\"path\":\"\",\"perms\":\"test\",\"type\":2}]', 23, '127.0.0.1', '2024-04-01 18:05:09');
INSERT INTO `sys_log` VALUES (28, 'admin', '删除菜单', 'com.sandy.fw.admin.controller.MenuController.delete()', '[15]', 43, '127.0.0.1', '2024-04-01 18:05:15');
INSERT INTO `sys_log` VALUES (29, 'admin', '删除菜单', 'com.sandy.fw.admin.controller.MenuController.delete()', '[14]', 33, '127.0.0.1', '2024-04-01 18:05:23');
INSERT INTO `sys_log` VALUES (30, 'admin', '删除菜单', 'com.sandy.fw.admin.controller.MenuController.delete()', '[13]', 33, '127.0.0.1', '2024-04-01 18:05:27');
INSERT INTO `sys_log` VALUES (31, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"icon-role\",\"id\":17,\"menuName\":\"角色管理\",\"orderNum\":0,\"parentId\":1,\"path\":\"\",\"perms\":\"\",\"type\":0}]', 199, '127.0.0.1', '2024-04-02 11:49:48');
INSERT INTO `sys_log` VALUES (32, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 5, '127.0.0.1', '2024-04-02 11:50:17');
INSERT INTO `sys_log` VALUES (33, 'admin', '修改菜单', 'com.sandy.fw.admin.controller.MenuController.update()', '[{\"icon\":\"icon-role\",\"id\":17,\"menuName\":\"角色管理\",\"orderNum\":0,\"parentId\":1,\"path\":\"sys/role\",\"perms\":\"\",\"type\":1}]', 45, '127.0.0.1', '2024-04-02 17:20:16');
INSERT INTO `sys_log` VALUES (34, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 11, '127.0.0.1', '2024-04-02 17:20:25');
INSERT INTO `sys_log` VALUES (35, 'admin', '修改菜单', 'com.sandy.fw.admin.controller.MenuController.update()', '[{\"icon\":\"icon-role\",\"id\":17,\"menuName\":\"角色管理\",\"orderNum\":0,\"parentId\":1,\"path\":\"sys/role\",\"perms\":\"\",\"type\":1}]', 21, '127.0.0.1', '2024-04-02 17:38:52');
INSERT INTO `sys_log` VALUES (36, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 10, '127.0.0.1', '2024-04-02 17:39:05');
INSERT INTO `sys_log` VALUES (37, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":18,\"menuName\":\"新增\",\"orderNum\":0,\"parentId\":17,\"path\":\"\",\"perms\":\"sys:role:save\",\"type\":2}]', 35, '127.0.0.1', '2024-04-02 17:40:58');
INSERT INTO `sys_log` VALUES (38, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":19,\"menuName\":\"修改\",\"orderNum\":0,\"parentId\":17,\"path\":\"\",\"perms\":\"sys:role:update\",\"type\":2}]', 30, '127.0.0.1', '2024-04-02 17:41:30');
INSERT INTO `sys_log` VALUES (39, 'admin', '新增菜单', 'com.sandy.fw.admin.controller.MenuController.save()', '[{\"icon\":\"\",\"id\":20,\"menuName\":\"删除\",\"orderNum\":0,\"parentId\":17,\"path\":\"\",\"perms\":\"sys:role:delete\",\"type\":2}]', 20, '127.0.0.1', '2024-04-02 17:41:48');
INSERT INTO `sys_log` VALUES (40, 'admin', '退出登录', 'com.sandy.fw.admin.controller.LoginController.logout()', '', 3, '127.0.0.1', '2024-04-02 17:41:57');
INSERT INTO `sys_log` VALUES (41, 'admin', '修改菜单', 'com.sandy.fw.admin.controller.MenuController.update()', '[{\"icon\":\"icon-notice\",\"id\":12,\"menuName\":\"操作日志\",\"orderNum\":1,\"parentId\":1,\"path\":\"sys/log\",\"perms\":\"sys:log:page\",\"type\":1}]', 129, '127.0.0.1', '2024-04-03 09:18:49');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(20) NOT NULL COMMENT '父级菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '类型   0：目录   1：菜单   2：按钮',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '', NULL, 0, '1', NULL, 'icon-system', NULL, '2024-03-25 15:27:19', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', '', 1, '1', 'sys:user:page', 'icon-admin', NULL, '2024-03-25 15:29:13', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (3, 2, '查看', '', NULL, 2, '1', 'sys:user:list', '#', NULL, '2024-03-25 15:41:19', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (4, 2, '修改', '', NULL, 2, '1', 'sys:user:update,sys:user:info', '#', NULL, '2024-03-29 15:24:48', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (5, 2, '删除', '', NULL, 2, '1', 'sys:user:delete', '#', NULL, '2024-03-29 15:25:42', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (7, 2, '新增', '', NULL, 2, '1', 'sys:user:save', '#', NULL, '2024-04-01 09:15:56', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (8, 1, '菜单管理', 'sys/menu', NULL, 1, '1', 'sys:menu:info', 'icon-menu', NULL, '2024-04-01 16:27:31', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (9, 8, '新增', '', NULL, 2, '1', 'sys:menu:save', '#', NULL, '2024-04-01 16:50:47', NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (10, 8, '修改', '', NULL, 2, '1', 'sys:menu:update', '', NULL, NULL, NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (11, 8, '删除', '', NULL, 2, '1', 'sys:menu:delete', '', NULL, NULL, NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (12, 1, '操作日志', 'sys/log', NULL, 1, '1', 'sys:log:page', 'icon-notice', NULL, NULL, NULL, NULL, '', 1);
INSERT INTO `sys_menu` VALUES (17, 1, '角色管理', 'sys/role', NULL, 1, '0', '', 'icon-role', NULL, NULL, NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (18, 17, '新增', '', NULL, 2, '0', 'sys:role:save', '', NULL, NULL, NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (19, 17, '修改', '', NULL, 2, '0', 'sys:role:update', '', NULL, NULL, NULL, NULL, '', 0);
INSERT INTO `sys_menu` VALUES (20, 17, '删除', '', NULL, 2, '0', 'sys:role:delete', '', NULL, NULL, NULL, NULL, '', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（1正常0停用）',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', '1', NULL, '2024-03-26 13:49:24', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(200) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(200) NOT NULL DEFAULT 0 COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (11, 5, 2);
INSERT INTO `sys_role_menu` VALUES (12, 5, 3);
INSERT INTO `sys_role_menu` VALUES (13, 5, 4);
INSERT INTO `sys_role_menu` VALUES (14, 5, 5);
INSERT INTO `sys_role_menu` VALUES (15, 5, 7);
INSERT INTO `sys_role_menu` VALUES (16, 5, -666666);
INSERT INTO `sys_role_menu` VALUES (17, 5, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `user_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '用户类型：1代表普通用户，0代表管理员',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超级管理员', '{bcrypt}$2a$10$Tk/7jOwsdeNg4opakiZslOD.ZWrnSExmaq/0n.GXsbZTcFfxwbl3.', 1, 'admin@cdqcp.com', '13540179099', NULL, NULL, '0', NULL, '2024-03-25 14:52:11', NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (2, 'sandy', '山哥哥', '{bcrypt}$2a$10$AV9Xz.3ck4RsXiad5ArcBO5.ZKwlpcnJzs740BHY..fsSp0PnM/Zu', 1, 'sandy@cdqcp.com', '11111111111', NULL, NULL, '1', NULL, '2024-03-26 13:53:39', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(200) NOT NULL COMMENT '用户ID',
  `role_id` bigint(200) NOT NULL DEFAULT 0 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 2, 1);

SET FOREIGN_KEY_CHECKS = 1;
