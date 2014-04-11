--
--
-- Tables:
-- BattingStats
-- PitchingStats
-- CatchingStats
-- Player
-- Position
-- PlayerSeason
-- Season
-- Team
-- TeamSeason
-- TeamSeasonPlayer


CREATE TABLE Season (
	syear VARCHAR(20) PRIMARY KEY
);

CREATE TABLE Player (
	player_id VARCHAR(12),
	name VARCHAR(50),
	nickName VARCHAR(50),
	birthDay DATE,
	deathDay DATE,
	battingHand VARCHAR(5),
	throwingHand VARCHAR(5),
	birthCity VARCHAR(50),
	brithState VARCHAR(30),
	college VARCHAR(50),
	firstGame VARCHAR(20),
	lastGame  VARCHAR(20),
	PRIMARY KEY(player_id)
);


CREATE TABLE BattingStats (
	atBats INT,
	hits INT,
	doubles INT,
	triples INT,
	homeRuns INT,
	runsBattedIn INT,
	strikeouts INT,
	walks INT,
	hitByPitch INT,
	intentionWalks INT,
	steals INT,
	stealsAttempted INT,
	
	player_id VARCHAR(12),
	syear VARCHAR(20),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY (player_id, syear)
);


CREATE TABLE PitchingStats (
	outsPitched INT,
	earnedRunsAllowed INT,
	strikeouts INT,
	walks INT,
	wins INT,
	losses INT,
	wildPitches INT,
	battersFaced INT,
	hitBatters INT,
	saves INT,
	
	player_id VARCHAR(12),
	syear VARCHAR(20),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY (player_id, syear)
);


CREATE TABLE CatchingStats (
	passedBalls INT,
	wildPitches INT,
	stealsAllowed INT,
	stealsCaught INT,
	
	player_id VARCHAR(12),
	syear VARCHAR(20),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY (player_id, syear)
);


CREATE TABLE FieldingStats (
	errors INT,
	putOuts INT,
	
	player_id VARCHAR(12),
	syear VARCHAR(20),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY (player_id, syear)
);



CREATE TABLE Position (
	player_id VARCHAR(12),
	position VARCHAR(30),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	PRIMARY KEY(player_id)
);




CREATE TABLE PlayerSeason (
	player_id VARCHAR(12),
	syear VARCHAR(20),
	gamesPlayed INT,
	salary NUMERIC(12,2),
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY(player_id, syear)
);



CREATE TABLE Team (
	teamId VARCHAR(8),
	name VARCHAR(30),
	league VARCHAR(50),
	syearFounded date,
	syearLast date,
	PRIMARY KEY(teamId)
);


CREATE TABLE TeamSeason (
	teamId VARCHAR(8),
	syear VARCHAR(20),
	gamesPlayed INT,
	wins INT,
	losses INT,
	team_rank INT,
	totalAttendance INT,
	FOREIGN KEY(teamId) REFERENCES Team(teamId) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY(teamId, syear)
);


CREATE TABLE TeamSeasonPlayer (
	player_id VARCHAR(12),
	teamId VARCHAR(8),
	syear VARCHAR(20),
	FOREIGN KEY(teamId) REFERENCES Team(teamId) ON DELETE SET NULL,
	FOREIGN KEY(player_id) REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY(syear) REFERENCES Season(syear) ON DELETE SET NULL,
	PRIMARY KEY(player_id, teamId, syear)
);




















