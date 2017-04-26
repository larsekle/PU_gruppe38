<?php
  
  /**
   * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
   */

  class DatabaseConnect{

    private $conn;

    /**
     * Constructor to set up database and connect
     */
    function __construct(){
      $this->conn = new mysqli('mysql.stud.ntnu.no', 'larsekle_tdt4140', 'PUgruppe38', 'larsekle_tdt4140database') or die("Unable to connect to database");
    }

    /**
     * Gets the connection parameter to the database
     * @return connection
     */
    function getConnection(){
      return $this->conn;
    }

    /**
     * Checks if there is any users in the database with the given username and password.
     * @param string $username Username
     * @param string $password Password
     * @return resource The query results from MySQL
     */
    function checkUser($username,$password){
    	return (mysqli_query($this->conn, "SELECT * FROM Users WHERE Username= $username AND Password= $password"));
    }

    /**
     * Find the position of the user with the given username.
     * @param string $username Username
     * @return resource The query results from MySQL
     */
    function getPosition($username){
      return (mysqli_query($this->conn, "SELECT Position FROM Users WHERE Username= $username"));
    }

    /**
     * Gets the student assistant ID from the user with the given username.
     * @param string $username Username
     * @return resource The query results from MySQL
     */
    function getStudassID($username){
      return (mysqli_query($this->conn,"SELECT StudentAssistantID FROM Users WHERE Username= $username"));
    }

  }

?>