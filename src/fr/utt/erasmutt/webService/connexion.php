<?php
	//Changement de zone pour la date
	date_default_timezone_set('Europe/Paris');

	//connection Ã  la BD
    $login="thikev";
    $pass="if262012";
    $dbname="mysql:dbname=erasmutt;host=localhost";
    
	$typeConnexionArray=array("ajouter","modifier","connecter");
	
	define("SALT", "b2J78Bji36");
	
    try {
        $db = new PDO($dbname, $login, $pass);
        echo("connexion Ã  la base reussie <br/>");
    } 
    catch (PDOException $e) {
        print "Connexion impossible : " . $e->getMessage() . "<br/>";
        die();
    }

	/* Attribut pour ajout connexion */
    $firstname="";
	$lastname="";
	$password="";
	$mail="";
	$token="";
	/********************************/
	
	//$jsonArray = "";
	//$messageArray = "";
	//$messagesContent = "";
    //$jsonArray=["message"=>"","token"=>"","username"=>""];
    
	
	
	
	if(isset($_GET['typeConnexion'])&&!empty($_GET['typeConnexion'])){
		$typeConnexion = $_GET['typeConnexion'];
		//Ajout un utilisateur
		if($typeConnexion == $typeConnexionArray[0]){
			
			if(isset($_GET['firstname'])&&!empty($_GET['firstname'])){
				$firstname = $_GET['firstname'];
			}
			if(isset($_GET['lastname'])&&!empty($_GET['lastname'])){
				$lastname = $_GET['lastname'];
			}
			if(isset($_GET['password'])&&!empty($_GET['password'])){
				$password = $_GET['password'];
			}
			if(isset($_GET['mail'])&&!empty($_GET['mail'])){
				$mail = $_GET['mail'];	
			}
			
			if($firstname!="" && $lastname!="" && $password!="" && $mail!=""){
				ajouterUtilisateur($db,$firstname,$lastname,$password,$mail);
			}
			else{
				$jsonArray["typeConnexion"]=$typeConnexionArray[0];
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
		}
		//Modification utilisateur
		else if($typeConnexion == $typeConnexionArray[1]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
			}
			if(isset($_GET['firstname'])&&!empty($_GET['firstname'])){
				$firstname = $_GET['firstname'];
			}
			if(isset($_GET['lastname'])&&!empty($_GET['lastname'])){
				$lastname = $_GET['lastname'];
			}
			if(isset($_GET['pictureUser'])&&!empty($_GET['pictureUser'])){
				$pictureUser = $_GET['pictureUser'];
			}
			if(isset($_GET['mail'])&&!empty($_GET['mail'])){
				$mail = $_GET['mail'];	
			}
			
			if($firstname!="" && $lastname!="" && $mail!="" && $token!=""){
				modifierUtilisateur($db,$firstname,$lastname,$mail,$pictureUser,$token);
			}
			else{
				$jsonArray["typeConnexion"]=$typeConnexionArray[1];
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		}
		//Connexion
		else if($typeConnexion == $typeConnexionArray[2]){
			if(isset($_GET['mail'])&&!empty($_GET['mail'])){
				$mail=$_GET['mail'];
			}
			if(isset($_GET['password'])&&!empty($_GET['password'])){
				$password=$_GET['password'];
			}
			
			if($password!="" && $mail!=""){
				connexionUtilisateur($db,$password,$mail);
			}
			else{
				$jsonArray["typeConnexion"]=$typeConnexionArray[2];
				$jsonArray["error"]="true";
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		}
		
	}
	
	function ajouterUtilisateur($db,$firstname,$lastname,$password,$mail){
		
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where mail = ?;");
        $stmt->execute(array($mail));
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		if($res['nbUser'] ==  0){
			$statement=$db->prepare("insert into users (firstname, lastname, password, mail) VALUES (?,?,?,?)");
			$statement->bindParam(1,$firstname);
			$statement->bindParam(2,$lastname);
			$statement->bindParam(3,md5($password.md5(SALT)));
			$statement->bindParam(4,$mail);
			$statement->execute();
			
			$jsonArray["typeConnexion"]="Ajouter";
			$jsonArray["message"]="Ajout reussis";
			$jsonArray["error"]="false";
		}
		else{
			
			$jsonArray["typeConnexion"]="Ajouter";
			$jsonArray["message"]="mail deja existant";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}

	function modifierUtilisateur($db,$firstname,$lastname,$mail,$pictureUser,$token){
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where mail = ? and token= '$token';");
        $stmt->execute(array($mail));
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeConnexion"]="modifier";
		if($res['nbUser'] ==  1){
			$statement=$db->prepare("update users set firstname = '$firstname' , lastname = '$lastname', pictureUser = '$pictureUser' where mail = ?;");
			$statement->execute(array($mail));
			$jsonArray["message"]="modification reussis";
			$jsonArray["firstname"]=$firstname;
			$jsonArray["lastname"]=$lastname;
			$jsonArray["pictureUser"]=$pictureUser;
			$jsonArray["error"]="false";
		}
		else{
			$jsonArray["message"]="Probleme token or mail";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}
	
	function seConnecter($db,$mail,$firstname,$lastname,$jsonArray){
			
			$token = uniqid();
			$datenow = date("Y-m-d H:i:s");
			
			$req=$db->prepare("update users set token ='$token', lastConnection='$datenow', nbTentative=0, banned=0 where mail=? ;");
			$req->execute(array($mail));
			$jsonArray["message"]="connexion reussie";
			$jsonArray["token"]=$token;
			$jsonArray["mail"]=$mail;
			$jsonArray["firstname"]=$firstname;
			$jsonArray["lastname"]=$lastname;
			$jsonArray["error"]="false";			
			return $jsonArray;
	}


	function connexionUtilisateur($db,$password,$mail){
		$statementConnexion=$db->prepare("SELECT COUNT(idUser) AS nbUser, firstname, banned, BannedDate, nbTentative, lastname, mail as login,password as pass FROM users where mail = ?;");
        $statementConnexion->execute(array($mail));    
        $res = $statementConnexion->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeConnexion"]="Connecter";
		//si un user
        if($res['nbUser'] ==  1){
        	//si il n'est pas banni
        	if($res['banned'] !=  1){
        		//si nb tentative+1 est inférieur à 5?
				if(($res['nbTentative']+1) < 5 ) {
					if(md5($password.md5(SALT)) == $res['pass']){
						$jsonArray=seConnecter($db,$mail,$res['firstname'],$res['lastname'],$jsonArray);
					}
					else {
						//Incrémente le nombre de tentative
						$req=$db->prepare("update users set nbTentative =nbTentative+1 where mail = ?;");
						$req->execute(array($mail));
						$jsonArray["message"]="il n y a pas d utilisateurs avec ce couple login mot de passe";
						$jsonArray["error"]="true";
					}
				}			
				else{
					$date=mktime();
					echo("mktime = ".$date);
					$req=$db->prepare("update users set BannedDate ='".$date."', nbTentative = 0,banned=1 where mail = ?;");
					$req->execute(array($mail));

                    $jsonArray["message"]="Vous etes banni pour une durée de 5 minutes après avoir essaye de vous connecter 5 fois sans trouver le bon mot de passe";
					$jsonArray["error"]="true";

				}
			}
			//si banni
			else{
				$duree = mktime() - $res['BannedDate'] ;
				//si encore banni
				if( $duree<300 ) {
					$jsonArray["message"]='Vous êtes actuellement banni pour une durée de 5 minutes. Merci de réessayer ultérieurement ! il vous reste environ : '.floor(5-($duree/60)) .' min';
					$jsonArray["error"]="true";
				}
				else{
					if(md5($password.md5(SALT)) == $res['pass']){
						$req=$db->prepare("update users set nbTentative = 0,banned=0 where mail = ?;");
						$req->execute(array($mail));
						$jsonArray=seConnecter($db,$mail,$res['firstname'],$res['lastname'],$jsonArray);
					}
					else{
						$req=$db->prepare("update users set nbTentative = nbTentative+1,banned=0 where mail = ?;");
						$req->execute(array($mail));
						$jsonArray["message"]="il n y a pas d utilisateurs avec ce couple login mot de passe";
						$jsonArray["error"]="true";
					}
				}

			}
		}
		else {
			//Incrémente le nombre de tentative
			$jsonArray["message"]="utilisateurs n existe pas";
			$jsonArray["error"]="true";

		}
		echo json_encode ($jsonArray);
	}
        
?>