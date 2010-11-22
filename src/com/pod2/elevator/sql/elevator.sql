SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `ElevatorDB` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `ElevatorDB` ;

-- -----------------------------------------------------
-- Table `ElevatorDB`.`SimulationTemplate`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`SimulationTemplate` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `numberFloors` INT NOT NULL ,
  `elevatorCapacity` INT NOT NULL ,
  `numberElevators` INT NOT NULL ,
  `scheduler` VARCHAR(45) NOT NULL ,
  `requestGenerationOn` TINYINT(1)  NOT NULL ,
  `name` VARCHAR(90) NOT NULL ,
  `created` DATE NOT NULL ,
  `lastEdit` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`SimulationResults`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`SimulationResults` (
  `uuid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(90) NOT NULL ,
  `templateId` INT NOT NULL ,
  `startTime` DATE NOT NULL ,
  `stopTime` DATE NOT NULL ,
  `startQuantum` BIGINT NOT NULL ,
  `stopQuantum` BIGINT NOT NULL ,
  PRIMARY KEY (`uuid`) ,
  INDEX `sr_templateId` (`templateId` ASC) ,
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) ,
  CONSTRAINT `sr_templateId`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`ElevatorState`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`ElevatorState` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resultId` INT NOT NULL ,
  `elevatorNumber` INT NOT NULL ,
  `position` DOUBLE NOT NULL ,
  `quantum` MEDIUMTEXT  NOT NULL ,
  `status` INT NOT NULL ,
  INDEX `es_resultId` (`resultId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `es_resultId`
    FOREIGN KEY (`resultId` )
    REFERENCES `ElevatorDB`.`SimulationResults` (`uuid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`CompletedRequest`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`CompletedRequest` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resultId` INT NOT NULL ,
  `elevatorNumber` INT NOT NULL ,
  `onloadFloor` INT NOT NULL ,
  `offloadFloor` INT NOT NULL ,
  `enterQuantum` BIGINT NOT NULL ,
  `onloadQuantum` BIGINT NOT NULL ,
  `offloadQuantum` BIGINT NOT NULL ,
  `timeConstraint` BIGINT NOT NULL ,
  `deliveryStatus` INT NOT NULL ,
  INDEX `cr_resultId` (`resultId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `cr_resultId`
    FOREIGN KEY (`resultId` )
    REFERENCES `ElevatorDB`.`SimulationResults` (`uuid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`LoggedEvent`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`LoggedEvent` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resultId` INT NOT NULL ,
  `quantum` BIGINT NOT NULL ,
  `message` TEXT NOT NULL ,
  INDEX `le_resultId` (`resultId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `le_resultId`
    FOREIGN KEY (`resultId` )
    REFERENCES `ElevatorDB`.`SimulationResults` (`uuid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`TemplatePassengerRequest`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`TemplatePassengerRequest` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `templateId` INT NOT NULL ,
  `onloadFloor` INT NOT NULL ,
  `offloadFloor` INT NOT NULL ,
  `timeConstraint` BIGINT NOT NULL ,
  `quantum` BIGINT NOT NULL ,
  INDEX `tpr_templateId` (`templateId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `tpr_templateId`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`TemplateServiceEvent`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`TemplateServiceEvent` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `templateId` INT NOT NULL ,
  `putInService` TINYINT(1)  NOT NULL ,
  `elevatorNumber` INT NOT NULL ,
  `quantum` BIGINT NOT NULL ,
  INDEX `templateId` (`templateId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `templateId`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`TemplateFailureEvent`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`TemplateFailureEvent` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `templateId` INT NOT NULL ,
  `component` VARCHAR(45) NOT NULL ,
  `elevatorNumber` INT NOT NULL ,
  `quantum` BIGINT NOT NULL ,
  INDEX `tfe_templateId` (`templateId` ASC) ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `tfe_templateId`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`RestrictedFloors`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`RestrictedFloors` (
  `templateId` INT NOT NULL ,
  `restrictedFloor` INT NOT NULL ,
  INDEX `rf_templateId` (`templateId` ASC) ,
  PRIMARY KEY (`templateId`, `restrictedFloor`) ,
  CONSTRAINT `rf_templateId`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
