-- drop for testing so any new app contexts that are spawned will be fresh
DROP TABLE IF EXISTS `Contacts`;

CREATE TABLE `Contacts` (
	`id` INT AUTO_INCREMENT,
    `firstName` VARCHAR(30) NOT NULL,
    `lastName` VARCHAR(30) NOT NULL,
    `mobileNumber` VARCHAR(11) NOT NULL,
    `emailAddress` VARCHAR(50) NOT NULL,
    `dateOfBirth` DATE NOT NULL,

    PRIMARY KEY(`id`),

    CHECK(`firstName` <> ''),
    CHECK(`lastName` <> ''),
    CHECK(`mobileNumber` <> ''),
    CHECK(`emailAddress` <> ''),
    CHECK(LENGTH(`mobileNumber`) = 11),
    CHECK(LOCATE('@', `emailAddress`) > 0)
);