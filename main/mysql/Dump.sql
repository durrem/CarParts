-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: carparts
-- ------------------------------------------------------
-- Server version	8.0.22

--
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articles` (
  `NUMART` varchar(15) NOT NULL,
  `CDEART` varchar(5) NOT NULL,
  `LIBART` varchar(50) NOT NULL,
  `ACHUSS` decimal(10,2) DEFAULT NULL,
  `ACHFRS` decimal(10,2) NOT NULL,
  `VTEFRS` decimal(10,2) NOT NULL,
  `QTEART` int NOT NULL,
  `FOUART` varchar(50) NOT NULL,
  `CHIFF_AFF` decimal(10,2) NOT NULL,
  `ENTREE` int DEFAULT NULL,
  `Created` date NOT NULL,
  PRIMARY KEY (`NUMART`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `billarticles`
--

DROP TABLE IF EXISTS `billarticles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billarticles` (
  `NUMFAC` int NOT NULL,
  `NUMLNG` int NOT NULL,
  `NUMART` varchar(15) NOT NULL,
  `QTEART` int NOT NULL,
  `VTEFRS` decimal(10,2) NOT NULL,
  PRIMARY KEY (`NUMFAC`,`NUMLNG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clientbills`
--

DROP TABLE IF EXISTS `clientbills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientbills` (
  `NUMFAC` int NOT NULL,
  `NUMCLI` int NOT NULL,
  `PCENT` decimal(10,4) NOT NULL,
  `MODE_PAYMT` char(1) NOT NULL,
  `DEJA_PAYE` decimal(10,2) NOT NULL,
  `FRAIS_TRSP` decimal(10,2) NOT NULL,
  `DATE_EMIS` date NOT NULL,
  `TVA` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`NUMFAC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `NUMCLI` int NOT NULL,
  `TYPCLI` varchar(15) DEFAULT NULL,
  `NOMCLI` varchar(50) NOT NULL,
  `ALATTN` varchar(50) NOT NULL,
  `ADRESSE` varchar(50) NOT NULL,
  `NOPLOC` varchar(10) NOT NULL,
  `LOCALITE` varchar(25) NOT NULL,
  `INDNOTEL` varchar(20) NOT NULL,
  `EMAIL` varchar(75) NOT NULL,
  `POURCENTS` decimal(10,4) NOT NULL,
  `CLI_DEPUIS` date NOT NULL,
  `TOT_ACHATS` decimal(10,2) NOT NULL,
  `TOT_PAYE` decimal(10,2) NOT NULL,
  `CANTCLI` varchar(15) NOT NULL,
  `COMCLI` varchar(100) NOT NULL,
  PRIMARY KEY (`NUMCLI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scratchpad`
--

DROP TABLE IF EXISTS `scratchpad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scratchpad` (
  `Ident` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `Type` int NOT NULL,
  `Code` varchar(25) NOT NULL,
  `Description` varchar(100) NOT NULL,
  PRIMARY KEY (`Ident`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `Ident` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Ident`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'carparts'
--
drop procedure CopyOrderItems;

DELIMITER $$
CREATE PROCEDURE CopyOrderItems( IN OldOrder INTEGER, IN NewOrder INTEGER, IN pNUMLNG INTEGER  )
BEGIN
	DECLARE finished INTEGER DEFAULT 0;
    DECLARE pNUMART Varchar( 15 );
    DECLARE pQTEART double;
    DECLARE pVTEFRS double;
	DECLARE curs CURSOR FOR Select NUMART, QTEART from BillArticles where NUMFAC = OldOrder;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
	OPEN curs;    
getItem:	
	LOOP
		FETCH curs INTO pNUMART, pQTEART;
		IF finished = 1 THEN 
			LEAVE getItem;
		END IF; 
        Select VTEFRS into pVTEFRS from Articles where NUMART = pNUMART;
		Insert into BillArticles ( NUMFAC, NUMLNG, NUMART, QTEART, VTEFRS ) values ( NewOrder, pNUMLNG, pNUMART, pQTEART, pVTEFRS * pQTEART );
		Set pNUMLNG = pNUMLNG + 1;
	END LOOP getItem;
	CLOSE curs;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE MakeOrder( IN itemIdList VARCHAR(1024) )
BEGIN
	DECLARE finished INTEGER DEFAULT 0;
    DECLARE pIdent INTEGER;
    DECLARE pType INTEGER;
    DECLARE pCode Varchar( 15 );
    DECLARE ClientId INTEGER DEFAULT NULL;
    DECLARE pNUMFAC Varchar( 15 ) DEFAULT NULL;
    DECLARE pNUMLNG INTEGER DEFAULT 1;
    DECLARE Rabais decimal( 10, 4 );
    DECLARE Price decimal( 10, 2 );
	DECLARE curs CURSOR FOR Select Ident, Type, Code from Scratchpad where UserId = 1 and FIND_IN_SET(Scratchpad.Ident, itemIdList ) order by Ident;    
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
        
    select Max( NUMFAC ) + 1 into pNUMFAC from ClientBills where NUMFAC like CONCAT( DATE_FORMAT(CURDATE(), "%y%m"), '%' );
	IF pNUMFAC IS NULL THEN 
		Select CONCAT( DATE_FORMAT(CURDATE(), "%y%m"), '001' ) into pNUMFAC;
	END IF;    
	OPEN curs;
getScratch:	
	LOOP
		FETCH curs INTO pIdent, pType, pCode;
		IF finished = 1 THEN 
			LEAVE getScratch;
		END IF;        
		CASE pType
		WHEN 1 THEN 
			-- Use this client if no client until now
			IF ClientId IS NULL THEN 
				-- SET NUMFAC = ...
                -- Generate NUMFAC new !
                Set ClientId = pCode;
                Select POURCENTS into Rabais from Clients where NUMCLI = pCode;
				Insert into ClientBills ( NUMFAC, NUMCLI, PCENT, MODE_PAYMT, DEJA_PAYE, FRAIS_TRSP, DATE_EMIS, TVA ) values (  pNUMFAC, pCode, Rabais, 'C', 0, 0, CurDate(), 0 );
			END IF;
			
		WHEN 2 THEN 
			-- Use this article or increment count 
			IF pNUMFAC IS NOT NULL THEN 
				-- Create new ITEM
                Select VTEFRS into Price from Articles where NUMART = pCode;
                -- Check if article already there ?
				Insert into BillArticles ( NUMFAC, NUMLNG, NUMART, QTEART, VTEFRS ) values ( pNUMFAC, pNUMLNG, pCode, 1, Price );
				Set pNUMLNG = pNUMLNG + 1;
			END IF;
		
		WHEN 3 THEN 
			-- Use this client and these items if no client until now
			IF ClientId IS NULL THEN 
				-- Get ClientId from Orders
				Select NUMCLI into ClientId from ClientBills where NUMFAC = pCode;
                -- Generate NUMFAC new !
                Select PCENT into Rabais from ClientBills where NUMFAC = pCode;
				Insert into ClientBills ( NUMFAC, NUMCLI, PCENT, MODE_PAYMT, DEJA_PAYE, FRAIS_TRSP, DATE_EMIS, TVA ) values (  pNUMFAC, ClientId, Rabais, 'C', 0, 0, CurDate(), 0 );
				-- Get articles from Order
				 -- Cursor on Select * from BillArticles where NUMFAC = Code
				CALL CopyOrderItems( pCode, NUMFAC, pNUMLNG );
			END IF;
		
		END CASE;        

	END LOOP getScratch;
	CLOSE curs;
END$$
DELIMITER ;

-- Dump completed on 2021-01-23 17:17:59
