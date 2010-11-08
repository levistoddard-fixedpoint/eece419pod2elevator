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
  `scheduler` INT NOT NULL ,
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
  INDEX `id` (`templateId` ASC) ,
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) ,
  CONSTRAINT `id`
    FOREIGN KEY (`templateId` )
    REFERENCES `ElevatorDB`.`SimulationTemplate` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`ElevatorState`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`ElevatorState` (
  `resultId` INT NOT NULL ,
  `position` DOUBLE NOT NULL ,
  `status` INT NOT NULL ,
  INDEX `uuid` (`resultId` ASC) ,
  CONSTRAINT `uuid`
    FOREIGN KEY (`resultId` )
    REFERENCES `ElevatorDB`.`SimulationResults` (`uuid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`CompletedRequest`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`CompletedRequest` (
  `resultId` INT NOT NULL ,
  `elevatorNumber` INT NOT NULL ,
  `onloadFloor` INT NOT NULL ,
  `offloadFloor` INT NOT NULL ,
  `enterQuantum` BIGINT NOT NULL ,
  `onloadQuantum` BIGINT NOT NULL ,
  `offloadQuantum` BIGINT NOT NULL ,
  `timeConstraint` BIGINT NOT NULL ,
  `deliveryStatus` INT NOT NULL )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`LoggedEvent`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`LoggedEvent` (
  `resultId` INT NOT NULL ,
  `quantum` BIGINT NOT NULL ,
  `message` TEXT NOT NULL )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`TemplatePassengerRequest`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`TemplatePassengerRequest` (
  `templateId` INT NOT NULL ,
  `onloadFloor` INT NOT NULL ,
  `offloadFloor` INT NOT NULL ,
  `timeConstraint` BIGINT NOT NULL )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ElevatorDB`.`TemplateServiceEvent`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ElevatorDB`.`TemplateServiceEvent` (
  `putInService` TINYINT(1)  NOT NULL )
ENGINE = InnoDB;


;
CREATE USER `admin` IDENTIFIED BY 'admin';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
