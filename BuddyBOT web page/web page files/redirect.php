<?php

	//Includes db-lecturer to get access to MySQL-queries
	include ("db-connect.php");
	session_start();

	//connects to database
	$db = new DatabaseConnect();
  $conn = $db->getConnection();

  //if the user has written in username and password, and clicked the Log in-button, store the variables.
	if(!empty($_POST)){
		$username = trim($_POST['username'],";'"); 
		$password = trim($_POST['password'],";'");

		//checks if the user already exists
		if($results = $conn->query("SELECT * FROM Users WHERE Username= '$username' AND Password= '$password'")){
			//if the user exists.
			if($results->num_rows){
				$getPosition = mysqli_query($conn, "SELECT Position FROM Users WHERE Username= '$username'");
				$positionResult = mysqli_fetch_array($getPosition);
				$position = $positionResult[0];
				$_SESSION['position'] = $position;
				$_SESSION['username'] = $username;

				//if the user is a lecturer, redirect to report-lecturer.php.
				if($_SESSION['position'] == 'Lecturer'){
					$_SESSION['lecturer'] = $position;
					$ass = -1;
					$_SESSION['ass'] = $ass;
					header("Location: report-lecturer.php");
				} 

				//if the user is a student assistant, redirect to report-studass.php.
				else if($_SESSION['position'] == 'StudentAssistant'){
					$getStudassID = mysqli_query($conn, "SELECT StudentAssistantID FROM Users WHERE Username= '$username'");
					$IDResult = mysqli_fetch_array($getStudassID);
					$studassID = $IDResult[0];
					$_SESSION['studassID'] = $studassID;
					$ass = -1;
					$_SESSION['ass'] = $ass;
					header("Location: report-studass.php");
				}

				//if the user is a student, redirect to report-student.php.
				else if($_SESSION['position'] == 'Student'){
					$getStudID = mysqli_query($conn, "SELECT StudentID FROM Users WHERE Username= '$username'");
					$IDResult = mysqli_fetch_array($getStudID);
					$studID = $IDResult[0];
					$_SESSION['studID'] = $studID;
					$ass = -1;
					$_SESSION['ass'] = $ass;
					header("Location: report-student.php");
				}

				//if none of the above, redirect to reguser.php.
				else{
					header("Location: reguser.php");
				}	
			}

			//if the user doesn't exist, redirect to reguser.php.
			else{ 
				session_destroy();
				header("Location: reguser.php");
			}
		}	
	}

	//close connecton.
	$conn->close();

?>


