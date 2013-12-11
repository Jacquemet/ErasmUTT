<?php
	//Changement de zone pour la date
	date_default_timezone_set('Europe/Paris');

	//connection à la BD
    $login="kevinlarmag";
    $pass="kevin140289";
    $dbname="mysql:dbname=kevinlarmag;host=mysql51-54.perso";
    
	$typeReviewsArray=array("ajouter","lister");
	
    try {
        $db = new PDO($dbname, $login, $pass);
        echo("connexion à la base reussie <br/>");
    } 
    catch (PDOException $e) {
        print "Connexion impossible : " . $e->getMessage() . "<br/>";
        die();
    }
    
	
	//www.ws.kevin-larue.fr/activiesManager.php?typeActivies=ajouter&name=UTT&description=Visite de l utt&longitude=4.056466&latitude=48.269312&website=www.utt.fr
	
	if(isset($_GET['typeReviews'])&&!empty($_GET['typeReviews'])){
		$typeReviews = $_GET['typeReviews'];
		//Ajouter une activit�
		if($typeReviews == $typeReviewsArray[0]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
			}
			if(isset($_GET['idUser'])&&!empty($_GET['idUser'])){
				$idUser = $_GET['idUser'];
			}
			if(isset($_GET['idActivity'])&&!empty($_GET['idActivity'])){
				$idActivity = $_GET['idActivity'];
			}
			if(isset($_GET['title'])&&!empty($_GET['title'])){
				$title = $_GET['title'];
			}
			if(isset($_GET['description'])&&!empty($_GET['description'])){
				$description = $_GET['description'];	
			}
			if(isset($_GET['mark'])&&!empty($_GET['mark'])){
				$mark = $_GET['mark'];	
			}
			if(isset($_GET['date'])&&!empty($_GET['date'])){
				$date = $_GET['date'];	
			}
			if(isset($_GET['language'])&&!empty($_GET['language'])){
				$language = $_GET['language'];	
			}
			if($token!="" && $idUser!="" && $idActivity!="" && $title!="" && $description!="" && $mark!="" && $date!="" && $language!=""){
				ajouterReview($db,$token,$idUser,$idActivity,$title,$description,$mark,$date,$language);
			}
			else{
				$jsonArray["typeReviews"]=$typeReviewsArray[0];
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
		}
		//lister review
		else if($typeReviews == $typeReviewsArray[1]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];	
			}
			
			if(idActivity!="" && token!="") {
				listerReview($db,$token,$messageArray);
			}
			else{
				$jsonArray["typeReviews"]=$typeReviewsArray[1];
				$jsonArray["error"]="true";
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		}		
	}
	
	function ajouterReview($db,$token,$idUser,$idActivity,$title,$description,$mark,$date,$language){
		
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
		$stmt->execute();
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeReviews"]="ajouter";
		if($res['nbUser'] ==  1){

			$statement=$db->prepare("insert into review (idUser, idActivity, title, description,mark,date,language) VALUES (?,?,?,?,?,?,?)");
			$statement->bindParam(1,$idUser);
			$statement->bindParam(2,$idActivity);
			$statement->bindParam(3,$title);
			$statement->bindParam(4,$description);
			$statement->bindParam(5,$mark);
			$statement->bindParam(6,$date);
			$statement->bindParam(7,$language);
			$statement->execute();
			
			$jsonArray["typeReviews"]="Ajouter";
			$jsonArray["message"]="Ajout reussis";
			$jsonArray["error"]="false";

		} else{
			$jsonArray["message"]="erreur de token";
			$jsonArray["error"]="true";
		}

		echo json_encode ($jsonArray);
	}

	
	function listerReview($db,$token,$messageArray){
			
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
		$stmt->execute();
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeReviews"]="lister";
		if($res['nbUser'] ==  1){
			$statement=$db->prepare("SELECT * FROM review");
			$statement->execute(array($idActivity));
			$jsonArray["message"]="listageOK";
			$jsonArray["error"]="false";
			while( $row =$statement->fetch(PDO::FETCH_ASSOC) ) {
				$messageContent["idReview"]=$row["idReview"];
				$messageContent["idUser"]=$row["idUser"];
				$messageContent["idActivity"]=$row["idActivity"];
				$messageContent["title"]=utf8_encode($row["title"]);
				$messageContent["description"]=utf8_encode($row["description"]);
				$messageContent["mark"]=$row["mark"];
				$messageContent["date"]=$row["date"];
				$messageContent["language"]=$row["language"];
				$messageArray[] = $messageContent;
			}
			$jsonArray["listReviews"]=$messageArray;
			
		}
		else{
			$jsonArray["message"]="erreur de token";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}
		
?>