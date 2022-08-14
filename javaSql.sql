--use master 
--go
-- alter database javaDB set single_user with rollback immediate

-- drop database javaDB

USE master
GO

CREATE DATABASE javaDB
GO

USE javaDB
GO

CREATE TABLE Genre
(
	IDGenre INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(500)
)
GO

CREATE TABLE [Role]
(
	IDRole INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(100) NOT NULL
)
GO

CREATE TABLE Artist
(
	IDArtist INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(500),
	RoleID INT FOREIGN KEY REFERENCES [Role](IDRole)
)
GO

CREATE TABLE Movie
(
	IDMovie INT PRIMARY KEY IDENTITY,
	Title NVARCHAR(500),
	Description NVARCHAR(2000),
	PublishedDate DATETIME,
	Duration INT,
	PosterURL NVARCHAR(2000),
	BeginDate NVARCHAR(50),
)
GO

CREATE TABLE MovieGenre
(
	IDMovieGenre INT PRIMARY KEY IDENTITY,
	GenreID INT FOREIGN KEY REFERENCES Genre(IDGenre),
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie)
)
GO

CREATE TABLE MovieArtists
(
	IDMovieArtist INT PRIMARY KEY IDENTITY,
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie),
	ArtistID INT FOREIGN KEY REFERENCES Artist(IDArtist),
)
GO

CREATE TABLE UserRoles
(
	IDRole INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(100)
)
GO

CREATE TABLE [User]
(
	IDUser INT PRIMARY KEY IDENTITY,
	Username NVARCHAR(100),
	Password NVARCHAR(100),
	RoleID INT FOREIGN KEY REFERENCES UserRoles(IDRole)
)
GO

---------------------------------------------
---	PROCEDURES

-- GENRE CRUD

CREATE OR ALTER PROC CreateGenre
@name NVARCHAR(500), @IDGenre INT OUTPUT
AS
INSERT INTO Genre([Name])
VALUES (@name)
SET @IDGenre = SCOPE_IDENTITY()
GO

CREATE OR ALTER PROC ReadGenres
AS
SELECT * FROM Genre
GO

CREATE OR ALTER PROC ReadGenre
@IDGenre INT
AS
SELECT * FROM Genre WHERE IDGenre = @IDGenre
GO

CREATE OR ALTER PROC UpdateGenre
@IDGenre INT, @name NVARCHAR(500)
AS
UPDATE Genre
SET Name = @name
WHERE IDGenre = @IDGenre
GO

CREATE OR ALTER PROC DeleteGenre
@IDGenre INT
AS
DELETE FROM MovieGenre
WHERE GenreID = @IDGenre

DELETE FROM Genre
WHERE IDGenre = @IDGenre
GO

-- Artist CRUD

CREATE OR ALTER PROC CreateArtist
@name NVARCHAR(500), @IDRole INT, @IDArtist INT OUTPUT
AS
INSERT INTO Artist([Name], [RoleID])
VALUES (@name, @IDRole)
SET @IDArtist = SCOPE_IDENTITY()
GO

CREATE OR ALTER PROC ReadArtists
AS
SELECT IDArtist, Name, RoleID AS IDRole FROM Artist
GO

CREATE OR ALTER PROC ReadArtist
@IDArtist INT
AS
SELECT IDArtist, Name, RoleID AS IDRole FROM Artist
WHERE IDArtist = @IDArtist
GO

CREATE OR ALTER PROC UpdateArtist
@IDArtist INT, @name NVARCHAR(500)
AS
UPDATE Artist
SET Name = @name
WHERE IDArtist = @IDArtist
GO

CREATE OR ALTER PROC DeleteArtist
@IDArtist INT
AS
DELETE FROM MovieArtists
WHERE ArtistID = @IDArtist

DELETE FROM Artist
WHERE IDArtist = @IDArtist
GO

INSERT INTO Role([Name])
VALUES ('Glumac'), ('Redatelj')
GO

CREATE OR ALTER PROC ReadRole
@IDRole INT
AS
SELECT IDRole, Name FROM Role WHERE IDRole = @IDRole
GO

-- USER CRUD

INSERT INTO UserRoles([Name])
VALUES ('admin'), ('user')
GO

CREATE OR ALTER PROC RegisterUser
@username NVARCHAR(100), @password NVARCHAR(100)
AS
INSERT INTO [User]([Username], [Password], [RoleID])
VALUES (@username, @password, 2)
GO

CREATE OR ALTER PROC AuthUser
@username NVARCHAR(100), @password NVARCHAR(100)
AS
SELECT * FROM [User]
WHERE Username = @username AND Password = @password
GO

-- MOVIE CRUD

-- MOVIE -> ARTISTS
-- MOVIE -> GENRES

CREATE OR ALTER PROC CreateMovie
@Title NVARCHAR(500), @Description NVARCHAR(2000),
@PublishedDate DATETIME, @Duration INT, @PosterURL NVARCHAR(2000),
@BeginDate NVARCHAR(50), @IDMovie INT OUTPUT
AS
INSERT INTO Movie([BeginDate], [Description], [Duration], [PosterURL], [PublishedDate], [Title])
VALUES (@beginDate, @description, @duration, @PosterURL ,@PublishedDate, @title)
SET @IDMovie = SCOPE_IDENTITY()
GO

CREATE OR ALTER PROC AddArtistForMovie
@IDMovie INT, @IDArtist INT
AS
INSERT INTO MovieArtists([ArtistID], [MovieID])
VALUES(@IDArtist, @IDMovie)
GO

CREATE OR ALTER PROC AddGenreForMovie
@IDMovie INT, @IDGenre INT
AS
INSERT INTO MovieGenre([GenreID], [MovieID])
VALUES(@IDGenre, @IDMovie)
GO

CREATE OR ALTER PROC UpdateMovie
@IDMovie INT,
@title NVARCHAR(500), @description NVARCHAR(2000),
@PublishedDate DATETIME, @duration INT, @PosterURL NVARCHAR(2000),
@beginDate NVARCHAR(50)
AS
UPDATE Movie
SET Title = @title, Description = @description, PublishedDate = @PublishedDate, Duration = @duration, PosterURL = @PosterURL, BeginDate = @beginDate
WHERE IDMovie = @IDMovie
GO

CREATE OR ALTER PROC DeleteGenreForMovie
@IDMovie INT, @IDGenre INT
AS
DELETE FROM MovieGenre
WHERE MovieID = @IDMovie AND GenreID = @IDGenre
GO

CREATE OR ALTER PROC DeleteArtistForMovie
@IDMovie INT, @IDArtist INT
AS
DELETE FROM MovieArtists
WHERE MovieID = @IDMovie AND ArtistID = @IDArtist
GO

CREATE OR ALTER PROC ReadGenresForMovie
@IDMovie INT
AS
SELECT g.IDGenre, g.Name
FROM MovieGenre 
INNER JOIN Genre AS g ON MovieGenre.GenreID = g.IDGenre
WHERE MovieID = @IDMovie
GO

CREATE OR ALTER PROC ReadArtistForMovie
@IDMovie INT
AS
SELECT a.IDArtist, a.Name, r.Name AS Role, r.IDRole FROM MovieArtists 
INNER JOIN Artist AS a ON MovieArtists.ArtistID = a.IDArtist
INNER JOIN Role AS r ON a.RoleID = r.IDRole
WHERE MovieID = @IDMovie
GO

CREATE OR ALTER PROC DeleteMovie
@IDMovie INT
AS
DELETE FROM MovieGenre
WHERE MovieID = @IDMovie

DELETE FROM MovieArtists
WHERE MovieID = @IDMovie

DELETE FROM Movie
WHERE IDMovie = @IDMovie
GO
CREATE OR ALTER PROC DeleteAllMovies
AS
DELETE FROM MovieArtists
DELETE FROM Artist
DELETE FROM MovieGenre
DELETE FROM Genre
DELETE FROM Movie
GO

CREATE OR ALTER PROC ReadMovie
@IDMovie INT
AS
SELECT * FROM Movie
WHERE IDMovie = @IDMovie
GO

CREATE OR ALTER PROC ReadMovies
AS
SELECT * FROM Movie
GO

CREATE OR ALTER PROC FindArtist
@Name NVARCHAR(200), @IDRole INT
AS
SELECT IDArtist, Name, RoleID AS IDRole
FROM Artist WHERE Name = @Name AND RoleID = @IDRole
GO

CREATE OR ALTER PROC FindGenre
@Name NVARCHAR(50)
AS
SELECT * FROM Genre WHERE Name = @Name
GO