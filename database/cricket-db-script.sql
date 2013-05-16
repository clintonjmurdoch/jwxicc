SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS 'JSF' DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci ;
USE 'JSF' ;

-- -----------------------------------------------------
-- Table 'JSF'.`GROUND`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`GROUND` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`GROUND` (
  `groundId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `groundName` VARCHAR(45) NOT NULL ,
  `streetAddress` VARCHAR(45) NULL ,
  `suburb` VARCHAR(45) NULL ,
  `mapRef` VARCHAR(45) NULL ,
  PRIMARY KEY (`groundId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`REVIEW`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`REVIEW` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`REVIEW` (
  `reviewId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `review_text` TEXT NOT NULL ,
  PRIMARY KEY (`reviewId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`COMPETITION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`COMPETITION` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`COMPETITION` (
  `competitionId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `associationName` VARCHAR(45) NOT NULL ,
  `grade` VARCHAR(45) NOT NULL ,
  `season` VARCHAR(9) NOT NULL ,
  `reviewId` INT UNSIGNED NULL ,
  PRIMARY KEY (`competitionId`) ,
  INDEX `competition_review` (`reviewId` ASC) ,
  CONSTRAINT `competition_review`
    FOREIGN KEY (`reviewId` )
    REFERENCES 'JSF'.`REVIEW` (`reviewId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`TEAM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`TEAM` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`TEAM` (
  `teamId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `teamName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`teamId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`WIN_TYPE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`WIN_TYPE` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`WIN_TYPE` (
  `winTypeId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `winTypeName` VARCHAR(45) NULL ,
  PRIMARY KEY (`winTypeId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`GAME`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`GAME` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`GAME` (
  `gameId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `competitionId` INT UNSIGNED NOT NULL ,
  `round` CHAR(3) NOT NULL ,
  `groundId` INT UNSIGNED NOT NULL ,
  `homeTeamId` INT UNSIGNED NOT NULL ,
  `awayTeamId` INT UNSIGNED NOT NULL ,
  `date` DATE NOT NULL ,
  `reviewId` INT UNSIGNED NULL ,
  `winner` ENUM('','HOME', 'AWAY') NULL ,
  `winTypeId` INT UNSIGNED NULL ,
  `winMargin` INT UNSIGNED NULL ,
  `marginType` ENUM('','wickets','runs') NULL ,
  `gameState` ENUM('Not Started', 'Incomplete', 'Completed') NULL ,
  `comment` TEXT NULL ,
  PRIMARY KEY (`gameId`) ,
  INDEX `game_ground` (`groundId` ASC) ,
  INDEX `game_competition` (`competitionId` ASC) ,
  INDEX `game_team_home` (`homeTeamId` ASC) ,
  INDEX `game_team_away` (`awayTeamId` ASC) ,
  INDEX `game_wintype` (`winTypeId` ASC) ,
  INDEX `game_review` (`reviewId` ASC) ,
  CONSTRAINT `game_ground`
    FOREIGN KEY (`groundId` )
    REFERENCES 'JSF'.`GROUND` (`groundId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_competition`
    FOREIGN KEY (`competitionId` )
    REFERENCES 'JSF'.`COMPETITION` (`competitionId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_team_home`
    FOREIGN KEY (`homeTeamId` )
    REFERENCES 'JSF'.`TEAM` (`teamId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_team_away`
    FOREIGN KEY (`awayTeamId` )
    REFERENCES 'JSF'.`TEAM` (`teamId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_wintype`
    FOREIGN KEY (`winTypeId` )
    REFERENCES 'JSF'.`WIN_TYPE` (`winTypeId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_review`
    FOREIGN KEY (`reviewId` )
    REFERENCES 'JSF'.`REVIEW` (`reviewId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`INNINGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`INNINGS` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`INNINGS` (
  `inningsId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `gameId` INT UNSIGNED NOT NULL ,
  `teamId` INT UNSIGNED NOT NULL ,
  `wicketsLost` INT UNSIGNED NULL ,
  `runsScored` INT UNSIGNED NULL ,
  `oversFaced` DECIMAL(4,1) UNSIGNED NULL ,
  `closureType` VARCHAR(12) NULL ,
  `inningsOfMatch` INT UNSIGNED NULL ,
  `no_balls` INT UNSIGNED NULL ,
  `wides` INT UNSIGNED NULL ,
  `leg_byes` INT UNSIGNED NULL ,
  `byes` INT UNSIGNED NULL ,
  `penalties` INT UNSIGNED NULL ,
  PRIMARY KEY (`inningsId`) ,
  INDEX `innings_team` (`teamId` ASC) ,
  INDEX `innings_game` (`gameId` ASC) ,
  CONSTRAINT `innings_team`
    FOREIGN KEY (`teamId` )
    REFERENCES 'JSF'.`TEAM` (`teamId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `innings_game`
    FOREIGN KEY (`gameId` )
    REFERENCES 'JSF'.`GAME` (`gameId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`PLAYER_DETAIL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`PLAYER_DETAIL` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`PLAYER_DETAIL` (
  `playerDetailId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `fullname` VARCHAR(45) NULL ,
  `birthdate` DATE NULL ,
  `birthplace` VARCHAR(45) NULL ,
  `nicknames` VARCHAR(90) NULL ,
  `batting_style` VARCHAR(45) NULL ,
  `bowling_style` VARCHAR(45) NULL ,
  `fielding_positions` VARCHAR(45) NULL ,
  `teams` VARCHAR(45) NULL ,
  `shirt_number` VARCHAR(45) NULL ,
  `cap_number` VARCHAR(45) NULL ,
  `image` BLOB NULL ,
  `profile` TEXT NULL ,
  PRIMARY KEY (`playerDetailId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`PLAYER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`PLAYER` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`PLAYER` (
  `playerId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `playerName` VARCHAR(45) NULL ,
  `scorecardName` VARCHAR(45) NULL ,
  `teamId` INT UNSIGNED NOT NULL ,
  `status` TINYINT(1)  NOT NULL DEFAULT 1 ,
  `playerDetailId` INT UNSIGNED NULL ,
  PRIMARY KEY (`playerId`) ,
  INDEX `player_team` (`teamId` ASC) ,
  UNIQUE INDEX `playerDetailId_UNIQUE` (`playerDetailId` ASC) ,
  INDEX `detailed_player` (`playerDetailId` ASC) ,
  CONSTRAINT `player_team`
    FOREIGN KEY (`teamId` )
    REFERENCES 'JSF'.`TEAM` (`teamId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `detailed_player`
    FOREIGN KEY (`playerDetailId` )
    REFERENCES 'JSF'.`PLAYER_DETAIL` (`playerDetailId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`HOWOUT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`HOWOUT` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`HOWOUT` (
  `howOutid` INT UNSIGNED NOT NULL ,
  `dismissalShort` VARCHAR(15) NOT NULL ,
  `dismissalType` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`howOutid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`BATTING`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`BATTING` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`BATTING` (
  `battingId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `playerId` INT UNSIGNED NOT NULL ,
  `battingPos` INT UNSIGNED NULL ,
  `score` INT UNSIGNED NULL ,
  `balls` INT UNSIGNED NULL ,
  `fours` INT UNSIGNED NULL ,
  `sixes` INT UNSIGNED NULL ,
  `howOutId` INT UNSIGNED NULL ,
  `inningsId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`battingId`) ,
  INDEX `batting_innings` (`inningsId` ASC) ,
  INDEX `batting_player` (`playerId` ASC) ,
  INDEX `batting_howout` (`howOutId` ASC) ,
  CONSTRAINT `batting_innings`
    FOREIGN KEY (`inningsId` )
    REFERENCES 'JSF'.`INNINGS` (`inningsId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `batting_player`
    FOREIGN KEY (`playerId` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `batting_howout`
    FOREIGN KEY (`howOutId` )
    REFERENCES 'JSF'.`HOWOUT` (`howOutid` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`WICKET_DETAIL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`WICKET_DETAIL` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`WICKET_DETAIL` (
  `wicketDetailId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `battingId` INT UNSIGNED NOT NULL ,
  `wicketType` CHAR(5) NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`wicketDetailId`) ,
  INDEX `wicketdetail_batting` (`battingId` ASC) ,
  INDEX `wicketdetail_player` (`playerId` ASC) ,
  CONSTRAINT `wicketdetail_batting`
    FOREIGN KEY (`battingId` )
    REFERENCES 'JSF'.`BATTING` (`battingId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `wicketdetail_player`
    FOREIGN KEY (`playerId` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`BOWLING`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`BOWLING` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`BOWLING` (
  `bowlingId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `playerId` INT UNSIGNED NOT NULL ,
  `bowlingPos` INT UNSIGNED NULL ,
  `overs` DECIMAL(4,1) UNSIGNED NULL ,
  `maidens` INT UNSIGNED NULL ,
  `runs` INT UNSIGNED NULL ,
  `wickets` INT UNSIGNED NULL ,
  `noBalls` INT UNSIGNED NULL ,
  `wides` INT UNSIGNED NULL ,
  `inningsId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`bowlingId`) ,
  INDEX `bowling_player` (`playerId` ASC) ,
  INDEX `bowling_innings` (`inningsId` ASC) ,
  CONSTRAINT `bowling_player`
    FOREIGN KEY (`playerId` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `bowling_innings`
    FOREIGN KEY (`inningsId` )
    REFERENCES 'JSF'.`INNINGS` (`inningsId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`FOW`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`FOW` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`FOW` (
  `fowId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `inningsId` INT UNSIGNED NOT NULL ,
  `wicket` INT UNSIGNED NOT NULL ,
  `score` INT UNSIGNED NOT NULL ,
  `overs` FLOAT(3,1) NOT NULL ,
  PRIMARY KEY (`fowId`) ,
  INDEX `fow_innings` (`inningsId` ASC) ,
  CONSTRAINT `fow_innings`
    FOREIGN KEY (`inningsId` )
    REFERENCES 'JSF'.`INNINGS` (`inningsId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`FOW_WICKET`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`FOW_WICKET` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`FOW_WICKET` (
  `fow_wicketId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `fowId` INT UNSIGNED NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  `outStatus` TINYINT(1)  NOT NULL ,
  `contribution` INT UNSIGNED NULL ,
  PRIMARY KEY (`fow_wicketId`) ,
  INDEX `fow_wicket_fow` (`fowId` ASC) ,
  INDEX `fow_wicket_player` (`playerId` ASC) ,
  CONSTRAINT `fow_wicket_fow`
    FOREIGN KEY (`fowId` )
    REFERENCES 'JSF'.`FOW` (`fowId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fow_wicket_player`
    FOREIGN KEY (`playerId` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`USER` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`USER` (
  `username` VARCHAR(20) NOT NULL ,
  `password` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`username`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`NEWSITEM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`NEWSITEM` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`NEWSITEM` (
  `newsid` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(100) NOT NULL ,
  `content` TEXT NOT NULL ,
  `timestamp` TIMESTAMP NOT NULL ,
  `poster` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`newsid`) ,
  INDEX `poster` (`poster` ASC) ,
  CONSTRAINT `newsitem_user`
    FOREIGN KEY (`poster` )
    REFERENCES 'JSF'.`USER` (`username` ))
ENGINE = InnoDB
AUTO_INCREMENT = 45;


-- -----------------------------------------------------
-- Table 'JSF'.`GAME_PLAYER_DESIGNATION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`GAME_PLAYER_DESIGNATION` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`GAME_PLAYER_DESIGNATION` (
  `designationId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `designationType` VARCHAR(15) NOT NULL ,
  `gameId` INT UNSIGNED NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`designationId`) ,
  INDEX `gpd_game` (`gameId` ASC) ,
  INDEX `gpd_player` (`playerId` ASC) ,
  CONSTRAINT `gpd_game`
    FOREIGN KEY (`gameId` )
    REFERENCES 'JSF'.`GAME` (`gameId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gpd_player`
    FOREIGN KEY (`playerId` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table 'JSF'.`PLAYER_RELATIONSHIP`
-- -----------------------------------------------------
DROP TABLE IF EXISTS 'JSF'.`PLAYER_RELATIONSHIP` ;

CREATE  TABLE IF NOT EXISTS 'JSF'.`PLAYER_RELATIONSHIP` (
  `relationshipId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `owningPlayer` INT UNSIGNED NOT NULL ,
  `otherPlayer` INT UNSIGNED NOT NULL ,
  `relationshipType` VARCHAR(45) NULL ,
  PRIMARY KEY (`relationshipId`) ,
  INDEX `owning_player_relationship` (`owningPlayer` ASC) ,
  INDEX `other_player_reference` (`otherPlayer` ASC) ,
  CONSTRAINT `owning_player_relationship`
    FOREIGN KEY (`owningPlayer` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `other_player_reference`
    FOREIGN KEY (`otherPlayer` )
    REFERENCES 'JSF'.`PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Placeholder table for view 'JSF'.`BEST_BOWLING`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS 'JSF'.`BEST_BOWLING` (`scorecardname` INT, `playerid` INT, `wickets` INT, `runs` INT);

-- -----------------------------------------------------
-- View 'JSF'.`BEST_BOWLING`
-- -----------------------------------------------------
DROP VIEW IF EXISTS 'JSF'.`BEST_BOWLING` ;
DROP TABLE IF EXISTS 'JSF'.`BEST_BOWLING`;
USE 'JSF';
CREATE  OR REPLACE VIEW 'JSF'.`BEST_BOWLING` AS 
select scorecardname, playerid, wickets, runs from BOWLING b natural join PLAYER p where wickets = 
(select max(wickets) from BOWLING x where x.playerid = p.playerid) and runs = (select min(runs) from BOWLING y where y.playerid = p.playerid and y.wickets = b.wickets) 
group by playerid order by wickets desc, runs asc;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table 'JSF'.`WIN_TYPE`
-- -----------------------------------------------------
START TRANSACTION;
USE 'JSF';
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (1, 'Runs on First Innings');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (2, 'Wickets on First Innings');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (3, 'Draw on First Innings');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (4, 'Washed Out');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (5, 'Tie on First Innings');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (6, 'Runs on Second Innings after First Innings Lead');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (7, 'Wickets on Second Innings after First Innings Lead');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (8, 'Runs on Second Innings after First Innings Deficit');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (9, 'Wickets on Second Innings after First Innings Deficit');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (10, 'Draw on Second Innings');
INSERT INTO 'JSF'.`WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (11, 'Tie on Second Innings');

COMMIT;

-- -----------------------------------------------------
-- Data for table 'JSF'.`HOWOUT`
-- -----------------------------------------------------
START TRANSACTION;
USE 'JSF';
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (1, 'no', 'not out');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (2, 'b', 'bowled');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (3, 'c', 'caught');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (6, 'lbw', 'lbw');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (18, 'ct wk', 'caught behind');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (8, 'run out', 'run out');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (7, 'ret hurt', 'retired hurt');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (12, 'ret out', 'retired out');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (16, 'abs hurt', 'absent hurt');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (14, 'timed out', 'timed out');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (0, 'dnb', 'did not bat');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (4, 'c&b', 'caught & bowled');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (5, 'hit wkt', 'hit wicket');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (9, 'st', 'stumped');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (10, 'obs', 'obstructed field');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (11, 'hand. ball', 'handled ball');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (13, 'ret no', 'retired not out');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (15, 'hit ball twice', 'hit ball twice');
INSERT INTO 'JSF'.`HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (17, 'abs ill', 'absent ill');

COMMIT;

-- -----------------------------------------------------
-- Data for table 'JSF'.`USER`
-- -----------------------------------------------------
START TRANSACTION;
USE 'JSF';
INSERT INTO 'JSF'.`USER` (`username`, `password`) VALUES ('murdoch', 'cricket');
INSERT INTO 'JSF'.`USER` (`username`, `password`) VALUES ('vorn', 'a1b2c3');

COMMIT;