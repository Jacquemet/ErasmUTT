-- phpMyAdmin SQL Dump
-- version 3.4.10.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le : Ven 22 Novembre 2013 à 11:14
-- Version du serveur: 5.5.20
-- Version de PHP: 5.3.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `erasmutt`
--

-- --------------------------------------------------------

--
-- Structure de la table `activities`
--

CREATE TABLE IF NOT EXISTS `activities` (
  `idActivity` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(125) NOT NULL,
  `descriptionActivity` longtext NOT NULL,
  `pictureActivity` varchar(125) NOT NULL,
  `averageMark` float NOT NULL,
  `longitude` varchar(125) NOT NULL,
  `latitude` varchar(125) NOT NULL,
  `website` varchar(250) NOT NULL,
  `focusOn` tinyint(1) NOT NULL,
  PRIMARY KEY (`idActivity`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `activitiesgroups`
--

CREATE TABLE IF NOT EXISTS `activitiesgroups` (
  `idGroup` int(11) NOT NULL,
  `idActivity` int(11) NOT NULL,
  PRIMARY KEY (`idGroup`,`idActivity`),
  KEY `idActivity` (`idActivity`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `idGroup` int(11) NOT NULL,
  `name` varchar(125) NOT NULL,
  `descriptionGroup` longtext NOT NULL,
  `pictureGroup` varchar(250) NOT NULL,
  PRIMARY KEY (`idGroup`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `idUserSender` int(11) NOT NULL,
  `idUserReceiver` int(11) NOT NULL,
  `message` longtext NOT NULL,
  `date` datetime NOT NULL,
  `read` tinyint(1) NOT NULL,
  PRIMARY KEY (`idMessage`),
  KEY `idUserSender` (`idUserSender`),
  KEY `idUserReceiver` (`idUserReceiver`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `review`
--

CREATE TABLE IF NOT EXISTS `review` (
  `idReview` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) NOT NULL,
  `idActivity` int(11) NOT NULL,
  `title` varchar(125) NOT NULL,
  `description` longtext NOT NULL,
  `mark` float NOT NULL,
  `date` datetime NOT NULL,
  `language` varchar(10) NOT NULL,
  PRIMARY KEY (`idReview`),
  KEY `idUser` (`idUser`),
  KEY `idActivity` (`idActivity`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(125) NOT NULL,
  `lastname` varchar(125) NOT NULL,
  `password` varchar(125) NOT NULL,
  `pictureUser` varchar(250) NOT NULL,
  `mail` varchar(125) NOT NULL,
  `banned` tinyint(1) NOT NULL,
  `lastConnection` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(200) DEFAULT NULL,
  `nbTentative` int(11) DEFAULT NULL,
  `BannedDate` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`idUser`, `firstname`, `lastname`, `password`, `pictureUser`, `mail`, `banned`, `lastConnection`, `token`, `nbTentative`, `BannedDate`) VALUES
(1, 'kevin', 'larue', 'd6763922db5362caeeb10feb2e59ebcb', '', 'larue.kev@gmail.com', 0, '2013-11-20 18:41:46', NULL, 0, '0'),
(2, 'thibault', 'jacquemet', '428cd39893faad9c041bea63855f83d4', '', 'thibault.jacquemet@utt.fr', 0, '2013-11-20 18:46:42', NULL, 0, '0'),
(5, 'cecile', 'rosetttes', 'cc6d58e30f462f3918996126a5325519', 'www.google.fr', 'cecile.roset@gmail.com', 0, '2013-11-22 11:12:16', '528f3c10bc3aa', 0, '1385118433');

-- --------------------------------------------------------

--
-- Structure de la table `usersgroups`
--

CREATE TABLE IF NOT EXISTS `usersgroups` (
  `idUser` int(11) NOT NULL,
  `idGroup` int(11) NOT NULL,
  PRIMARY KEY (`idUser`,`idGroup`),
  KEY `idGroup` (`idGroup`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `activitiesgroups`
--
ALTER TABLE `activitiesgroups`
  ADD CONSTRAINT `activitiesgroups_ibfk_1` FOREIGN KEY (`idGroup`) REFERENCES `groups` (`idGroup`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `activitiesgroups_ibfk_2` FOREIGN KEY (`idActivity`) REFERENCES `activities` (`idActivity`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`idUserSender`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`idUserReceiver`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `review_ibfk_2` FOREIGN KEY (`idActivity`) REFERENCES `activities` (`idActivity`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `usersgroups`
--
ALTER TABLE `usersgroups`
  ADD CONSTRAINT `usersgroups_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `usersgroups_ibfk_2` FOREIGN KEY (`idGroup`) REFERENCES `groups` (`idGroup`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
