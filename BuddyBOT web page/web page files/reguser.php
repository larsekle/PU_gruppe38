<?php

  session_start();

  //storing the errormessage from session-variable from regcheck, if registration fails.
  if(isset($_SESSION['errormessage'])){
    $errormessage = $_SESSION['errormessage'];
  }

?>

<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Register Here</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
  <link rel='stylesheet prefetch' href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900'>
  <link rel='stylesheet prefetch' href='http://fonts.googleapis.com/css?family=Montserrat:400,700'>
  <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/reguser.css">
</head>
<div class = "header">
  <div class = "headertext">
    <a href = "http://folk.ntnu.no/ingriehe/index.html" style="text-decoration:none">BUDDYBOT</a>
  </div>
</div>
<body>
<div class = "padding"></div> 
<div class="container">
  <div class="info">
    <h1>Register user</h1>
    <div class ="undertext"> 
      <?php 
        echo $errormessage; 
        unset($_SESSION['errormessage']);
      ?>
    </div>
  </div>
</div>
<div class="form">
  <center>
    <div class="img-circle"></div>
  </center>
  <div class = "image-padding-bottom"></div>
  <form action="regcheck.php" method="post">
    <center>
      <input type="text" name ="firstname" placeholder="First name" style="text-align:center">
      <br>
      <input type="text" name ="lastname" placeholder="Last name" style="text-align:center">
      <br>
      <input type="text" name ="username" placeholder="Username" style="text-align:center">
      <br>
      <input type="email" name ="email" placeholder="Email" style="text-align:center">
      <br>
      <input type="password" name ="password" placeholder="Password" style="text-align:center">
      <br>
      <input type="password" name ="passwordtwo" placeholder="Confirm password" style="text-align:center">
      <br>
      <div class = "drowdown" align="center">
        <select name = 'selectPosition'>
          <option value = "Select Position">Select Position</option>
          <option value = "Lecturer">Lecturer</option>
          <option value = "StudentAssistant">Student assistant</option>
        </select>
      </div>
      <br>
      <td colspan="2" style="text-align:center;"><input type="submit" value="Register user"></td>
    </center>
  </form>
  <p class="message">Already registered? <a href="login.html">Log in here</a></p>
</div>
</body>
</html>