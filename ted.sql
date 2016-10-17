-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 17. Okt 2016 um 16:09
-- Server-Version: 10.1.16-MariaDB
-- PHP-Version: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `inar`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ted`
--

CREATE TABLE `ted` (
  `text` int(11) NOT NULL,
  `sentence` int(11) NOT NULL,
  `positive` float NOT NULL,
  `negative` float NOT NULL,
  `anger` float NOT NULL,
  `anticipation` float NOT NULL,
  `disgust` float NOT NULL,
  `fear` float NOT NULL,
  `joy` float NOT NULL,
  `sadness` float NOT NULL,
  `surprise` float NOT NULL,
  `trust` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `ted`
--

INSERT INTO `ted` (`text`, `sentence`, `positive`, `negative`, `anger`, `anticipation`, `disgust`, `fear`, `joy`, `sadness`, `surprise`, `trust`) VALUES
(1, 1, 0.5, 0.4, 1, 0.1, 1, 0.5, 0.3, 0, 0.1, 1),
(1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
(2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `ted`
--
ALTER TABLE `ted`
  ADD PRIMARY KEY (`text`,`sentence`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
