<?php

  /**
   * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
   */

  class LecturerDatabase
  {
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
     * Gets table of distinct assignments that has been worked on
     * @return resource The query results from MySQL
     */
    function assignmentsList(){
      return (mysqli_query($this->conn, "SELECT DISTINCT Assignment FROM Failures ORDER BY Assignment"));
    }
   
    /**
     * Gets table of all the different tags, with another column with number of failures there are on each tag.
     * If $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return resource The query results from MySQL
     */
    function tagDistribution($ass){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures WHERE Assignment = $ass GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
    }

    /**
     * Gets the number of failures on the specified assignment.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return int The number of failures on the specified assignment
     */    
    function totalTags($ass){
      if($ass == -1){
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures");
      }
      else{
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE Assignment = $ass");
      }
      $totalTags_row = mysqli_fetch_array($totalTags);
      return $totalTags_row[0];
    }

    /**
     * Gets the number of how many students have had any failures in TDT4100.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return int The number of students with failures in TDT4100
     */ 
    function uniqueUsers($ass){
      if($ass == -1){
        $uniqueUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures");
      }
      else{
        $uniqueUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures WHERE Assignment = $ass");
      }
      $uniqueUsers_row = mysqli_fetch_array($uniqueUsers);
      return $uniqueUsers_row[0]; 
    }

    /**
     * Gets table of all students, having a column with their studentID and another with the students' number of failures
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return resource The query results from MySQL
     */
    function strugStud($ass){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT StudentID, COUNT(*) FROM Failures GROUP BY StudentID ORDER BY COUNT(*) "));
      }
      else{
        return (mysqli_query($this->conn, "SELECT StudentID, COUNT(*) FROM Failures WHERE Assignment = $ass GROUP BY StudentID ORDER BY COUNT(*) "));
      }
    }

    /**
     * Gets the number of how many failures the last 24 hours.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return int The number of failures the last 24 hours
     */ 
    function failuresNoLimit($ass){
      if($ass == -1){
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY");
      }
      else{
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND Assignment = $ass");
      }
      $countFailures24H_row = mysqli_fetch_array($failuresNoLimit);
      return $countFailures24H_row[0];
    }

    /**
     * Gets table of the last 10 failures.
     * A table with the columns tag and date
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return resource The query results from MySQL
     */
    function recentFailures($ass){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,12,5) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY ORDER BY DateTime DESC LIMIT 10 "));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,12,5) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND Assignment = $ass ORDER BY DateTime DESC LIMIT 10 "));
      }
    }

    /**
     * Gets table of the top 5 visited links via BuddyBOT.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return resource The query results from MySQL
     */
    function mostVisited($ass){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Link, Tag, ROUND(AVG(Rating),1), COUNT(*) FROM (Feedback AS F NATURAL JOIN Resources AS R) WHERE Link != 'N/A' GROUP BY Tag, Link ORDER BY COUNT(*) DESC LIMIT 5"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Link, Tag, ROUND(AVG(Rating),1), COUNT(*) FROM (Feedback AS F NATURAL JOIN Resources AS R) WHERE Link != 'N/A' AND Assignment = $ass GROUP BY Tag ORDER BY COUNT(*) DESC LIMIT 5"));
      }
    }

    /**
     * Gets table of the top 5 rated links via BuddyBOT.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return resource The query results from MySQL
     */
    function bestRated($ass){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Link, Tag, ROUND(AVG(Rating),1), COUNT(*) FROM (Feedback AS F NATURAL JOIN Resources AS R) GROUP BY Tag, Link ORDER BY ROUND(AVG(Rating),1) DESC LIMIT 5"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Link, Tag, ROUND(AVG(Rating),1), COUNT(*) FROM (Feedback AS F NATURAL JOIN Resources AS R) WHERE Link != 'N/A' AND Assignment = $ass GROUP BY Tag ORDER BY ROUND(AVG(Rating),1) DESC LIMIT 5"));
      }
    }

    /**
     * Gets the percentage of how many students has sent in failures on the exercise within the assignment.
     * @param int $ass Assignment number
     * @param int $ex Exercise number
     * @param int $assUsers Amount of students working on assignment
     * @return int Percentage
     */
    function exUsers($ass,$ex,$assUsers){
      $exUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) AS Count From Failures WHERE Assignment = $ass AND Exercise = $ex");
      $exUsers_row = mysqli_fetch_array($exUsers);
      return round(100*($exUsers_row[0]/$assUsers));
    }

    /**
     * Gets the number of users who has worked on the specified assignment
     * @param int $ass Assignment number
     * @return int 
     */
    function assUsers($ass){
      $assUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures WHERE Assignment = $ass");
      $assUsers_row = mysqli_fetch_array($assUsers);
      return $assUsers_row[0];
    }

  }
  
?>
