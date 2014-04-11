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



CREATE TABLE IF NOT EXISTS BattingStats (
	atBats INT,
	hits INT,
	doubles INT,
	triples INT,
	homeRuns INT,
	runsBattedIn INT,
	strikeouts INT,
	walks INT,
	hitByPitch INT,
	INTentionWalks INT,
	steals INT,
	stealsAttempted INT,
	
	player_id VARCHAR(12),
	'year' VARCHAR(20),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY (player_id, 'year')
);


CREATE TABLE IF NOT EXISTS PitchingStats (
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
	'year' VARCHAR(20),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY (player_id, 'year')
);


CREATE TABLE IF NOT EXISTS CatchingStats (
	passedBalls INT,
	wildPitches INT,
	stealsAllowed INT,
	stealsCaught INT,
	
	player_id VARCHAR(12),
	'year' VARCHAR(20),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY (player_id, 'year')
);


CREATE TABLE IF NOT EXISTS FieldingStats (
	errors INT,
	putOuts INT,
	
	player_id VARCHAR(12),
	'year' VARCHAR(20),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY (player_id, 'year')
);


CREATE TABLE IF NOT EXISTS Player (
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


CREATE TABLE IF NOT EXISTS Position (
	player_id VARCHAR(12),
	position VARCHAR(30),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	PRIMARY KEY(player_id)
);




CREATE TABLE IF NOT EXISTS PlayerSeason (
	player_id VARCHAR(12),
	'year' VARCHAR(20),
	gamesPlayed INT,
	salary NUMERIC(12,2),
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY(player_id, 'year')
);




CREATE TABLE IF NOT EXISTS Season (
	'year' VARCHAR(20)  
);


CREATE TABLE IF NOT EXISTS Team (
	teamId VARCHAR(8),
	name VARCHAR(30),
	league VARCHAR(50),
	yearFounded date,
	yearLast date,
	PRIMARY KEY(teamId)
);


CREATE TABLE IF NOT EXISTS TeamSeason (
	teamId VARCHAR(8),
	'year' VARCHAR(20),
	gamesPlayed INT,
	wins INT,
	losses INT,
	'rank' INT,
	totalAttendance INT,
	FOREIGN KEY team_id REFERENCES Team(team_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY(teamId, 'year')
);


CREATE TABLE IF NOT EXISTS TeamSeasonPlayer (
	player_id (12),
	teamId VARCHAR(8),
	'year' VARCHAR(20),
	FOREIGN KEY team_id REFERENCES Team(team_id) ON DELETE SET NULL,
	FOREIGN KEY player_id REFERENCES Player(player_id) ON DELETE SET NULL,
	FOREIGN KEY 'year' REFERENCES Season('year') ON DELETE SET NULL,
	PRIMARY KEY(player_id, teamId, 'year')
);




















