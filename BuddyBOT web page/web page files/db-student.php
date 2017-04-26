<?php

  /**
   * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
   */

  class StudentDatabase
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
     * Gets table of distinct assignments this student has worked on
     * @param int $ID Student ID
     * @return resource The query results from MySQL
     */
    function assignmentsList($ID){
      return (mysqli_query($this->conn, "SELECT DISTINCT Assignment FROM Failures WHERE StudentID = $ID ORDER BY Assignment"));
    }

    /**
     * Gets table of distinct assignments all students has worked on
     * @return resource The query results from MySQL
     */
    function assignmentsList2(){
      return (mysqli_query($this->conn, "SELECT DISTINCT Assignment FROM Failures ORDER BY Assignment"));
    }

    /**
     * Gets the ID of this students student assistant
     * @param int $ID Student ID
     * @return int The student assistant's ID
     */
    function studassIDquery($ID){
      $studassIDquery = mysqli_query($this->conn, "SELECT StudentAssistantID FROM Users WHERE StudentID = $ID");
      $studassID_row = mysqli_fetch_array($studassIDquery);
      return $studassID_row[0];
    }

    /**
     * Gets the number of students this student's student assistant have.
     * @param int $studassID Student assistant ID
     * @return int The student assistant's ID
     */
    function studsInGroup($studassID){
      $studsInGroup = mysqli_query($this->conn, "SELECT COUNT(*) FROM Users WHERE Position = 'Student' AND StudentAssistantID = $studassID");
      $studsInGroup_row = mysqli_fetch_array($studsInGroup);
      return $studsInGroup_row[0];
      
    }

    /**
     * Gets the number of students attending the class TDT4100.
     * @return int The number of course attendants
     */
    function courseAttendants(){
      $courseAttendants = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) FROM Users WHERE Position = 'Student'");
      $courseattendants_row = mysqli_fetch_array($courseAttendants);
      return $courseattendants_row[0];
    }

    /**
     * Gets table of amount of failures on the specified assignment
     * @param int $staticAss Assignment number
     * @param int $ID Student ID
     * @return resource The query results from MySQL
     */
    function failuresOnAss($staticAss, $ID){
      return(mysqli_query($this->conn,"SELECT COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID = $ID AND Assignment = $staticAss"));
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
     * Gets the number of failures in total for all students
     * If $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @return int The number of failures in total
     */
    function failsByAll($ass){
      if($ass == -1){
        $failsByAll = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures");
      }
      else{
        $failsByAll = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE Assignment = $ass");
      }
      $failsbyall_row = mysqli_fetch_array($failsByAll);
      return $failsbyall_row[0];

    }

    /**
     * Gets table of all the different tags, with another column with number of failures this student has in specific tags.
     * If $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
     * @return resource The query results from MySQL
     */
    function tagDistribution($ass,$ID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures WHERE StudentID = $ID GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, COUNT(*) FROM Failures WHERE StudentID = $ID AND Assignment = $ass GROUP BY Tag ORDER BY COUNT(*) DESC"));
      }
    }

    /**
     * Gets the number of failures this student has.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
     * @return int The students' number of failures on the specified assignment
     */  
    function totalTags($ass, $ID){ 
      if($ass == -1){
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE StudentID = $ID");
      }
      else{
        $totalTags = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE StudentID = '$ID' AND Assignment = $ass");
      }
      $totalTags_row = mysqli_fetch_array($totalTags);
      return $totalTags_row[0];
    }

    /**
     * Gets the number of how many students have had any failures in TDT4100.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
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
     * Gets the number of how many failures this student has had the last 24 hours.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID Student ID
     * @return int The students' number of failures the last 24 hours
     */ 
    function failuresNoLimit($ass, $ID){
      if($ass == -1){
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE StudentID = $ID AND DateTime >= CURDATE() - INTERVAL 1 DAY");
      }
      else{
        $failuresNoLimit = mysqli_query($this->conn, "SELECT COUNT(*) FROM Failures WHERE StudentID = $ID AND DateTime >= CURDATE() - INTERVAL 1 DAY AND Assignment = $ass");
      }
      $countFailures24H_row = mysqli_fetch_array($failuresNoLimit);
      return $countFailures24H_row[0];
    }

    /**
     * Gets table of the last 10 failures this student has had.
     * A table with the columns tag and date
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $ID StudentID
     * @return resource The query results from MySQL
     */
    function recentFailures($ass, $ID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,1,19) FROM Failures WHERE StudentID = $ID ORDER BY DateTime DESC LIMIT 10 "));
      }
      else{
        return (mysqli_query($this->conn, "SELECT Tag, SUBSTRING(DateTime,1,19) FROM Failures WHERE StudentID = $ID AND Assignment = $ass ORDER BY DateTime DESC LIMIT 10 "));
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
     * Gets table of the numbers of failures each student in this student's student assistant have.
     * A table with the columns StudentID and count of failures.
     * if $ass-variable equals -1, query is for all assignments. Else, it's for the specified assignment.
     * @param int $ass Assignment number
     * @param int $studassID Student assistant ID
     * @return resource The query results from MySQL
     */
    function failsInGroup($ass, $studassID){
      if($ass == -1){
        return (mysqli_query($this->conn, "SELECT StudentID, COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $studassID) GROUP BY StudentID ORDER BY COUNT(*)"));
      }
      else{
        return (mysqli_query($this->conn, "SELECT StudentID, COUNT(*) FROM (Failures AS F NATURAL JOIN Users AS U) WHERE StudentID IN (SELECT DISTINCT StudentID FROM Users WHERE Position = 'Student' AND StudentAssistantID = $studassID) AND Assignment = $ass GROUP BY StudentID ORDER BY COUNT(*)"));
      }
    }

    /**
     * Gets the percentage of how many students who has worked on the specified exercise.
     * @param int $ass Assignment number
     * @param int $ex Exercise number
     * @param int $ID Student ID
     * @return int Percentage
     */
    function exUsers($ass,$ex,$ID){
      $exUsers = mysqli_query($this->conn, "SELECT COUNT(DISTINCT StudentID) AS Count From Failures WHERE Assignment = $ass AND Exercise = $ex AND StudentID = $ID");
      $exUsers_row = mysqli_fetch_array($exUsers);
      return round(100*($exUsers_row[0]));
    }
 
  }

?>