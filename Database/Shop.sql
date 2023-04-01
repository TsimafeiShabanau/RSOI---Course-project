-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema shop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shop
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `shop` ;

-- -----------------------------------------------------
-- Table `shop`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`categories` (
  `CategoryId` INT(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` VARCHAR(64) NOT NULL,
  `CategoryDescription` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`CategoryId`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`vendors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`vendors` (
  `VendorId` INT(11) NOT NULL AUTO_INCREMENT,
  `VendorName` VARCHAR(64) NOT NULL,
  `Email` VARCHAR(64) NULL DEFAULT NULL,
  `PhoneNumber` VARCHAR(20) NULL DEFAULT NULL,
  `Country` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`VendorId`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`products` (
  `ProductId` INT(11) NOT NULL AUTO_INCREMENT,
  `ProductName` VARCHAR(64) NOT NULL,
  `ProductDescription` VARCHAR(256) NULL DEFAULT NULL,
  `Price` DECIMAL(9,2) NOT NULL,
  `VendorId` INT(11) NOT NULL,
  PRIMARY KEY (`ProductId`),
  INDEX `VendorId` (`VendorId` ASC) VISIBLE,
  CONSTRAINT `products_ibfk_1`
    FOREIGN KEY (`VendorId`)
    REFERENCES `shop`.`vendors` (`VendorId`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`category_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`category_product` (
  `CategoryId` INT(11) NOT NULL,
  `ProductId` INT(11) NOT NULL,
  PRIMARY KEY (`CategoryId`, `ProductId`),
  INDEX `ProductId` (`ProductId` ASC) VISIBLE,
  CONSTRAINT `category_product_ibfk_1`
    FOREIGN KEY (`CategoryId`)
    REFERENCES `shop`.`categories` (`CategoryId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `category_product_ibfk_2`
    FOREIGN KEY (`ProductId`)
    REFERENCES `shop`.`products` (`ProductId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`roles` (
  `RoleId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`RoleId`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`users` (
  `UserId` INT(11) NOT NULL AUTO_INCREMENT,
  `Login` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(256) NOT NULL,
  `Email` VARCHAR(128) NOT NULL,
  `RoleId` INT(11) NOT NULL DEFAULT '2',
  PRIMARY KEY (`UserId`),
  INDEX `fk_users_roles1_idx` (`RoleId` ASC) VISIBLE,
  CONSTRAINT `fk_users_roles1`
    FOREIGN KEY (`RoleId`)
    REFERENCES `shop`.`roles` (`RoleId`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`customers` (
  `CustomerId` INT(11) NOT NULL AUTO_INCREMENT,
  `CustomerName` VARCHAR(64) NOT NULL,
  `PhoneNumber` VARCHAR(20) NOT NULL,
  `UserId` INT(11) NOT NULL,
  PRIMARY KEY (`CustomerId`),
  INDEX `fk_customers_users1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_customers_users1`
    FOREIGN KEY (`UserId`)
    REFERENCES `shop`.`users` (`UserId`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`orders` (
  `OrderId` INT(11) NOT NULL AUTO_INCREMENT,
  `OrderStatus` VARCHAR(32) NOT NULL DEFAULT 'Pending',
  `TotalPrice` DECIMAL(9,2) NOT NULL,
  `Date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CustomerId` INT(11) NOT NULL,
  PRIMARY KEY (`OrderId`),
  INDEX `CustomerId` (`CustomerId` ASC) VISIBLE,
  CONSTRAINT `orders_ibfk_1`
    FOREIGN KEY (`CustomerId`)
    REFERENCES `shop`.`customers` (`CustomerId`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shop`.`order_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shop`.`order_product` (
  `OrderId` INT(11) NOT NULL,
  `ProductId` INT(11) NOT NULL,
  `Amount` INT(11) NOT NULL,
  PRIMARY KEY (`OrderId`, `ProductId`),
  INDEX `ProductId` (`ProductId` ASC) VISIBLE,
  CONSTRAINT `order_product_ibfk_1`
    FOREIGN KEY (`OrderId`)
    REFERENCES `shop`.`orders` (`OrderId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `order_product_ibfk_2`
    FOREIGN KEY (`ProductId`)
    REFERENCES `shop`.`products` (`ProductId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO Roles VALUES (1, "admin");
INSERT INTO Roles VALUES (2, "user");
INSERT INTO Roles VALUES (3, "blocked");