<?php 


  //Includes db-studass to get access to MySQL-queries
  include ("db-studass.php");
  session_start(); 

  //sets assignment from register.php. Starts at -1, which means all assignments. Stores this students assistants ID in $ID-variable and $username for use in header.
  //Checks if the $_SESSION variable is set to prevent unwanted access.
  //if not: return to login page.
  if ($_GET['ass'] AND isset($_SESSION['studassID'])) {
    $ID = $_SESSION['studassID'];
    $ass = $_GET['ass'];
    $username = $_SESSION['username'];
  }

  else if (isset($_SESSION['studassID'])) {
    $ID = $_SESSION['studassID'];
    $ass = $_SESSION['ass'];
    $username = $_SESSION['username'];
  }

  else {
    header("Location: login.html");
  }

  $barColor = '4678B2';
  $tags = array();
  $values = array();
  $namesWithFailures = array(); 
  $stud_failures = array(); 
  $failureList = array();
  $assignments = array();
  $studIDs = array();
  $names = array(); 
  $averagelist = array();
  $graphNames = array();
  $failurenums = array();
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>PU38 - Dashboard 2</title>

  <!-- Bootstrap Core CSS -->
  <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- MetisMenu CSS -->
  <link href="../vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

  <!-- Custom CSS -->
  <link href="../dist/css/sb-admin-2.css" rel="stylesheet">

  <!-- Morris Charts CSS -->
  <link href="../vendor/morrisjs/morris.css" rel="stylesheet">

  <!-- Custom Fonts -->
  <link href="../vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<link rel="stylesheet" type="text/css" href="repTest.css"> 
<?php

  //connects to database
  $db = new StudassDatabase();
  $conn = $db->getConnection();

  //List of queries. Explination in db-studass.php
  $assignmentsList = $db->assignmentsList();
  $tagDistribution = $db->tagDistribution($ass,$ID);
  $totalTags_amt = $db->totalTags($ass,$ID); 
  $uniqueUsers_amt = $db->uniqueUsers($ass,$ID); 
  $strugStud = $db->strugStud($ass);
  $countFailures24H = $db->failuresNoLimit($ass,$ID);
  $recentFailures = $db->recentFailures($ass,$ID); 
  $mostVisited = $db->mostVisited($ass);
  $bestRated = $db->bestRated($ass);
  $failprStudent = $db->failprStudent($ass,$ID);
  $assignmentsList2 = $db->assignmentsList();
  $studNamesInGroup = $db->studNamesInGroup($ID);
  $studsInGroup_amt = $db->studsInGroup($ID);
  $getStudIDs = $db->getStudIDs($ID);

  
  //fills the array $names with the different names of the students in this group
  while($row2 = mysqli_fetch_array($studNamesInGroup)) {
    $fullName = $row2[0] . " " .$row2[1];
    array_push($names, (string) $fullName);
  }

  //fills the array $studIDs with the different student IDs in this group
  while($rad = mysqli_fetch_array($getStudIDs)) {
    array_push($studIDs, $rad[0]);
  }
  
  //fills the array $assignments with the different assignmentnumber
  while($rad = mysqli_fetch_array($assignmentsList2)) {
    array_push($assignments, $rad[0]);
  } 

  /**
   * Failure progress on assignments-graph.
   * Fills the array $failureList with the information from the "studFailOnAss"-query about the different students in the student group. 
   * Then fills in the average of the students in total on each exercise at the end of $failureList
   */
  $j = 0;
  while($j < sizeof($studIDs)) {
    $newID = $studIDs[$j]; 
      for($i=0; $i < sizeof($assignments); $i++) { 
        $staticAss = $assignments[$i]; 
        $studFailOnAss = $db->studFailOnAss($newID,$staticAss);
        array_push($failureList, $studFailOnAss);
      }
    $j += 1;
  }

  $j = 0;
  $counter = 0;
  $extra = 0;
  while($j < sizeof($assignments)) { 
    while($counter < sizeof($names)) {  
      $averagelist[$j] += $failureList[$counter*sizeof($assignments) + $extra];   
      $counter +=1; 
    }
    $j+=1;
    $extra+=1;
    $counter = 0;
  }

  for($i=0; $i< sizeof($assignments); $i++) {
    $averagelist[$i] = $averagelist[$i]/sizeof($names);
    $failureList[sizeof($failureList)] = $averagelist[$i];
  }

  //same array as $names, just with the name 'Total' added at the ending of the array. For use in the Failure progress on assignments-graph.
  $graphNames = $names;
  $graphNames[sizeof($graphNames)] = 'Total';

  /**
   * Failures per student in your group-graph
   * Fills the array $namesWithFailures with the names of the student in your group who has had any failures on the specified assignment.
   * Fills the array $stud_failures with the number of failures this student has on the specified assignment.
   */
  while ($row_fs = mysqli_fetch_array($failprStudent)) {
    $fullName = $row_fs[0] . " " .$row_fs[1];
    array_push($namesWithFailures, (string) $fullName);
    array_push($stud_failures, $row_fs[2]);
  }

  /**
   * Tag distribution on assignment.
   * Fills the arrays $tags and $values with information from the $tagDistribution query.
   */
  while ($row = mysqli_fetch_array($tagDistribution)) {
    array_push($tags, (string) $row[0]);
    array_push($values, $row[1]);
  }

  /**
   * Failures per student-graph.
   * Sets the four variables for the amount of failures different students have. Uses the query $strugStud.
   */
  $count0_3 = 0;
  $count4_10 = 0;
  $count11_20 = 0;
  $count21_more = 0;

  while ($row = mysqli_fetch_array($strugStud)) {
    if($row[1] > 0 AND $row[1] <= 3){
      $count0_3 += 1;
    }
    if($row[1] > 3 AND $row[1] <= 10){
      $count4_10 += 1;
    }
    if($row[1] > 10 AND $row[1] <= 20){
      $count11_20 += 1;
    }
    if($row[1] > 20 AND $row[1] < 100000){
      $count21_more += 1;
    }
  }

  /**
   * Exercise attempts.
   * Finds the percentage of how many students has sent in failures on each exercise within the assignment. Uses the exUsers-query
   * Always six exercises on each assignment.
   */
  $assUsers = $db->assUsers($ass,$ID);
  $percentage_ex1 = $db->exUsers($ass,1,$ID,$assUsers);
  $percentage_ex2 = $db->exUsers($ass,2,$ID,$assUsers);
  $percentage_ex3 = $db->exUsers($ass,3,$ID,$assUsers);
  $percentage_ex4 = $db->exUsers($ass,4,$ID,$assUsers);
  $percentage_ex5 = $db->exUsers($ass,5,$ID,$assUsers);
  $percentage_ex6 = $db->exUsers($ass,6,$ID,$assUsers);
?>

<!-- Chart code starts here -->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">

  /**
   * Load the Visualization API and the corechart package.
   * Set a callback to run when the Google Visualization API is loaded.
   */
  google.load('visualization', '1.0', {'packages':['corechart']});
  google.setOnLoadCallback(drawChart);
  
  /**
   * Callback that creates and populates a data table, instantiates the charts, passes in the data and draws them.
   * data: Tag distribution on assignment-graph
   * data2: Failures per student-graph
   * data3: Failures in your student group-graph
   * sets options, which is the style of the graphs.
   */
  function drawChart() {

    var data = google.visualization.arrayToDataTable([
      ['Tag', '# failures', { role: 'style' } ],
      <?php
        $counter = 0;
        $amount = 0;
        while($counter < (sizeof($tags)-1) AND $amount < 9){ 
      ?>
          ["<?php echo $tags[$counter];?>", <?php echo $values[$counter]; ?>, 'color: <?php echo $barColor ?>'],
          <?php
            $counter +=1;
            $amount += 1;
        }
          ?>
        ["<?php echo $tags[$counter];?>", <?php echo $values[$counter]; ?>, 'color: <?php echo $barColor ?>']
    ]);

    var data2 = new google.visualization.DataTable();
      data2.addColumn('string', 'Topping');
      data2.addColumn('number', 'Slices');
      data2.addRows([
        ['0-3', <?php echo $count0_3; ?>],
        ['4-10', <?php echo $count4_10; ?>],
        ['10-20', <?php echo $count11_20; ?>],
        ['20+', <?php echo $count21_more; ?>]
    ]);

    var data3 = google.visualization.arrayToDataTable([
      ['Name', '# failures', { role: 'style' } ],
      <?php 
        $index = 0;
        while ($index < $studsInGroup_amt-1) {
      ?>
          ["<?php echo $namesWithFailures[$index];?>", <?php echo $stud_failures[$index]; ?>, 'color: <?php echo $barColor ?>'],
          <?php
            $index += 1; 
        }
          ?>
        ["<?php echo $namesWithFailures[$index];?>", <?php echo $stud_failures[$index]; ?>, 'color: <?php echo $barColor ?>']
    ]);

 
    var options = {
                   'width':650,
                   'height':200,
                   chartArea:{left:100,top:0,width:"100%",height:"100%"}
                  };

    var options2 = {
                   chartArea:{left: 140, top: 0, bottom: 0,width:"100%",height:"100%"}
                  };   
               

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
    chart.draw(data, options);
    var chart2 = new google.visualization.PieChart(document.getElementById('chart_div2'));
    chart2.draw(data2, options2); 
    var chart3 = new google.visualization.BarChart(document.getElementById('chart_div3'));
    chart3.draw(data3, options);            
  }
</script>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">

  /**
   * Load the Current API and the corechart and bar package.
   * Set a callback to run when the Google Visualization API is loaded.
   */
  google.charts.load('current', {packages: ['corechart', 'bar']});
  google.charts.setOnLoadCallback(drawBasic);

  /**
   * Callback that creates and populates a data table, instantiates the charts, passes in the data and draws them.
   * data4: Failure progress on assignments-graph
   * sets options, which is the style of the graphs.
   */
  function drawBasic() {

    var data4= google.visualization.arrayToDataTable([
      ['Assignment',
      <?php
        $counter = 0;  
        while($counter < sizeof($graphNames) -1 ){ ?>
          '<?php echo $graphNames[$counter];?>', 
          <?php
            $counter +=1;
        }
          ?>
        '<?php echo $graphNames[$counter];?>'], 


      <?php
        $counter = 0;
        $counter2 = 0;
        $incr = 0;
        while ($counter < sizeof($assignments)-1){ 
      ?> 
          ["A<?php echo $assignments[$counter];?>",  
          <?php
            while($counter2 < sizeof($graphNames) -1){ 
          ?> 
              <?php echo $failureList[($counter2 * sizeof($assignments)) + $incr]; ?>, 
              <?php
                $counter2 += 1;
            }
              ?>
          <?php echo $failureList[($counter2 * sizeof($assignments)) + $incr]; ?> ],
          <?php
            $counter2 = 0;
            $counter += 1;
            $incr += 1;
        }
          ?>
      
        ["A<?php echo $assignments[$counter];?>",  
        <?php
          $counter3 = 0;
          while($counter3 < sizeof($graphNames) -1){ 
        ?> 
            <?php echo $failureList[($counter3 * sizeof($assignments)) + $incr]; ?>, 
            <?php
              $counter3 += 1;
          }
            ?>
          <?php echo $failureList[($counter3 * sizeof($assignments)) + $incr]; ?> ]
    ]);


  var options4 = {
                  title: '#failures',
                  curveType: 'function',
                  legend: { position: 'bottom' }
                };

    var chart4 = new google.visualization.LineChart(document.getElementById('curve_chart'));
    chart4.draw(data4, options4);
  }
</script>

<!-- Navigation -->
<body>
  <div id="wrapper">
    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="http://folk.ntnu.no/ingriehe/index.html">BuddyBOT - Dashboard for student assistants</a>
        <a name = "pagetop"></a>
      </div>
      <!-- /.navbar-header -->
      <ul class="nav navbar-top-links navbar-right">
        <?php 
          if ($ass != -1){ 
        ?>
            <!-- /.dropdown -->
            <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#"> Exercise attempts
                <i class="fa fa-tasks fa-fw"></i> <i class="fa fa-caret-down"></i>
              </a> 
              <ul class="dropdown-menu dropdown-tasks">
                <li>
                  <a href="#">
                    <div>
                      <p>
                        <strong>Exercise 1</strong>
                        <span class="pull-right text-muted"> <?php echo $percentage_ex1 ?> % Covered</span>
                      </p>
                                <div class="progress progress-striped active">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex1 ?>%">
                                        <span class="sr-only">40% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="#">
                            <div>
                                <p>
                                    <strong>Exercise 2</strong>
                                    <span class="pull-right text-muted"><?php echo $percentage_ex2 ?> % Covered</span>
                                </p>
                                <div class="progress progress-striped active">
                                    <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex2 ?>%">
                                        <span class="sr-only">20% Complete</span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                      <a href="#">
                        <div>
                          <p>
                            <strong>Exercise 3</strong>
                            <span class="pull-right text-muted"><?php echo $percentage_ex3 ?> % Covered</span>
                          </p>
                          <div class="progress progress-striped active">
                            <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex3 ?>%">
                              <span class="sr-only">60% Complete (warning)</span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                      <a href="#">
                        <div>
                          <p>
                            <strong>Exercise 4</strong>
                            <span class="pull-right text-muted"><?php echo $percentage_ex4 ?> % Covered</span>
                          </p>
                          <div class="progress progress-striped active">
                            <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex4 ?>%">
                              <span class="sr-only">80% Complete (danger)</span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                      <a href="#">
                        <div>
                          <p>
                            <strong>Exercise 5</strong>
                            <span class="pull-right text-muted"> <?php echo $percentage_ex5 ?> % Covered</span>
                          </p>
                          <div class="progress progress-striped active">
                            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex5 ?>%">
                              <span class="sr-only">40% Complete (success)</span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                      <a href="#">
                        <div>
                          <p>
                            <strong>Exercise 6</strong>
                            <span class="pull-right text-muted"><?php echo $percentage_ex6 ?> % Covered</span>
                          </p>
                          <div class="progress progress-striped active">
                            <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: <?php echo $percentage_ex6 ?>%">
                              <span class="sr-only">20% Complete</span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </li> 
              </ul>
        <?php 
          }
        ?>
          <!-- /.dropdown-tasks -->
          </li>
          <!-- /.dropdown -->
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#"><?php echo $username ?>
              <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li><a href="endsess.php"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
            </ul>
            <!-- /.dropdown-user -->
          </li>
          <!-- /.dropdown -->
      </ul>
      <!-- /.navbar-top-links -->
      <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
          <ul class="nav" id="side-menu">
            <li><a href="#pagetop"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a></li>
              <li><a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Select assignment<span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                  <li><a href="report-studass.php?ass=-1">All assignments</a></li>
                    <?php
                      while ($row = mysqli_fetch_array($assignmentsList)) {
                        $ass_nr = $row[0];
                    ?>
                        <li><a href = "report-studass.php?ass=<?php echo $ass_nr; ?>">Assignment <?php echo $ass_nr ?></a></li>
                    <?php
                      }
                    ?>                     
                </ul>
                <!-- /.nav-second-level -->
              </li>
              <li><a href="#link_review"><i class="fa fa-link fa-fw"></i> Link review</a></li>
          </ul>
        </div>
        <!-- /.sidebar-collapse -->
      </div>
      <!-- /.navbar-static-side -->
    </nav>
    <div id="page-wrapper">
      <div class="row">
        <div class="col-lg-12">
          <?php if($ass == -1){ ?>
              <h1 class="page-header">Dashboard for all assignments</h1>
          <?php
            }
            else { ?>
              <h1 class="page-header">Dashboard for Assignment <?php echo $ass ?></h1>
          <?php
            }
          ?>
        </div>
        <!-- /.col-lg-12 -->
      </div>
      <!-- /.row -->
      <div class="row">
        <div class="col-lg-3 col-md-6">
          <div class="panel panel-red">
            <div class="panel-heading">
              <div class="row">
                <div class="col-xs-3">
                  <i class="fa fa-tags fa-5x"></i>
                </div>
                <div class="col-xs-9 text-right">
                  <div class="huge"><?php echo $totalTags_amt?></div>
                  <div>Failures in total</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6">
          <div class="panel panel-green">
            <div class="panel-heading">
              <div class="row">
                <div class="col-xs-3">
                  <i class="fa fa-clock-o fa-5x"></i>
                </div>
                <div class="col-xs-9 text-right">
                  <div class="huge"><?php echo $countFailures24H?></div>
                  <div>Failures last 24h</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6">
          <div class="panel panel-yellow">
            <div class="panel-heading">
              <div class="row">
                <div class="col-xs-3">
                  <i class="fa fa-warning fa-5x"></i>
                </div>
                <div class="col-xs-9 text-right">
                  <div class="huge"><?php echo $count21_more?></div>
                  <div>Struggling students in your group</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6">
          <div class="panel panel-primary">
            <div class="panel-heading">
              <div class="row">
                <div class="col-xs-3">
                  <i class="fa fa-user fa-5x"></i>
                </div>
                <div class="col-xs-9 text-right">
                  <div class="huge"><?php echo $uniqueUsers_amt?></div>
                  <div>Students in your group</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- /.row -->
      <div class="row">
        <div class="col-lg-8">
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-bar-chart-o fa-fw"></i> Tag distribution on assignment
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
              <div id="chart_div" class = "BarChart"></div>
              <!--<div id="morris-area-chart"></div>-->
            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-bar-chart-o fa-fw"></i> Failures per student in your group
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
              <div id="chart_div3" class="BarChart"></div>
              <!--<div id="morris-area-chart"></div>-->
            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
          <div class="panel panel-default">
            <div class="panel-heading">
              <a name = "link_review"></a>
                <i class="fa fa-link fa-fw"></i> Most visited links
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
              <div class="table-responsive">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th>Link</th>
                      <th>Tag</th>
                      <th>Avg rating</th>
                      <th>Times visited</th>
                    </tr>
                  </thead>
                  <tbody>
                    <?php
                      while ($row = mysqli_fetch_array($mostVisited)) {
                        $mv_link = $row[0];
                        $mv_tag = $row[1];
                        $mv_rating = $row[2];
                        $mv_visited = $row[3];
                    ?>
                        <tr>
                          <td><a href = "<?php echo $mv_link ?>"><?php echo $mv_link ?></a></td>
                          <td><?php echo $mv_tag ?></td>
                          <td><?php echo $mv_rating ?></td>
                          <td><?php echo $mv_visited ?></td>
                        </tr>
                    <?php
                      }
                    ?>
                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
              <!-- /.row -->
            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-link fa-fw"></i> Best ranked links
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
              <div class="table-responsive">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th>Link</th>
                      <th>Tag</th>
                      <th>Avg rating</th>
                      <th>Times visited</th>
                    </tr>
                  </thead>
                  <tbody>
                    <?php
                      while ($row = mysqli_fetch_array($bestRated)) {
                        $br_link = $row[0];
                        $br_tag = $row[1];
                        $br_rating = $row[2];
                        $br_visited = $row[3];
                    ?>
                        <tr>
                          <td><a href = "<?php echo $br_link ?>"><?php echo $br_link ?></a></td>
                          <td><?php echo $br_tag ?></td>
                          <td><?php echo $br_rating ?></td>
                          <td><?php echo $br_visited ?></td>
                        </tr>
                    <?php
                      }
                    ?>
                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
              <!-- /.row -->
            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
        </div>
        <!-- /.col-lg-8 -->
        <div class="col-lg-4">
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-warning fa-fw"></i> # Failures per student
            </div>
            <div class="panel-body">
              <div id="chart_div2" class = "BarChart2"></div>
            </div>
            <!-- /.panel-body -->
          </div>  
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-level-up fa-fw"></i> Failure progress on assignments
            </div>
            <div class="panel-body">
              <div id="curve_chart" class = "BarChart2"></div>
            </div>
            <!-- /.panel-body -->
          </div>  
          <div class="panel panel-default">
            <div class="panel-heading">
              <i class="fa fa-clock-o fa-fw"></i> Most recent failures [last 24h]
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
              <div class="list-group">
                <?php 
                  while ($rf_row = mysqli_fetch_array($recentFailures)){
                    $rf_tag = $rf_row[0];
                    $rf_time = $rf_row[1];
                ?>
                    <a class="list-group-item">
                      <i class="fa fa-times fa-fw"></i><?php echo $rf_tag ?>
                      <span class="pull-right text-muted small"><em><?php echo $rf_time ?></em></span>
                    </a>
                <?php
                  } 
                ?>
              </div>
              <!-- /.list-group -->
            </div>
            <!-- /.panel-body -->
          </div>
          <!-- /.panel -->
          <!-- /.panel-body -->
        </div>
        <!-- /.panel .chat-panel -->
        </div>
        <!-- /.col-lg-4 -->
      </div>
      <!-- /.row -->
    </div>
    <!-- /#page-wrapper -->
  </div>
  <!-- /#wrapper -->

  <!-- jQuery -->
  <script src="../vendor/jquery/jquery.min.js"></script>
  <!-- Bootstrap Core JavaScript -->
  <script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
  <!-- Metis Menu Plugin JavaScript -->
  <script src="../vendor/metisMenu/metisMenu.min.js"></script>
  <!-- Morris Charts JavaScript -->
  <script src="../vendor/raphael/raphael.min.js"></script>
  <script src="../vendor/morrisjs/morris.min.js"></script>
  <script src="../data/morris-data.js"></script>
  <!-- Custom Theme JavaScript -->
  <script src="../dist/js/sb-admin-2.js"></script>
</body>
</html>
<style type="text/css">
.BarChart2{
}
</style>
<div class = "padding-footer"><div class="footer"><br><br></div></div>
