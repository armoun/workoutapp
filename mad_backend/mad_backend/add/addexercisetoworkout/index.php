<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['workoutid']) && !empty($_GET['exerciseid'])) 
	{
		$workoutidFromURL = $_GET['workoutid'];
		$exerciseidFromURL = $_GET['exerciseid'];
		$reps = "10, 10, 10";

		//workout inputten
		//$result = mysqli_query($conn, "INSERT INTO mad_workouts_exercises (workout_id, exercise_id, reps) VALUES ('$workoutidFromURL', '$exerciseidFromURL', '$reps')"); 
        
        if(!($stmt = $conn->prepare("INSERT INTO mad_workouts_exercises (workout_id, exercise_id, reps) VALUES (?,?,?)"))) {
                             echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
                        }

        if (!$stmt->bind_param("iii", $workoutidFromURL, $exerciseidFromURL, $reps)) {
                            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
                        }

        if (!$stmt->execute()) {
                            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                        }

		echo('exercise added');
	}
	else
	{
		echo('not all data in url');
	}

?>