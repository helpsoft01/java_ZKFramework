-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jun 15, 2015 at 07:03 PM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `taxioperation`
--

-- --------------------------------------------------------

--
-- Table structure for table `abbreviation`
--

CREATE TABLE IF NOT EXISTS `abbreviation` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `abbreviation`
--

INSERT INTO `abbreviation` (`id`, `name`, `note`, `value`) VALUES
(2, 'HH2 Bắc Hà - Lê Văn Lương', NULL, 'HH2');

-- --------------------------------------------------------

--
-- Table structure for table `car_supplier`
--

CREATE TABLE IF NOT EXISTS `car_supplier` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `car_supplier`
--

INSERT INTO `car_supplier` (`id`, `name`, `value`) VALUES
(1, 'Toyota', 'Toyota'),
(2, 'Hyundai', 'Hyundai'),
(3, 'Bentley', 'Bentley'),
(4, 'Maybach', 'Maybach'),
(5, 'Roll Royce', 'RR');

-- --------------------------------------------------------

--
-- Table structure for table `channel`
--

CREATE TABLE IF NOT EXISTS `channel` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `province_id` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `channel`
--

INSERT INTO `channel` (`id`, `name`, `value`, `province_id`) VALUES
(1, 'Kênh 1', 'HCM_K1', 1),
(2, 'Kênh 2', 'HCM_K2', 1),
(3, 'Kênh 3', 'HCM_K3', 1);

-- --------------------------------------------------------

--
-- Table structure for table `color`
--

CREATE TABLE IF NOT EXISTS `color` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `color`
--

INSERT INTO `color` (`id`, `name`, `value`) VALUES
(1, 'Xanh', 'X'),
(2, 'Đỏ', 'D');

-- --------------------------------------------------------

--
-- Table structure for table `config`
--

CREATE TABLE IF NOT EXISTS `config` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `config`
--

INSERT INTO `config` (`id`, `name`, `note`, `value`) VALUES
(1, 'Mã', 'Test chơi chơi', 'Giá trị');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) NOT NULL,
  `address` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `address2` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `address3` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `isActive` bit(1) NOT NULL,
  `isFrequently` bit(1) NOT NULL,
  `isVIP` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `phoneNumber` varchar(255) COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `address`, `address2`, `address3`, `email`, `isActive`, `isFrequently`, `isVIP`, `name`, `note`, `phoneNumber`) VALUES
(1, 'Hà Đông, Hà Nội', NULL, NULL, 'tuanpa@vietek.com.vn', b'1', b'1', b'1', 'Phạm Anh Tuấn', 'Khách cực VIP', '0945868885'),
(2, 'Cẩm Phả, Quảng Ninh', NULL, NULL, 'vanltc0408@gmail.com', b'1', b'0', b'1', 'Lê Thị Cẩm Vân', NULL, '0913914885'),
(3, 'Cửa sông Hồng', NULL, NULL, 'Vud@vietek.com.vn', b'1', b'0', b'1', 'Đỗ Vũ', NULL, '0988594179');

-- --------------------------------------------------------

--
-- Table structure for table `function`
--

CREATE TABLE IF NOT EXISTS `function` (
  `id` int(11) NOT NULL,
  `clazz` varchar(255) COLLATE utf8_vietnamese_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci NOT NULL,
  `isActive` bit(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `function`
--

INSERT INTO `function` (`id`, `clazz`, `name`, `isActive`) VALUES
(1, '/zul/account_info.zul', 'Account Info', b'1'),
(2, '/zul/manager_dashboard.zul', 'Manager Dashboard', b'1'),
(3, '/zul/customers.zul', 'List customer', b'1'),
(4, '/zul/address_shotcut.zul', 'Address shotcut', b'1'),
(5, '/zul/taxi_orders.zul', 'Order List', b'1'),
(8, '/zul/color.zul', 'color', b'1'),
(9, '/zul/taxitype.zul', 'Taxi Types', b'1'),
(10, '/zul/abbreviation.zul', 'Abbreviation', b'1'),
(11, '/zul/carsupplier.zul', 'carsuppler', b'1'),
(12, '/zul/taxi.zul', 'taxi', b'1'),
(13, '/zul/placename.zul', 'Dia danh', b'1'),
(14, '/zul/province.zul', 'TinhThanh', b'1'),
(15, '/zul/channel.zul', 'Kenh', b'1'),
(16, '/zul/taxi_order_dd.zul', 'Dieu dam', b'1'),
(17, '/zul/config.zul', 'Config', b'1'),
(18, '/zul/user.zul', 'User', b'1');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id` int(11) NOT NULL,
  `isActive` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci NOT NULL,
  `type` int(11) NOT NULL,
  `function_id` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `isActive`, `name`, `type`, `function_id`) VALUES
(1, b'1', 'menu.orderList', 2, 5),
(3, b'1', 'menu.managerDashboard', 2, 2),
(4, b'1', 'menu.customer_list', 2, 3),
(5, b'1', 'menu.addressShotcut', 1, 4),
(8, b'1', 'Màu sắc', 1, 8),
(9, b'1', 'Loại taxi', 1, 9),
(10, b'1', 'Bảng viết tắt', 1, 10),
(11, b'1', 'Hãng xe', 1, 11),
(12, b'1', 'Xe taxi', 1, 12),
(13, b'1', 'Địa danh', 1, 13),
(14, b'1', 'Tỉnh thành', 1, 14),
(15, b'1', 'Kênh', 1, 15),
(16, b'1', 'Điều Đàm', 2, 16),
(17, b'1', 'Cấu hình hệ thống', 1, 17),
(18, b'1', 'Người dùng', 1, 18);

-- --------------------------------------------------------

--
-- Table structure for table `order_taxi`
--

CREATE TABLE IF NOT EXISTS `order_taxi` (
  `taxi_order_id` int(11) NOT NULL,
  `taxi_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `order_taxi`
--

INSERT INTO `order_taxi` (`taxi_order_id`, `taxi_id`) VALUES
(1, 1),
(5, 1),
(6, 1),
(1, 2),
(5, 2),
(6, 2);

-- --------------------------------------------------------

--
-- Table structure for table `placename`
--

CREATE TABLE IF NOT EXISTS `placename` (
  `id` int(11) NOT NULL,
  `address` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `placename`
--

INSERT INTO `placename` (`id`, `address`, `name`, `note`, `value`) VALUES
(1, NULL, 'Thái Bình', NULL, 'TB'),
(2, NULL, 'Hà Nội', NULL, 'HN'),
(3, NULL, 'TP Hồ Chí Minh', NULL, 'HCM');

-- --------------------------------------------------------

--
-- Table structure for table `province`
--

CREATE TABLE IF NOT EXISTS `province` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `province`
--

INSERT INTO `province` (`id`, `name`, `value`) VALUES
(1, 'TP Hồ Chí Minh', 'HCM'),
(2, 'Hà Nội', 'HNI'),
(3, 'Thái Bình', 'TBH'),
(4, 'Bình Dương', 'BDG');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

-- --------------------------------------------------------

--
-- Table structure for table `taxi`
--

CREATE TABLE IF NOT EXISTS `taxi` (
  `id` int(11) NOT NULL,
  `taxiNumber` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `isActive` bit(1) NOT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `carsupplier_id` int(11) DEFAULT NULL,
  `color_id` int(11) DEFAULT NULL,
  `taxitype_id` int(11) DEFAULT NULL,
  `producetionYear` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `taxi`
--

INSERT INTO `taxi` (`id`, `taxiNumber`, `isActive`, `value`, `carsupplier_id`, `color_id`, `taxitype_id`, `producetionYear`) VALUES
(1, '55A - 50505', b'0', '2001', 5, 1, 1, 1990),
(2, '80B1 - 56789', b'0', '2002', 4, 2, 1, 2000);

-- --------------------------------------------------------

--
-- Table structure for table `taxitype`
--

CREATE TABLE IF NOT EXISTS `taxitype` (
  `id` int(11) NOT NULL,
  `isActive` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `seats` int(11) NOT NULL,
  `value` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `color_id` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `taxitype`
--

INSERT INTO `taxitype` (`id`, `isActive`, `name`, `seats`, `value`, `color_id`) VALUES
(1, b'0', '4 chỗ - Xanh', 4, NULL, 1),
(2, b'0', '7 chỗ - Đỏ', 7, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `taxi_order`
--

CREATE TABLE IF NOT EXISTS `taxi_order` (
  `id` int(11) NOT NULL,
  `address1` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `address2` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `address3` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `callInTime` datetime DEFAULT NULL,
  `callRepeatTime` int(11) NOT NULL,
  `extNumber` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `inTaxiTime` datetime DEFAULT NULL,
  `isCancel` bit(1) DEFAULT NULL,
  `isVip` bit(1) DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `orderTime` datetime DEFAULT NULL,
  `phoneNumber` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `pickupAddress` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `channel_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `taxi_id` int(11) DEFAULT NULL,
  `province_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `taxi_order`
--

INSERT INTO `taxi_order` (`id`, `address1`, `address2`, `address3`, `callInTime`, `callRepeatTime`, `extNumber`, `inTaxiTime`, `isCancel`, `isVip`, `note`, `orderTime`, `phoneNumber`, `pickupAddress`, `channel_id`, `customer_id`, `taxi_id`, `province_id`, `user_id`) VALUES
(1, '', '', '', '2015-06-15 15:25:50', 0, NULL, NULL, b'0', b'0', NULL, NULL, '0988594179', 'Nha tho duc ba', 1, NULL, 1, NULL, 1),
(2, NULL, NULL, NULL, '2015-06-15 17:43:48', 0, NULL, NULL, b'0', b'0', NULL, NULL, '0988594179', NULL, 1, 3, NULL, NULL, 1),
(5, '', '', '', '2015-06-15 19:00:35', 0, NULL, NULL, b'0', b'0', NULL, NULL, '0988594179', 'Nhà thờ đức bà', 1, 3, NULL, NULL, 1),
(6, '', '', '', '2015-06-15 19:02:24', 0, NULL, NULL, b'0', b'0', NULL, NULL, '0945868885', '58 Trần Hưng Đạo', 1, 1, NULL, NULL, 1),
(7, NULL, NULL, NULL, '2015-06-16 00:00:56', 0, NULL, NULL, b'0', b'0', NULL, NULL, '0945868885', '23 Bình Lợi', 1, 1, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL,
  `accountName` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `birthDay` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `extNumber` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `fullName` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `isActive` bit(1) NOT NULL,
  `password` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `phoneNumber` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `channel_id` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `accountName`, `address`, `birthDay`, `description`, `email`, `extNumber`, `fullName`, `isActive`, `password`, `phoneNumber`, `channel_id`) VALUES
(1, 'admin', 'Dương Nội, Hà Dông, Hà Nội', '1985-01-22 00:00:00', 'This is administrator of this system.\nHe can do enything in this system :)1 ', 'tuanpa@vietek.com.vn', '8002', 'Phạm Anh Tuấn', b'1', '1234', '0945868885', NULL),
(2, 'dovu', 'Cửa sông Hồng', NULL, NULL, NULL, '8003', 'Đỗ Vũ', b'1', '1234', '0988594179', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE IF NOT EXISTS `user_role` (
  `User_ID` int(11) NOT NULL,
  `Role_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `abbreviation`
--
ALTER TABLE `abbreviation`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `car_supplier`
--
ALTER TABLE `car_supplier`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_m1ikg965o4n6p9al2iku2436o` (`province_id`);

--
-- Indexes for table `color`
--
ALTER TABLE `color`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `function`
--
ALTER TABLE `function`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_hsxc1iexn7p6ol9j1id513x4c` (`function_id`);

--
-- Indexes for table `order_taxi`
--
ALTER TABLE `order_taxi`
  ADD PRIMARY KEY (`taxi_order_id`,`taxi_id`), ADD KEY `FK_jgtq4x0yli83t19pm63xbj5gy` (`taxi_id`);

--
-- Indexes for table `placename`
--
ALTER TABLE `placename`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `province`
--
ALTER TABLE `province`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `taxi`
--
ALTER TABLE `taxi`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_b8lbsp0ky93f3wlj1mte3g7yq` (`carsupplier_id`), ADD KEY `FK_dcbl49xvosrkb91rtjynsnu2p` (`color_id`), ADD KEY `FK_7muhhl5x7pjc1qf1u5y9hb08j` (`taxitype_id`);

--
-- Indexes for table `taxitype`
--
ALTER TABLE `taxitype`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_707jxy66pas8v1agsr6q5g18e` (`color_id`);

--
-- Indexes for table `taxi_order`
--
ALTER TABLE `taxi_order`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_i66l3lukg48dww1tdpd7y7c4t` (`channel_id`), ADD KEY `FK_jbnclt4oqslmajdtljyhk4co4` (`customer_id`), ADD KEY `FK_hudmtc3qnatemj8iclpp9ipri` (`taxi_id`), ADD KEY `FK_809l8avkcgs8m6w52dtcqs5i7` (`province_id`), ADD KEY `FK_gvi1oifd0fsfdcndt0pn9atwu` (`user_id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`), ADD KEY `FK_5x70jq612lxttxfpsmvijn0by` (`channel_id`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`User_ID`,`Role_ID`), ADD KEY `FK_2b44mwqxwxa4kicwvav6x5jqw` (`Role_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `abbreviation`
--
ALTER TABLE `abbreviation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `car_supplier`
--
ALTER TABLE `car_supplier`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `channel`
--
ALTER TABLE `channel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `color`
--
ALTER TABLE `color`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `config`
--
ALTER TABLE `config`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `function`
--
ALTER TABLE `function`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `placename`
--
ALTER TABLE `placename`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `province`
--
ALTER TABLE `province`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `taxi`
--
ALTER TABLE `taxi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `taxitype`
--
ALTER TABLE `taxitype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `taxi_order`
--
ALTER TABLE `taxi_order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `channel`
--
ALTER TABLE `channel`
ADD CONSTRAINT `FK_m1ikg965o4n6p9al2iku2436o` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`);

--
-- Constraints for table `menu`
--
ALTER TABLE `menu`
ADD CONSTRAINT `FK_hsxc1iexn7p6ol9j1id513x4c` FOREIGN KEY (`function_id`) REFERENCES `function` (`id`);

--
-- Constraints for table `order_taxi`
--
ALTER TABLE `order_taxi`
ADD CONSTRAINT `FK_8cy1kck81g1yjjfv013m8tna5` FOREIGN KEY (`taxi_order_id`) REFERENCES `taxi_order` (`id`),
ADD CONSTRAINT `FK_jgtq4x0yli83t19pm63xbj5gy` FOREIGN KEY (`taxi_id`) REFERENCES `taxi` (`id`);

--
-- Constraints for table `taxi`
--
ALTER TABLE `taxi`
ADD CONSTRAINT `FK_7muhhl5x7pjc1qf1u5y9hb08j` FOREIGN KEY (`taxitype_id`) REFERENCES `taxitype` (`id`),
ADD CONSTRAINT `FK_b8lbsp0ky93f3wlj1mte3g7yq` FOREIGN KEY (`carsupplier_id`) REFERENCES `car_supplier` (`id`),
ADD CONSTRAINT `FK_dcbl49xvosrkb91rtjynsnu2p` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`);

--
-- Constraints for table `taxitype`
--
ALTER TABLE `taxitype`
ADD CONSTRAINT `FK_707jxy66pas8v1agsr6q5g18e` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`);

--
-- Constraints for table `taxi_order`
--
ALTER TABLE `taxi_order`
ADD CONSTRAINT `FK_809l8avkcgs8m6w52dtcqs5i7` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`),
ADD CONSTRAINT `FK_gvi1oifd0fsfdcndt0pn9atwu` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
ADD CONSTRAINT `FK_hudmtc3qnatemj8iclpp9ipri` FOREIGN KEY (`taxi_id`) REFERENCES `taxi` (`id`),
ADD CONSTRAINT `FK_i66l3lukg48dww1tdpd7y7c4t` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`),
ADD CONSTRAINT `FK_jbnclt4oqslmajdtljyhk4co4` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
ADD CONSTRAINT `FK_5x70jq612lxttxfpsmvijn0by` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
ADD CONSTRAINT `FK_2b44mwqxwxa4kicwvav6x5jqw` FOREIGN KEY (`Role_ID`) REFERENCES `role` (`id`),
ADD CONSTRAINT `FK_7lmhwficvqjhbphqr3tjkh8cb` FOREIGN KEY (`User_ID`) REFERENCES `user` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
