-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 21, 2021 at 01:00 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `attendanceLotus`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `attendanceID` int(11) NOT NULL,
  `ssu_ID` int(11) NOT NULL,
  `attDate` date NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`attendanceID`, `ssu_ID`, `attDate`, `status`) VALUES
(91, 53, '2021-03-18', 1),
(92, 55, '2021-03-18', 1),
(93, 54, '2021-03-18', 1),
(94, 53, '2021-03-17', 1),
(95, 55, '2021-03-17', 1),
(96, 54, '2021-03-17', 1),
(97, 53, '2021-03-16', 1),
(98, 55, '2021-03-16', 1),
(99, 54, '2021-03-16', 1),
(100, 53, '2021-03-15', 1),
(101, 55, '2021-03-15', 1),
(102, 54, '2021-03-15', 1),
(106, 53, '2021-03-13', 1),
(107, 55, '2021-03-13', 1),
(108, 54, '2021-03-13', 1),
(118, 53, '2021-03-19', 1),
(119, 55, '2021-03-19', 1),
(120, 54, '2021-03-19', 1),
(127, 57, '2021-03-19', 1),
(128, 59, '2021-03-19', 1),
(129, 58, '2021-03-19', 1),
(174, 57, '2021-03-18', 1),
(175, 59, '2021-03-18', 1),
(176, 58, '2021-03-18', 1),
(179, 57, '2021-03-17', 1),
(180, 59, '2021-03-17', 1),
(181, 58, '2021-03-17', 1),
(184, 57, '2021-03-16', 1),
(185, 59, '2021-03-16', 1),
(186, 58, '2021-03-16', 1),
(220, 53, '2021-03-20', 1),
(221, 55, '2021-03-20', 1),
(222, 54, '2021-03-20', 1),
(223, 57, '2021-03-20', 1),
(224, 59, '2021-03-20', 1),
(225, 58, '2021-03-20', 1),
(261, 52, '2021-03-16', 1),
(262, 52, '2021-03-15', 1),
(264, 52, '2021-03-13', 1),
(265, 52, '2021-03-12', 1),
(266, 52, '2021-03-11', 1),
(267, 53, '2021-03-12', 1),
(268, 55, '2021-03-12', 1),
(269, 54, '2021-03-12', 1),
(285, 48, '2021-03-20', 1),
(286, 56, '2021-03-20', 1),
(287, 47, '2021-03-20', 1),
(288, 77, '2021-03-20', 1),
(289, 48, '2021-03-19', 1),
(290, 56, '2021-03-19', 1),
(291, 47, '2021-03-19', 1),
(292, 77, '2021-03-19', 1),
(293, 48, '2021-03-18', 3),
(294, 56, '2021-03-18', 2),
(295, 47, '2021-03-18', 1),
(296, 77, '2021-03-18', 3),
(297, 48, '2021-03-17', 1),
(298, 56, '2021-03-17', 2),
(299, 47, '2021-03-17', 1),
(300, 77, '2021-03-17', 1),
(301, 48, '2021-03-16', 1),
(302, 56, '2021-03-16', 2),
(303, 47, '2021-03-16', 1),
(304, 77, '2021-03-16', 1),
(305, 48, '2021-03-15', 1),
(306, 56, '2021-03-15', 1),
(307, 47, '2021-03-15', 3),
(308, 77, '2021-03-15', 1),
(309, 48, '2021-03-13', 1),
(310, 56, '2021-03-13', 1),
(311, 47, '2021-03-13', 3),
(312, 77, '2021-03-13', 1),
(313, 48, '2021-03-12', 2),
(314, 56, '2021-03-12', 2),
(315, 47, '2021-03-12', 3),
(316, 77, '2021-03-12', 1),
(317, 48, '2021-03-11', 1),
(318, 56, '2021-03-11', 1),
(319, 47, '2021-03-11', 3),
(320, 77, '2021-03-11', 1),
(322, 52, '2021-03-20', 1),
(323, 71, '2021-03-20', 1),
(324, 50, '2021-03-20', 1),
(325, 72, '2021-03-20', 1),
(326, 49, '2021-03-20', 1),
(327, 51, '2021-03-20', 1),
(328, 69, '2021-03-20', 1),
(329, 73, '2021-03-20', 1),
(330, 71, '2021-03-19', 1),
(331, 50, '2021-03-19', 1),
(332, 72, '2021-03-19', 1),
(333, 49, '2021-03-19', 1),
(334, 51, '2021-03-19', 1),
(335, 69, '2021-03-19', 1),
(336, 73, '2021-03-19', 1),
(337, 71, '2021-03-18', 1),
(338, 50, '2021-03-18', 1),
(339, 72, '2021-03-18', 1),
(340, 49, '2021-03-18', 1),
(341, 51, '2021-03-18', 1),
(342, 69, '2021-03-18', 1),
(343, 73, '2021-03-18', 1),
(344, 71, '2021-03-17', 1),
(345, 50, '2021-03-17', 1),
(346, 72, '2021-03-17', 1),
(347, 49, '2021-03-17', 1),
(348, 51, '2021-03-17', 1),
(349, 69, '2021-03-17', 1),
(350, 73, '2021-03-17', 1),
(351, 71, '2021-03-16', 1),
(352, 50, '2021-03-16', 1),
(353, 72, '2021-03-16', 1),
(354, 49, '2021-03-16', 1),
(355, 51, '2021-03-16', 1),
(356, 69, '2021-03-16', 1),
(357, 73, '2021-03-16', 1),
(358, 71, '2021-03-15', 1),
(359, 50, '2021-03-15', 1),
(360, 72, '2021-03-15', 1),
(361, 49, '2021-03-15', 1),
(362, 51, '2021-03-15', 1),
(363, 69, '2021-03-15', 1),
(364, 73, '2021-03-15', 1),
(365, 57, '2021-03-15', 1),
(366, 59, '2021-03-15', 1),
(367, 58, '2021-03-15', 1),
(371, 57, '2021-03-13', 1),
(372, 59, '2021-03-13', 1),
(373, 58, '2021-03-13', 1),
(374, 57, '2021-03-12', 1),
(375, 59, '2021-03-12', 1),
(376, 58, '2021-03-12', 1),
(377, 52, '2021-03-17', 1),
(378, 52, '2021-03-18', 1),
(379, 52, '2021-03-19', 1);

-- --------------------------------------------------------

--
-- Table structure for table `classes`
--

CREATE TABLE `classes` (
  `classID` int(11) NOT NULL,
  `year` char(1) NOT NULL,
  `block` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `classes`
--

INSERT INTO `classes` (`classID`, `year`, `block`) VALUES
(8, '2', 'B'),
(9, '2', 'A'),
(10, '1', 'A'),
(12, '3', 'A');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `courseID` int(11) NOT NULL,
  `courseName` varchar(255) NOT NULL,
  `isHidden` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`courseID`, `courseName`, `isHidden`) VALUES
(8, 'BSCS', 0),
(9, 'BSIT', 0),
(10, 'BSEMC', 0),
(14, 'ACT', 1);

-- --------------------------------------------------------

--
-- Table structure for table `courses_classes`
--

CREATE TABLE `courses_classes` (
  `ccl_ID` int(11) NOT NULL,
  `courseID` int(11) NOT NULL,
  `classID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `courses_classes`
--

INSERT INTO `courses_classes` (`ccl_ID`, `courseID`, `classID`) VALUES
(15, 8, 8),
(16, 9, 10),
(26, 9, 9),
(27, 10, 8),
(29, 10, 9),
(33, 9, 8),
(38, 8, 9),
(39, 14, 8);

-- --------------------------------------------------------

--
-- Table structure for table `courses_classes_subjects`
--

CREATE TABLE `courses_classes_subjects` (
  `cclsu_ID` int(11) NOT NULL,
  `ccl_ID` int(11) NOT NULL,
  `subjectID` int(11) NOT NULL,
  `teacherID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `courses_classes_subjects`
--

INSERT INTO `courses_classes_subjects` (`cclsu_ID`, `ccl_ID`, `subjectID`, `teacherID`) VALUES
(10, 15, 6, 4),
(11, 15, 7, 7),
(18, 29, 5, NULL),
(21, 29, 6, 4),
(22, 29, 7, NULL),
(32, 26, 5, NULL),
(33, 26, 6, 4),
(34, 38, 5, 4),
(35, 38, 6, 4),
(39, 29, 9, NULL),
(40, 29, 10, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `studID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `fName` varchar(255) NOT NULL,
  `mName` varchar(255) NOT NULL,
  `lName` varchar(255) NOT NULL,
  `courseID` int(11) NOT NULL,
  `studentID` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`studID`, `userID`, `fName`, `mName`, `lName`, `courseID`, `studentID`) VALUES
(26, 80, 'Angelo', 'Flores', 'Gopez', 10, '201910501'),
(31, 87, 'Roy', 'Flores', 'Gopez', 8, '201910502'),
(33, 92, 'Erahlyn', 'Kiszha', 'Celiz', 10, '201910503'),
(34, 93, 'Jesusthenes', 'Lo-ot', 'Miravalles', 10, '201910504'),
(35, 94, 'Everly Stecel', 'Roa', 'Bayog', 8, '201910505'),
(36, 95, 'Pocholo', 'Lagario', 'Gopez', 9, '201910506'),
(37, 96, 'Haidee', 'Hidocos', 'Hidocos', 8, '201910507'),
(38, 97, 'Jezreel', 'Joshua', 'Martin', 8, '201910508'),
(39, 98, 'Mark', 'Jason', 'Margallo', 8, '201910509'),
(40, 99, 'Fernand', 'Fermin', 'Fermin', 8, '201910510'),
(41, 100, 'John Angelo ', 'Philip', 'Vergel', 8, '201910511'),
(42, 101, 'Patrisha', 'Marie', 'Pineda', 8, '201910512'),
(43, 102, 'Samantha', 'Layco', 'Layco', 8, '201910513'),
(44, 103, 'John', 'Larry', 'Sumala', 8, '201910514'),
(45, 104, 'Robin', 'Fernandez', 'Fernandez', 10, '201910515'),
(46, 105, 'Geevee', 'Jay', 'Selibio', 10, '201910516'),
(47, 106, 'Alyssa', 'Sarmiento', 'Sarmiento', 8, '201910517'),
(48, 107, 'Jhon', 'Arnel', 'Menor', 8, '201910518'),
(49, 108, 'Maverick', 'Ace', 'Malicsi', 14, '201910519'),
(53, 118, 'April', 'Mae', 'Basco', 10, '201910520'),
(54, 119, 'John', 'Ray', 'Vino', 10, '201910521');

-- --------------------------------------------------------

--
-- Table structure for table `students_cclsu`
--

CREATE TABLE `students_cclsu` (
  `ssu_ID` int(11) NOT NULL,
  `studID` int(11) NOT NULL,
  `cclsu_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `students_cclsu`
--

INSERT INTO `students_cclsu` (`ssu_ID`, `studID`, `cclsu_ID`) VALUES
(47, 31, 10),
(48, 35, 10),
(49, 26, 21),
(50, 33, 21),
(51, 34, 21),
(52, 36, 33),
(53, 37, 34),
(54, 38, 34),
(55, 39, 34),
(56, 40, 10),
(57, 37, 35),
(58, 38, 35),
(59, 39, 35),
(62, 31, 11),
(69, 46, 21),
(71, 53, 21),
(72, 45, 21),
(73, 54, 21),
(74, 35, 11),
(75, 40, 11),
(76, 42, 11),
(77, 42, 10);

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `subjectID` int(11) NOT NULL,
  `subjectName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`subjectID`, `subjectName`) VALUES
(5, 'App Development'),
(6, 'Social Issues'),
(7, 'Math'),
(9, 'Science'),
(10, 'English');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE `teachers` (
  `teacherID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `fName` varchar(255) NOT NULL,
  `mName` varchar(255) NOT NULL,
  `lName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`teacherID`, `userID`, `fName`, `mName`, `lName`) VALUES
(4, 75, 'Richard', 'Flores', 'Gopez'),
(7, 85, 'Maria Kristina', 'Flores', 'Gopez'),
(9, 89, 'Joy', 'Flores', 'Gopez');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `username` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `userType` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `username`, `password`, `userType`) VALUES
(75, 'rfgopez1', '1234', 2),
(77, 'admin', 'admin', 1),
(80, '201910501', '1234', 3),
(85, 'mfgopez1', '1234', 2),
(87, '201910502', '1234', 3),
(89, 'jfgopez1', '1234', 2),
(92, '201910503', '1234', 3),
(93, '201910504', '1234', 3),
(94, '201910505', '1234', 3),
(95, '201910506', '1234', 3),
(96, '201910507', '1234', 3),
(97, '201910508', '1234', 3),
(98, '201910509', '1234', 3),
(99, '201910510', '1234', 3),
(100, '201910511', '1234', 3),
(101, '201910512', '1234', 3),
(102, '201910513', '1234', 3),
(103, '201910514', '1234', 3),
(104, '201910515', '1234', 3),
(105, '201910516', '1234', 3),
(106, '201910517', '1234', 3),
(107, '201910518', '1234', 3),
(108, '201910519', '1234', 3),
(118, '201910520', '1234', 3),
(119, '201910521', '1234', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`attendanceID`),
  ADD KEY `ssu_ID` (`ssu_ID`);

--
-- Indexes for table `classes`
--
ALTER TABLE `classes`
  ADD PRIMARY KEY (`classID`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`courseID`);

--
-- Indexes for table `courses_classes`
--
ALTER TABLE `courses_classes`
  ADD PRIMARY KEY (`ccl_ID`),
  ADD KEY `classID` (`classID`),
  ADD KEY `courseID` (`courseID`);

--
-- Indexes for table `courses_classes_subjects`
--
ALTER TABLE `courses_classes_subjects`
  ADD PRIMARY KEY (`cclsu_ID`),
  ADD KEY `ccl_ID` (`ccl_ID`),
  ADD KEY `subjectID` (`subjectID`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`studID`),
  ADD UNIQUE KEY `studentID` (`studentID`);

--
-- Indexes for table `students_cclsu`
--
ALTER TABLE `students_cclsu`
  ADD PRIMARY KEY (`ssu_ID`),
  ADD KEY `studID` (`studID`),
  ADD KEY `cclsu_ID` (`cclsu_ID`);

--
-- Indexes for table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`subjectID`);

--
-- Indexes for table `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`teacherID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `attendanceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=380;

--
-- AUTO_INCREMENT for table `classes`
--
ALTER TABLE `classes`
  MODIFY `classID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `courseID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `courses_classes`
--
ALTER TABLE `courses_classes`
  MODIFY `ccl_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `courses_classes_subjects`
--
ALTER TABLE `courses_classes_subjects`
  MODIFY `cclsu_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `studID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `students_cclsu`
--
ALTER TABLE `students_cclsu`
  MODIFY `ssu_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;

--
-- AUTO_INCREMENT for table `subjects`
--
ALTER TABLE `subjects`
  MODIFY `subjectID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `teachers`
--
ALTER TABLE `teachers`
  MODIFY `teacherID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `ssu_ID` FOREIGN KEY (`ssu_ID`) REFERENCES `students_cclsu` (`ssu_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `courses_classes`
--
ALTER TABLE `courses_classes`
  ADD CONSTRAINT `classID` FOREIGN KEY (`classID`) REFERENCES `classes` (`classID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `courseID` FOREIGN KEY (`courseID`) REFERENCES `courses` (`courseID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `courses_classes_subjects`
--
ALTER TABLE `courses_classes_subjects`
  ADD CONSTRAINT `ccl_ID` FOREIGN KEY (`ccl_ID`) REFERENCES `courses_classes` (`ccl_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `subjectID` FOREIGN KEY (`subjectID`) REFERENCES `subjects` (`subjectID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `students_cclsu`
--
ALTER TABLE `students_cclsu`
  ADD CONSTRAINT `cclsu_ID` FOREIGN KEY (`cclsu_ID`) REFERENCES `courses_classes_subjects` (`cclsu_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `studID` FOREIGN KEY (`studID`) REFERENCES `students` (`studID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
