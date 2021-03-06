<?php
	//Changement de zone pour la date
	date_default_timezone_set('Europe/Paris');

	//connection Ã  la BD
    $login="**";
    $pass="**";
    $dbname="mysql:dbname=**;host=**";
    
	$typeActivitiesArray=array("ajouter","modifier","lister","query","markUpdate");
	
    try {
        $db = new PDO($dbname, $login, $pass);
    } 
    catch (PDOException $e) {
        print "Connexion impossible : " . $e->getMessage() . "<br/>";
        die();
    }
    
	
	//www.ws.kevin-larue.fr/activiesManager.php?typeActivies=ajouter&name=UTT&description=Visite de l utt&longitude=4.056466&latitude=48.269312&website=www.utt.fr
	
	if(isset($_GET['typeActivies'])&&!empty($_GET['typeActivies'])){
		$typeActivities = $_GET['typeActivies'];
		//Ajouter une activité
		if($typeActivities == $typeActivitiesArray[0]){
			
			if(isset($_GET['name'])&&!empty($_GET['name'])){
				$name = $_GET['name'];
			}
			if(isset($_GET['description'])&&!empty($_GET['description'])){
				$description = $_GET['description'];
			}
			if(isset($_GET['longitude'])&&!empty($_GET['longitude'])){
				$longitude = $_GET['longitude'];
			}
			if(isset($_GET['latitude'])&&!empty($_GET['latitude'])){
				$latitude = $_GET['latitude'];	
			}
			if(isset($_GET['website'])&&!empty($_GET['website'])){
				$website = $_GET['website'];	
			}
			if(isset($_GET['address'])&&!empty($_GET['address'])){
				$address = $_GET['address'];	
			}
			if($name!="" && $description!="" && $longitude!="" && $latitude!=""){
				ajouterActivite($db,$name,$description,$longitude,$latitude,$website,$address);
			}
			else{
				$jsonArray["typeActivities"]=$typeActivitiesArray[0];
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
		}
		//Modification activite
		else if($typeActivities == $typeActivitiesArray[1]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
			}
			if(isset($_GET['name'])&&!empty($_GET['name'])){
				$name = $_GET['name'];
			}
			if(isset($_GET['description'])&&!empty($_GET['description'])){
				$description = $_GET['description'];
			}
			if(isset($_GET['longitude'])&&!empty($_GET['longitude'])){
				$longitude = $_GET['longitude'];
			}
			if(isset($_GET['latitude'])&&!empty($_GET['latitude'])){
				$latitude = $_GET['latitude'];	
			}
			if(isset($_GET['address'])&&!empty($_GET['address'])){
				$address = $_GET['address'];	
			}
			if(isset($_GET['website'])&&!empty($_GET['website'])){
				$website = $_GET['website'];	
			}
			else{
				$website = "";
			}
			if(isset($_GET['id'])&&!empty($_GET['id'])){
				$id = $_GET['id'];	
			}
			
			if($id!="" && $name!="" && $description!="" && $longitude!="" && $latitude!=""){
				modifierActivite($db,$id,$name,$description,$longitude,$latitude,$website,$address);
			}
			else{
				$jsonArray["typeActivities"]=$typeActivitiesArray[1];
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		}
		//lister activite
		else if($typeActivities == $typeActivitiesArray[2]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
				listerActivite($db,$token,$messageArray);
			}
			else{
				$jsonArray["typeActivities"]=$typeActivitiesArray[2];
				$jsonArray["error"]="true";
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		}
		else if($typeActivities == $typeActivitiesArray[3]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
				if(isset($_GET['query'])&&!empty($_GET['query'])){
					$query = $_GET['query'];
					searchActivite($db,$token,$messageArray,$query);
				}				
			}
			else{
				$jsonArray["typeActivities"]=$typeActivitiesArray[3];
				$jsonArray["error"]="true";
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}
			
		} else if($typeActivities == $typeActivitiesArray[4]){
			if(isset($_GET['token'])&&!empty($_GET['token'])){
				$token = $_GET['token'];
				
				if(isset($_GET['id'])&&!empty($_GET['id'])){
					$id = $_GET['id'];	
					updateMarkActivity($db,$token,$id);
				}
				
			}
			else{
				$jsonArray["typeActivities"]=$typeActivitiesArray[4];
				$jsonArray["error"]="true";
				$jsonArray["message"]="erreur";
				echo json_encode ($jsonArray);
			}		
		}
		
	}
	
	function ajouterActivite($db,$name,$description,$longitude,$latitude,$website){
		
		$stmt= $db->prepare("SELECT COUNT(idActivity) AS nbActivity FROM activities where name = ?;");
        $stmt->execute(array($name));
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		if($res['nbUser'] ==  0){
			$statement=$db->prepare("insert into activities (name, descriptionActivity, longitude, latitude,website,averageMark,address) VALUES (?,?,?,?,?,0,?)");
			$statement->bindParam(1,$name);
			$statement->bindParam(2,$description);
			$statement->bindParam(3,$longitude);
			$statement->bindParam(4,$latitude);
			$statement->bindParam(5,$website);
			$statement->bindParam(6,$address);
			$statement->execute();
			
			$jsonArray["typeActivities"]="Ajouter";
			$jsonArray["message"]="Ajout reussis";
			$jsonArray["error"]="false";
		}
		else{
			
			$jsonArray["typeActivities"]="Ajouter";
			$jsonArray["message"]="mail deja existant";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}

	function modifierActivite($db,$id,$name,$description,$longitude,$latitude,$website,$address){
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
        $stmt->execute(array());
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeActivities"]="modifier";
		if($res['nbUser'] ==  1){
			$statement=$db->prepare("update activities set name = '$name' , descriptionActivity = '$description', longitude = '$longitude' where idActivity = ?;");
			$statement->execute(array($id));
			$jsonArray["message"]="modification reussis";
			$jsonArray["name"]=$name;
			$jsonArray["description"]=$description;
			$jsonArray["longitude"]=$longitude;
			$jsonArray["latitude"]=$latitude;
			$jsonArray["website"]=$website;
			$jsonArray["address"]=$address;
			$jsonArray["error"]="false";
		}
		else{
			$jsonArray["message"]="Probleme token or mail";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}
	
	function listerActivite($db,$token,$messageArray){
			
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
        $stmt->execute();
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeActivities"]="lister";
		if($res['nbUser'] ==  1){
			$statement=$db->prepare("SELECT * FROM activities");
			$statement->execute();
			$jsonArray["message"]="listageOK";
			$jsonArray["error"]="false";
			while( $row =$statement->fetch(PDO::FETCH_ASSOC) ) {
				$messageContent["idActivity"]=$row["idActivity"];
				$messageContent["name"]=utf8_encode($row["name"]);
				$messageContent["desc"]=utf8_encode($row["descriptionActivity"]);
				$messageContent["picture"]=$row["pictureActivity"];
				$messageContent["averageMark"]=$row["averageMark"];
				$messageContent["longitude"]=$row["longitude"];
				$messageContent["latitude"]=$row["latitude"];
				$messageContent["website"]=$row["website"];
				$messageContent["focusOn"]=$row["focusOn"];
				$messageContent["address"]=$row["address"];
				$messageArray[] = $messageContent;
			}
			$jsonArray["listActivities"]=$messageArray;
			
		}
		else{
			$jsonArray["message"]="erreur de token";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}
	
	function searchActivite($db,$token,$messageArray,$query){
			
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
        $stmt->execute(array());
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeActivities"]="search";
		if($res['nbUser'] ==  1){
			$statement=$db->prepare("SELECT * FROM activities where name like '%$query%'");
			$statement->execute();
			$jsonArray["message"]="searchOK";
			$jsonArray["error"]="false";
			while( $row =$statement->fetch(PDO::FETCH_ASSOC) ) {
				$messageContent["idActivity"]=$row["idActivity"];
				$messageContent["name"]=$row["name"];
				$messageContent["desc"]=$row["descriptionActivity"];
				$messageContent["picture"]=$row["pictureActivity"];
				$messageContent["averageMark"]=$row["averageMark"];
				$messageContent["longitude"]=$row["longitude"];
				$messageContent["latitude"]=$row["latitude"];
				$messageContent["website"]=$row["website"];
				$messageContent["focusOn"]=$row["focusOn"];
				$messageArray[] = $messageContent;
			}
			$jsonArray["resultActivities"]=$messageArray;
			
		}
		else{
			$jsonArray["message"]="erreur de token";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}

	function updateMarkActivity($db,$token,$id){
		$stmt= $db->prepare("SELECT COUNT(idUser) AS nbUser FROM users where token= '$token';");
        $stmt->execute(array());
		$res = $stmt->fetch(PDO::FETCH_ASSOC);
		$jsonArray["typeActivities"]="markUpdate";
		if($res['nbUser'] ==  1){
			$statement2=$db->prepare("SELECT AVG(mark) as mark FROM review WHERE idActivity = $id;");
			$statement2->execute(array());
			$mark = $statement2->fetch(PDO::FETCH_ASSOC);
			$statement=$db->prepare("UPDATE activities SET averageMark = ? WHERE idActivity = ?;");
			$statement->execute(array($mark,$id));
			$jsonArray["mark"] = $mark['mark'];
			$jsonArray["message"]="modification reussis";
			$jsonArray["error"]="false";
		}
		else{
			$jsonArray["message"]="Probleme token or mail";
			$jsonArray["error"]="true";
		}
		echo json_encode ($jsonArray);
	}
	
        
?>
