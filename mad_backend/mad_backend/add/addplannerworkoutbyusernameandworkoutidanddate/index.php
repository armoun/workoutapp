<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['username']) && !empty($_GET['workoutid']) && !empty($_GET['date'])) 
	{
		$usernameFromURL = $_GET['username'];
		$workoutidFromUrl = $_GET['workoutid'];
		$dateFromUrl = $_GET['date'];

		//workout inputten
		//$result = mysqli_query($conn, "INSERT INTO mad_workouts (name, owner_username) VALUES ('$nameFromURL', '$usernameFromURL')"); 

        if(!($stmt = $conn->prepare("INSERT INTO mad_planner (datum, username, workout_id) VALUES (?,?,?)"))) {
            echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
        }
        
        if (!$stmt->bind_param("ssi", $dateFromUrl, $usernameFromURL, $workoutidFromUrl)) {
            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
        }
    
        if (!$stmt->execute()) {
            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
        }
        
		echo('workout added');
		


	}
	else
	{
		echo('not all parameters in URL');
	}

?>