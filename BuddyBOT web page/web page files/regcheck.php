
<?php
	
	//Includes db-lecturer to get access to MySQL-queries
	include ("db-connect.php");

	session_start();

	//connects to database
	$db = new DatabaseConnect();
	$conn = $db->getConnection();

	//stores variables from register page, if something is written in.
	if(!empty($_POST)){ 
		$firstname = trim($_POST['firstname'],";'");
		$lastname = trim($_POST['lastname'],";'");
		$username = trim($_POST['username'],";'");
		$email = trim($_POST['email'],";'");
		$password = trim($_POST['password']);
		$passwordtwo = trim($_POST['passwordtwo']);
		$position = $_POST['selectPosition'];

		createUser($username, $firstname, $lastname, $email, $password, $passwordtwo, $position);
	}
	else{
		echo "Something went wrong with the data";
	}

	/**
   * Returns if the passwords are equal.
   * If not true, return session errormessage for later use.
   * @param string $password Password
   * @param string $passwordtwo Confirmation password
   * @return bool 
   */
	function checkPasswords($password, $passwordtwo){
		if ($password == $passwordtwo){
			return true;
		}
		else{
			$_SESSION['errormessage'] = 'You have to type in two equal passwords';
			return false;
		}
	}

	/**
   * Returns if the user chose a position.
   * If not true, return session errormessage for later use.
   * @param string $position Position of user
   * @return bool 
   */
	function checkPosition($position){
		if ($position != "Select Position"){
			return true;
		}
		else{
			$_SESSION['errormessage'] = 'You have to select a position';
			return false;
		}
	}

	/**
   * Returns if the username already is in the database(true if it's a new one).
   * If not true, return session errormessage for later use.
   * @param string $username Username
   * @return bool 
   */
	function checkUsername($username){
		$checkUsername = $GLOBALS['conn']->query("SELECT * FROM Users WHERE Username = '$username'");
    if($checkUsername->num_rows > 0){ 
    	$_SESSION['errormessage'] = 'The username already exist';
      return false;
    }
    else{
    	return true;
    }
	}

	/**
   * Returns if the full name of the user already is in the database(true if it's a new one).
   * If not true, return session errormessage for later use.
   * @param string $firstname First name
   * @param string $lastname Last name
   * @return bool 
   */
	function checkName($firstname, $lastname){
		$checkName = $GLOBALS['conn']->query("SELECT * FROM Users WHERE (Firstname = '$firstname' AND Lastname = '$lastname') ");
    if($checkName->num_rows > 0){ 
    	$_SESSION['errormessage'] = 'There is already a user with this name';
      return false;
    }
    else{
    	return true;
    }
	}

	/**
   * Returns if the email already is in the database(true if it's a new one).
   * If not true, return session errormessage for later use.
   * @param string $email Email
   * @return bool 
   */
	function checkEmail($email){
		$checkEmail = $GLOBALS['conn']->query("SELECT * FROM Users WHERE Email= '3$email'");
	  if($checkEmail->num_rows > 0){ 
	    $_SESSION['errormessage'] = 'There is already a user with this email';
	    return false;
	  }
	  else{
	    return true;
	  }
	}

	/**
   * Tries creating the user using the functions above.
   * If all functions are true, insert information into database and go to login.html.
   * If not, or if the insertion fails, send to reguser.php with apropriate errormessage.
   * If not true, return session errormessage for later use.
   * @param string $username Username
   * @param string $firstname First name
   * @param string $lastname Last name
   * @param string $email Email
   * @param string $password Password
   * @param string $passwordtwo Confirmation password
   * @param string $position Position of user
   * @return bool 
   */
	function createUser($username, $firstname, $lastname, $email, $password, $passwordtwo,$position){
		if (checkUsername($username) and checkName($firstname, $lastname) and checkEmail($email) and checkPasswords($password, $passwordtwo) and checkPosition($position)){
			if($insert = $GLOBALS['conn']->query("INSERT INTO Users(Username, Password, Firstname, Lastname, Email, Position) VALUES ('{$username}','{$password}','{$firstname}','{$lastname}','{$email}','{$position}')")){
					session_unset(); 
  				session_destroy();
		    	header("Location: login.html");
		  }
		  else{
		  	header("Location: reguser.php");
		  }
		}
		else{
			header("Location: reguser.php");
		}
	}
	
?>



