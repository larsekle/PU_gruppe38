<?php

	//Destroys session and unsets variables when logging out, preventing unwanted access.
	session_start();
	session_unset(); 
	session_destroy(); 
	header ("Location: login.html");
	
?>

