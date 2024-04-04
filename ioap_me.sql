/*
 Navicat Premium Data Transfer

 Source Server         : hazy oap
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : hazy:3306
 Source Schema         : ioap_me

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 11/03/2024 16:40:45
*/

--
-- NOTE: THIS IS OUT OF DATE!
--
-- Database tables are created by the system (assuming appropriate environment)
--
-- Databases ARE NOT!
--
--
-- CREATE DATABASE xxxx DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATION utf8mb4_0900_ai_ci 
--

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for citation
-- ----------------------------
DROP TABLE IF EXISTS `citation`;
CREATE TABLE `citation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `citation_author_list` longtext,
  `cruise_id` varchar(255) DEFAULT NULL,
  `dataset_abstract` longtext,
  `document_id` bigint(20) NOT NULL,
  `expocode` varchar(255) DEFAULT NULL,
  `purpose` longtext,
  `research_projects` longtext,
  `scientific_references` longtext,
  `section` varchar(255) DEFAULT NULL,
  `supplemental_information` longtext,
  `title` longtext,
  `use_limitation` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=503 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for country_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `country_suggestion`;
CREATE TABLE `country_suggestion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `suggestion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `dataset_identifier` varchar(255) NOT NULL,
  `last_modified` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=508 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for document_update_listener
-- ----------------------------
DROP TABLE IF EXISTS `document_update_listener`;
CREATE TABLE `document_update_listener` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `document_id` varchar(255) NOT NULL,
  `document_location` varchar(255) NOT NULL,
  `notification_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for funding
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=405 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for generic_variable
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
  `equilibrator_type` varchar(255) DEFAULT NULL,
  `equilibrator_volume` varchar(255) DEFAULT NULL,
  `field_replicate` longtext,
  `flow_rate` varchar(255) DEFAULT NULL,
  `freqency_of_standardization` longtext,
  `full_variable_name` longtext,
  `gas_concentration` varchar(255) DEFAULT NULL,
  `gas_dectector_resolution` varchar(255) DEFAULT NULL,
  `gas_dectector_uncertainty` longtext,
  `gas_detector_manufacture` varchar(255) DEFAULT NULL,
  `gas_detector_model` varchar(255) DEFAULT NULL,
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
  `standard_gas_manufacture` varchar(255) DEFAULT NULL,
  `standard_gas_uncertainties` longtext,
  `standardization_technique` longtext,
  `storage_method` longtext,
  `temperature_correction_method` longtext,
  `temperature_measurement` varchar(255) DEFAULT NULL,
  `temperature_standarization` longtext,
  `titration_type` longtext,
  `uncertainty` longtext,
  `units` longtext,
  `vapor_correction` longtext,
  `vented` varchar(255) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `variables_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6642 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ncei_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `ncei_suggestion`;
CREATE TABLE `ncei_suggestion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `suggestion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `institution` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mi` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `investigators_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2488 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for person_typed_string
-- ----------------------------
DROP TABLE IF EXISTS `person_typed_string`;
CREATE TABLE `person_typed_string` (
  `person_researcher_ids_id` bigint(20) DEFAULT NULL,
  `typed_string_id` bigint(20) DEFAULT NULL,
  `researcher_ids_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for platform
-- ----------------------------
DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `country` varchar(255) DEFAULT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `platform_id` varchar(255) DEFAULT NULL,
  `platform_type` varchar(255) DEFAULT NULL,
  `platforms_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for suggest_query
-- ----------------------------
DROP TABLE IF EXISTS `suggest_query`;
CREATE TABLE `suggest_query` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `query` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for time_and_location
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
  `organism_loc` varchar(255) DEFAULT NULL,
  `south_lat` varchar(255) DEFAULT NULL,
  `spatial_ref` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `west_lon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=504 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for typed_string
-- ----------------------------
DROP TABLE IF EXISTS `typed_string`;
CREATE TABLE `typed_string` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1068 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
