-- 
-- Database structure for adjourn models
-- Last generated: 2023-11-24
--

CREATE DATABASE IF NOT EXISTS `adjourn_db` 
	DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
USE `adjourn_db`;

--	File	----------

-- Represents a directory that may contain files
-- parent_id: Id of the directory that contains this directory. None if this is the root directory / drive.
CREATE TABLE `directory`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`parent_id` INT, 
	`name` VARCHAR(32) NOT NULL, 
	INDEX d_combo_1_idx (parent_id, name), 
	CONSTRAINT d_d_parent_ref_fk FOREIGN KEY d_d_parent_ref_idx (parent_id) REFERENCES `directory`(`id`) ON DELETE SET NULL
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents a specific file type, such as a text file
-- name: Name of this file type
CREATE TABLE `file_type`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`name` VARCHAR(4), 
	INDEX ft_name_idx (`name`)
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- directory_id:  Id of the directory where this file resides
-- name:          Name of this file
-- type_id:       Id that matches this file's type
-- size:          Size of this file in bytes
-- last_modified: Time when this file was last modified (may be out of date)
CREATE TABLE `file_index`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`directory_id` INT NOT NULL, 
	`name` VARCHAR(32) NOT NULL, 
	`type_id` INT NOT NULL, 
	`size` INT NOT NULL, 
	`last_modified` DATETIME, 
	INDEX fi_combo_1_idx (type_id, size, name), 
	INDEX fi_combo_2_idx (directory_id, name), 
	CONSTRAINT fi_d_directory_ref_fk FOREIGN KEY fi_d_directory_ref_idx (directory_id) REFERENCES `directory`(`id`) ON DELETE CASCADE, 
	CONSTRAINT fi_ft_type_ref_fk FOREIGN KEY fi_ft_type_ref_idx (type_id) REFERENCES `file_type`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

