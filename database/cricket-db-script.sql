SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

-- -----------------------------------------------------
-- Table `GROUND`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GROUND` ;

CREATE  TABLE IF NOT EXISTS `GROUND` (
  `groundId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `groundName` VARCHAR(45) NOT NULL ,
  `streetAddress` VARCHAR(45) NULL ,
  `suburb` VARCHAR(45) NULL ,
  `mapRef` VARCHAR(45) NULL ,
  PRIMARY KEY (`groundId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `REVIEW`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `REVIEW` ;

CREATE  TABLE IF NOT EXISTS `REVIEW` (
  `reviewId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `review_text` TEXT NOT NULL ,
  PRIMARY KEY (`reviewId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMPETITION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `COMPETITION` ;

CREATE  TABLE IF NOT EXISTS `COMPETITION` (
  `competitionId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `associationName` VARCHAR(45) NOT NULL ,
  `grade` VARCHAR(45) NOT NULL ,
  `season` VARCHAR(9) NOT NULL ,
  `reviewId` INT UNSIGNED NULL ,
  PRIMARY KEY (`competitionId`) ,
  INDEX `competition_review` (`reviewId` ASC) ,
  CONSTRAINT `competition_review`
    FOREIGN KEY (`reviewId` )
    REFERENCES `REVIEW` (`reviewId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TEAM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `TEAM` ;

CREATE  TABLE IF NOT EXISTS `TEAM` (
  `teamId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `teamName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`teamId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WIN_TYPE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `WIN_TYPE` ;

CREATE  TABLE IF NOT EXISTS `WIN_TYPE` (
  `winTypeId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `winTypeName` VARCHAR(45) NULL ,
  PRIMARY KEY (`winTypeId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GAME`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GAME` ;

CREATE  TABLE IF NOT EXISTS `GAME` (
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
    REFERENCES `GROUND` (`groundId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_competition`
    FOREIGN KEY (`competitionId` )
    REFERENCES `COMPETITION` (`competitionId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_team_home`
    FOREIGN KEY (`homeTeamId` )
    REFERENCES `TEAM` (`teamId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_team_away`
    FOREIGN KEY (`awayTeamId` )
    REFERENCES `TEAM` (`teamId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_wintype`
    FOREIGN KEY (`winTypeId` )
    REFERENCES `WIN_TYPE` (`winTypeId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `game_review`
    FOREIGN KEY (`reviewId` )
    REFERENCES `REVIEW` (`reviewId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INNINGS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `INNINGS` ;

CREATE  TABLE IF NOT EXISTS `INNINGS` (
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
    REFERENCES `TEAM` (`teamId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `innings_game`
    FOREIGN KEY (`gameId` )
    REFERENCES `GAME` (`gameId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PLAYER_DETAIL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PLAYER_DETAIL` ;

CREATE  TABLE IF NOT EXISTS `PLAYER_DETAIL` (
  `playerDetailId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `fullname` VARCHAR(45) NULL ,
  `birthdate` DATE NULL ,
  `birthplace` VARCHAR(45) NULL ,
  `nicknames` VARCHAR(90) NULL ,
  `batting_style` VARCHAR(45) NULL ,
  `bowling_style` VARCHAR(45) NULL ,
  `fielding_positions` VARCHAR(45) NULL ,
  `teams` VARCHAR(45) NULL ,
  `shirt_number` INT UNSIGNED NULL ,
  `cap_number` INT UNSIGNED NULL ,
  `image` BLOB NULL ,
  `profile` TEXT NULL ,
  PRIMARY KEY (`playerDetailId`) ,
  UNIQUE INDEX `cap_number_UNIQUE` (`cap_number` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PLAYER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PLAYER` ;

CREATE  TABLE IF NOT EXISTS `PLAYER` (
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
    REFERENCES `TEAM` (`teamId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `detailed_player`
    FOREIGN KEY (`playerDetailId` )
    REFERENCES `PLAYER_DETAIL` (`playerDetailId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HOWOUT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HOWOUT` ;

CREATE  TABLE IF NOT EXISTS `HOWOUT` (
  `howOutid` INT UNSIGNED NOT NULL ,
  `dismissalShort` VARCHAR(15) NOT NULL ,
  `dismissalType` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`howOutid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BATTING`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BATTING` ;

CREATE  TABLE IF NOT EXISTS `BATTING` (
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
    REFERENCES `INNINGS` (`inningsId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `batting_player`
    FOREIGN KEY (`playerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `batting_howout`
    FOREIGN KEY (`howOutId` )
    REFERENCES `HOWOUT` (`howOutid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WICKET_DETAIL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `WICKET_DETAIL` ;

CREATE  TABLE IF NOT EXISTS `WICKET_DETAIL` (
  `wicketDetailId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `battingId` INT UNSIGNED NOT NULL ,
  `wicketType` CHAR(5) NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`wicketDetailId`) ,
  INDEX `wicketdetail_batting` (`battingId` ASC) ,
  INDEX `wicketdetail_player` (`playerId` ASC) ,
  CONSTRAINT `wicketdetail_batting`
    FOREIGN KEY (`battingId` )
    REFERENCES `BATTING` (`battingId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `wicketdetail_player`
    FOREIGN KEY (`playerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BOWLING`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BOWLING` ;

CREATE  TABLE IF NOT EXISTS `BOWLING` (
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
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bowling_innings`
    FOREIGN KEY (`inningsId` )
    REFERENCES `INNINGS` (`inningsId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PARTNERSHIP`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PARTNERSHIP` ;

CREATE  TABLE IF NOT EXISTS `PARTNERSHIP` (
  `partnershipId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `inningsId` INT UNSIGNED NOT NULL ,
  `wicket` INT UNSIGNED NOT NULL ,
  `scoreAtEnd` INT UNSIGNED NOT NULL ,
  `oversAtEnd` FLOAT(3,1) NOT NULL ,
  `runsScored` INT NULL ,
  `overs` FLOAT(3,1) NULL ,
  PRIMARY KEY (`partnershipId`) ,
  INDEX `partnership_innings` (`inningsId` ASC) ,
  CONSTRAINT `partnership_innings`
    FOREIGN KEY (`inningsId` )
    REFERENCES `INNINGS` (`inningsId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PARTNERSHIP_PLAYER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PARTNERSHIP_PLAYER` ;

CREATE  TABLE IF NOT EXISTS `PARTNERSHIP_PLAYER` (
  `partnershipPlayerId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `partnershipId` INT UNSIGNED NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  `outStatus` TINYINT(1)  NOT NULL ,
  `contribution` INT UNSIGNED NULL ,
  `battingPosition` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`partnershipPlayerId`) ,
  INDEX `partnershipplayer_partnership_rel` (`partnershipId` ASC) ,
  INDEX `partnershipplayer_player_rel` (`playerId` ASC) ,
  CONSTRAINT `partnershipplayer_partnership_rel`
    FOREIGN KEY (`partnershipId` )
    REFERENCES `PARTNERSHIP` (`partnershipId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `partnershipplayer_player_rel`
    FOREIGN KEY (`playerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `USER` ;

CREATE  TABLE IF NOT EXISTS `USER` (
  `username` VARCHAR(20) NOT NULL ,
  `password` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`username`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NEWSITEM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NEWSITEM` ;

CREATE  TABLE IF NOT EXISTS `NEWSITEM` (
  `newsid` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(100) NOT NULL ,
  `content` TEXT NOT NULL ,
  `timestamp` TIMESTAMP NOT NULL ,
  `poster` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`newsid`) ,
  INDEX `poster` (`poster` ASC) ,
  CONSTRAINT `newsitem_user`
    FOREIGN KEY (`poster` )
    REFERENCES `USER` (`username` ))
ENGINE = InnoDB
AUTO_INCREMENT = 45;


-- -----------------------------------------------------
-- Table `GAME_PLAYER_DESIGNATION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GAME_PLAYER_DESIGNATION` ;

CREATE  TABLE IF NOT EXISTS `GAME_PLAYER_DESIGNATION` (
  `designationId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `designationType` VARCHAR(15) NOT NULL ,
  `gameId` INT UNSIGNED NOT NULL ,
  `playerId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`designationId`) ,
  INDEX `gpd_game` (`gameId` ASC) ,
  INDEX `gpd_player` (`playerId` ASC) ,
  CONSTRAINT `gpd_game`
    FOREIGN KEY (`gameId` )
    REFERENCES `GAME` (`gameId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `gpd_player`
    FOREIGN KEY (`playerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PLAYER_RELATIONSHIP`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PLAYER_RELATIONSHIP` ;

CREATE  TABLE IF NOT EXISTS `PLAYER_RELATIONSHIP` (
  `relationshipId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `owningPlayerId` INT UNSIGNED NOT NULL ,
  `otherPlayerId` INT UNSIGNED NOT NULL ,
  `ownerViewOfRelationship` VARCHAR(15) NOT NULL ,
  PRIMARY KEY (`relationshipId`) ,
  INDEX `owning_player_relationship` (`owningPlayerId` ASC) ,
  INDEX `other_player_reference` (`otherPlayerId` ASC) ,
  CONSTRAINT `owning_player_relationship`
    FOREIGN KEY (`owningPlayerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `other_player_reference`
    FOREIGN KEY (`otherPlayerId` )
    REFERENCES `PLAYER` (`playerId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Placeholder table for view `BEST_BOWLING`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BEST_BOWLING` (`scorecardname` INT, `playerid` INT, `wickets` INT, `runs` INT);

-- -----------------------------------------------------
-- Placeholder table for view `BEST_BATTING`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BEST_BATTING` (`battingid` INT, `playerid` INT, `score` INT, `balls` INT, `outstatus` INT);

-- -----------------------------------------------------
-- View `BEST_BOWLING`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BOWLING` ;
DROP TABLE IF EXISTS `BEST_BOWLING`;

CREATE  OR REPLACE VIEW `BEST_BOWLING` AS 
select scorecardname, playerid, wickets, runs from BOWLING b natural join PLAYER p where wickets = 
(select max(wickets) from BOWLING x where x.playerid = p.playerid) and runs = (select min(runs) from BOWLING y where y.playerid = p.playerid and y.wickets = b.wickets) 
group by playerid order by wickets desc, runs asc;

-- -----------------------------------------------------
-- View `BEST_BATTING`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BATTING` ;
DROP TABLE IF EXISTS `BEST_BATTING`;

CREATE  OR REPLACE VIEW `BEST_BATTING` AS
select b.battingid, b.playerid, b.score, b.balls, if(b.howoutid in (1, 7, 13),0,1) as outstatus 
from BATTING b natural join PLAYER p where b.score = 
(select score from BATTING ba where ba.playerid = p.playerid order by ba.score desc, ba.balls asc limit 1) 
and p.teamid = 2 group by p.playerid;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `WIN_TYPE`
-- -----------------------------------------------------
START TRANSACTION;

INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (1, 'Runs on First Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (2, 'Wickets on First Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (3, 'Draw on First Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (4, 'Washed Out');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (5, 'Tie on First Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (6, 'Runs on Second Innings after First Innings Lead');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (7, 'Wickets on Second Innings after First Innings Lead');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (8, 'Runs on Second Innings after First Innings Deficit');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (9, 'Wickets on Second Innings after First Innings Deficit');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (10, 'Draw on Second Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (11, 'Tie on Second Innings');

COMMIT;

-- -----------------------------------------------------
-- Data for table `HOWOUT`
-- -----------------------------------------------------
START TRANSACTION;

INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (1, 'no', 'not out');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (2, 'b', 'bowled');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (3, 'c', 'caught');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (6, 'lbw', 'lbw');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (18, 'ct wk', 'caught behind');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (8, 'run out', 'run out');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (7, 'ret hurt', 'retired hurt');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (12, 'ret out', 'retired out');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (16, 'abs hurt', 'absent hurt');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (14, 'timed out', 'timed out');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (0, 'dnb', 'did not bat');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (4, 'c&b', 'caught & bowled');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (5, 'hit wkt', 'hit wicket');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (9, 'st', 'stumped');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (10, 'obs', 'obstructed field');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (11, 'hand. ball', 'handled ball');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (13, 'ret no', 'retired not out');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (15, 'hit ball twice', 'hit ball twice');
INSERT INTO `HOWOUT` (`howOutid`, `dismissalShort`, `dismissalType`) VALUES (17, 'abs ill', 'absent ill');

COMMIT;

-- -----------------------------------------------------
-- Data for table `USER`
-- -----------------------------------------------------
START TRANSACTION;

INSERT INTO `USER` (`username`, `password`) VALUES ('murdoch', 'cricket');
INSERT INTO `USER` (`username`, `password`) VALUES ('vorn', 'a1b2c3');

COMMIT;
