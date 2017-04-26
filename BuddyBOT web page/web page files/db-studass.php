<?php

  /**
   * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
   */

  class StudassDatabase
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
     * Gets table of all the different tags, with another column with number of failures the students in this group has on the specific tags.
     * If $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student assistant ID
     * @return resource The query results from MySQL
     */
    function tagDistribution($ass,$ID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID') GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures WHERE Assignment = $ass AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID') GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
    }

    /**
     * Gets the number of failures the students in this group have.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
     * @return int The number of failures on the specified assignment within the whole group
     */  
    function totalTags($ass, $ID){
      if($ass == -1){
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      }
      else{
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE Assignment = $ass AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID')");
      }
      $totalTags_row = mysqli_fetch_array($totalTags);
      return $totalTags_row[0];
    }

    /**
     * Gets the number of how many students in this student group who have had any failures in TDT4100.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
     * @return int The number of students in the group with failures in TDT4100
     */ 
    function uniqueUsers($ass, $ID){
      if($ass == -1){
        $uniqueUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      }
      else{
        $uniqueUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures WHERE Assignment = $ass AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
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
     * Gets the number of how many failures there has been the last 24 hours in your student group.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student assistant ID
     * @return int The number of failures the last 24 hours
     */ 
    function failuresNoLimit($ass, $ID){
      if($ass == -1){
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      }
      else{
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND Assignment = $ass AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      }
      $countFailures24H_row = mysqli_fetch_array($failuresNoLimit);
      return $countFailures24H_row[0];
    }

    /**
      * Gets table of the last 10 failures the students in this group has had.
      * A table with the columns tag and date
      * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
      * @param int $ass Assignment number
      * @param int $ID Student assistant ID
      * @return resource The query results from MySQL
      */
    function recentFailures($ass, $ID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,12,5) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID') ORDER BY DateTime DESC LIMIT 10 "));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,12,5) FROM Failures WHERE DateTime >= CURDATE() - INTERVAL 1 DAY AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID) AND Assignment = $ass ORDER BY DateTime DESC LIMIT 10 "));
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
     * Gets the number of failures each student in this group has had on the specified assignment
     * Table with the columns name, lastname and amount of failures.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student assistant ID
     * @return resource The query results from MySQL
     */
    function failprStudent($ass, $ID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Firstname, SUBSTRING(Lastname,1,1), COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID') GROUP BY StudentID ORDER BY Firstname"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Firstname, SUBSTRING(Lastname,1,1) AS Lname, COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = '$ID') AND Assignment = $ass GROUP BY StudentID ORDER BY Firstname"));
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
     * Gets the names of the students in this group.
     * Table with the columns first name and last name(shortened).
     * @param int $ID Student assistant ID
     * @return resource The query results from MySQL
     */
    function studNamesInGroup($ID){
      return (mysqli_query($this->conn, "SELECT Firstname, SUBSTRING(Lastname,1,1) FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID"));
    }


    /**
     * Gets the number of students in this group.
     * @param int $ID Student assistant ID
     * @return int Number of students in group
     */
    function studsInGroup($ID){
      $studsInGroup = mysqli_query($this->conn, "SELECT COUNT(*) FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID");
      $studsInGroup_row = mysqli_fetch_array($studsInGroup);
      return $studsInGroup_row[0];
    }


    /**
     * Gets a table with the student IDs of the students in this group.
     * @param int $ID Student assistant ID
     * @return resource The query results from MySQL
     */
    function getStudIDs($ID){
      return (mysqli_query($this->conn, "SELECT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID")); 
    }


    /**
     * Gets the number of failures the student with the specified ID has on the assignment.
     * @param int $ID Student ID
     * @param int $ass Assignment variable
     * @return int Number of failures on the assignment
     */
    function studFailOnAss($ID,$ass){
      $studFailOnAss = mysqli_query($this->conn, "SELECT COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID = $ID AND Assignment = $ass");
      $studFailOnAss_row = mysqli_fetch_array($studFailOnAss);
      return $studFailOnAss_row[0];
    }

    /**
     * Gets the percentage of how many students has sent in failures on the exercise within the assignment.
     * @param int $ass Assignment number
     * @param int $ex Exercise number
     * @param int $ID Student assistant ID
     * @param int $assUsers Amount of students working on assignment
     * @return int Percentage
     */
    function exUsers($ass,$ex,$ID,$assUsers){
      $exUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) AS Count From Failures WHERE Assignment = $ass AND Exercise = $ex AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      $exUsers_row = mysqli_fetch_array($exUsers);
      return round(100*($exUsers_row[0]/$assUsers));
    }

    /**
     * Gets the number of students in the group who has worked on the specified assignment
     * @param int $ass Assignment number
     * @param int $ID Student assistant ID
     * @return int 
     */
    function assUsers($ass,$ID){
      $assUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) From Failures WHERE Assignment = $ass AND StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $ID)");
      $assUsers_row = mysqli_fetch_array($assUsers);
      return $assUsers_row[0];
    }

  }

?>