/*
 Navicat Premium Data Transfer

 Source Server         : dunkel
 Source Server Type    : MySQL
 Source Server Version : 50173
 Source Host           : dunkel
 Source Database       : oap

 Target Server Type    : MySQL
 Target Server Version : 50173
 File Encoding         : utf-8

 Date: 06/25/2018 14:09:38 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `citation`
-- ----------------------------
DROP TABLE IF EXISTS `citation`;
CREATE TABLE `citation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `citation_author_list` longtext,
  `cruise_id` varchar(255) DEFAULT NULL,
  `document_id` bigint(20) NOT NULL,
  `expocode` varchar(255) DEFAULT NULL,
  `platform_abstract` longtext,
  `projects` longtext,
  `purpose` longtext,
  `research_projects` longtext,
  `scientific_references` longtext,
  `section` varchar(255) DEFAULT NULL,
  `supplemental_information` longtext,
  `title` longtext,
  PRIMARY KEY (`id`),
  KEY `FKioom1ffp0yx3uv5qa7ary352r` (`document_id`),
  CONSTRAINT `FKioom1ffp0yx3uv5qa7ary352r` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `country_suggestion`
-- ----------------------------
DROP TABLE IF EXISTS `country_suggestion`;
CREATE TABLE `country_suggestion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `suggestion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `document`
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `last_modified` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `funding`
-- ----------------------------
DROP TABLE IF EXISTS `funding`;
CREATE TABLE `funding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `agency_name` varchar(255) DEFAULT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `grant_number` varchar(255) DEFAULT NULL,
  `grant_title` longtext,
  `funding_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkwcc9fmu807ouw7t7s9koqs6k` (`document_id`),
  CONSTRAINT `FKkwcc9fmu807ouw7t7s9koqs6k` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `generic_variable`
-- ----------------------------
DROP TABLE IF EXISTS `generic_variable`;
CREATE TABLE `generic_variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `analyzing_instrument` longtext,
  `batch_number` varchar(255) DEFAULT NULL,
  `biological_subject` longtext,
  `calculation_method` longtext,
  `cell_type` varchar(255) DEFAULT NULL,
  `crm_manufacture` varchar(255) DEFAULT NULL,
  `curve_fitting_method` longtext,
  `detailed_information` longtext,
  `drying_method` longtext,
  `duration` varchar(255) DEFAULT NULL,
  `equilibrator_pressure_measure_method` longtext,
  `equilibrator_temperature_measure_method` longtext,
  `equilibrator_type` longtext,
  `equilibrator_volume` varchar(255) DEFAULT NULL,
  `field_replicate` longtext,
  `flow_rate` varchar(255) DEFAULT NULL,
  `freqency_of_standardization` longtext,
  `full_variable_name` varchar(255) DEFAULT NULL,
  `gas_concentration` varchar(255) DEFAULT NULL,
  `gas_dectector_resolution` varchar(255) DEFAULT NULL,
  `gas_dectector_uncertainty` longtext,
  `gas_detector_manufacture` longtext,
  `gas_detector_model` longtext,
  `gas_flow_rate` varchar(255) DEFAULT NULL,
  `headspace_volume` varchar(255) DEFAULT NULL,
  `intake_depth` varchar(255) DEFAULT NULL,
  `intake_location` varchar(255) DEFAULT NULL,
  `life_stage` varchar(255) DEFAULT NULL,
  `magnitude_of_blank_correction` varchar(255) DEFAULT NULL,
  `manipulation_method` longtext,
  `measured` varchar(255) DEFAULT NULL,
  `observation_detail` longtext,
  `observation_type` varchar(255) DEFAULT NULL,
  `pco2temperature` varchar(255) DEFAULT NULL,
  `ph_scale` varchar(255) DEFAULT NULL,
  `ph_standards` longtext,
  `ph_temperature` varchar(255) DEFAULT NULL,
  `poison` longtext,
  `poison_description` longtext,
  `poison_volume` varchar(255) DEFAULT NULL,
  `quality_flag` longtext,
  `reference_method` longtext,
  `researcher_institution` longtext,
  `researcher_name` varchar(255) DEFAULT NULL,
  `sampling_instrument` longtext,
  `seawater_volume` varchar(255) DEFAULT NULL,
  `species_id_code` varchar(255) DEFAULT NULL,
  `standard_gas_manufacture` longtext,
  `standard_gas_uncertainties` longtext,
  `standardization_technique` longtext,
  `storage_method` longtext,
  `temperature_correction` longtext,
  `temperature_correction_method` longtext,
  `temperature_measurement` longtext,
  `temperature_standarization` longtext,
  `titration_type` longtext,
  `uncertainty` longtext,
  `units` varchar(255) DEFAULT NULL,
  `vapor_correction` longtext,
  `vented` varchar(255) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `variables_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK664yjl2d7damuendddx0vpw2l` (`document_id`),
  CONSTRAINT `FK664yjl2d7damuendddx0vpw2l` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `ncei_suggestion`
-- ----------------------------
DROP TABLE IF EXISTS `ncei_suggestion`;
CREATE TABLE `ncei_suggestion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `suggestion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `person`
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `address1` longtext,
  `address2` longtext,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `id_type` varchar(255) DEFAULT NULL,
  `institution` longtext,
  `last_name` varchar(255) DEFAULT NULL,
  `mi` varchar(255) DEFAULT NULL,
  `rid` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `investigators_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKahsnx7wkyvehbeknklxn4365c` (`document_id`),
  CONSTRAINT `FKahsnx7wkyvehbeknklxn4365c` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `platform`
-- ----------------------------
DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `country` varchar(255) DEFAULT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` longtext,
  `platform_id` varchar(255) DEFAULT NULL,
  `platform_type` longtext,
  `platforms_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK82o5gkh3q5c2slwffed5e1ac4` (`document_id`),
  CONSTRAINT `FK82o5gkh3q5c2slwffed5e1ac4` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `suggest_query`
-- ----------------------------
DROP TABLE IF EXISTS `suggest_query`;
CREATE TABLE `suggest_query` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `query` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `time_and_location`
-- ----------------------------
DROP TABLE IF EXISTS `time_and_location`;
CREATE TABLE `time_and_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `document_id` bigint(20) NOT NULL,
  `east_lon` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `geo_names` longtext,
  `north_lat` varchar(255) DEFAULT NULL,
  `organism_loc` longtext,
  `south_lat` varchar(255) DEFAULT NULL,
  `spatial_ref` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `west_lon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfi5wht63e70paohywuf0psh4t` (`document_id`),
  CONSTRAINT `FKfi5wht63e70paohywuf0psh4t` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
