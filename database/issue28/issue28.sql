-- -----------------------------------------------------
-- View `BEST_BOWLING`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BOWLING` ;
DROP TABLE IF EXISTS `BEST_BOWLING`;

CREATE  OR REPLACE VIEW `BEST_BOWLING` AS 
select bowlingid, playerid, wickets, runs from BOWLING b where wickets = 
(select max(wickets) from BOWLING x where x.playerid = b.playerid) and runs = (select min(runs) from BOWLING y where y.playerid = b.playerid and y.wickets = b.wickets) 
group by playerid order by wickets desc, runs asc;

-- -----------------------------------------------------
-- View `BEST_BATTING`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BATTING` ;
DROP TABLE IF EXISTS `BEST_BATTING`;

CREATE  OR REPLACE VIEW `BEST_BATTING` AS
select b.battingid, b.playerid, b.score, b.balls, if(b.howoutid in (1, 7, 13),0,1) as outstatus 
from BATTING b where b.score = 
(select max(score) from BATTING ba where ba.playerid = b.playerid) 
and b.balls = (select min(balls) from BATTING ba where ba.playerid = b.playerid and ba.score = b.score)
group by b.playerid;

-- -----------------------------------------------------
-- View `BEST_BOWLING_SEASONS`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BOWLING_SEASONS` ;
DROP TABLE IF EXISTS `BEST_BOWLING_SEASONS`;

CREATE  OR REPLACE VIEW `BEST_BOWLING_SEASONS` AS
select b.bowlingId, g.competitionId, b.playerid, wickets, runs from BOWLING b inner join INNINGS i natural join GAME g on b.inningsId = i.inningsId
where wickets = 
    (select max(wickets) from BOWLING bo inner join INNINGS inns natural join GAME ga 
    on bo.inningsId = inns.inningsId
    where bo.playerid = b.playerid and g.competitionId = ga.competitionId) 
and runs = 
    (select min(runs) from BOWLING bo inner join INNINGS inns natural join GAME ga 
    on bo.inningsId = inns.inningsId
    where bo.playerid = b.playerid and bo.wickets = b.wickets and g.competitionId = ga.competitionId) 
group by competitionid, playerid;

-- -----------------------------------------------------
-- View `BEST_BATTING_SEASONS`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `BEST_BATTING_SEASONS` ;
DROP TABLE IF EXISTS `BEST_BATTING_SEASONS`;

CREATE  OR REPLACE VIEW `BEST_BATTING_SEASONS` AS
select b.battingid, g.competitionId, b.playerid, b.score as score, b.balls as balls, 
if(b.howoutid in (1, 7, 13),0,1) as outstatus 
from BATTING b natural join INNINGS i natural join GAME g where b.score = 
    (select max(score) from BATTING ba natural join INNINGS inns natural join GAME ga 
    where ba.playerid = b.playerId and ga.competitionId = g.competitionId group by ga.competitionId)
and b.balls = 
    (select min(balls) from BATTING ba natural join INNINGS inns natural join GAME ga 
    where ba.playerid = b.playerId and b.score = ba.score and ga.competitionId = g.competitionId group by ga.competitionId)
group by competitionId, playerId;

UPDATE WIN_TYPE SET winTypeName = 'Runs' where winTypeId = 1;
UPDATE WIN_TYPE SET winTypeName = 'Wickets' where winTypeId = 2;

INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (1, 'Runs on First Innings');
INSERT INTO `WIN_TYPE` (`winTypeId`, `winTypeName`) VALUES (2, 'Wickets on First Innings');