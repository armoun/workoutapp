<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['workoutid'])) 
	{
		$workoutIdFromURL = $_GET['workoutid'];

		//alle exercises deleten gekoppeld aan de workout
		mysqli_query($conn, "DELETE FROM mad_workouts_exercises WHERE workout_id = '$workoutIdFromURL'"); 

		//workout deleten
		mysqli_query($conn, "DELETE FROM mad_workouts WHERE id = '$workoutIdFromURL'"); 

		echo("workout verwijderd");

	}
	else
	{
		echo('workoutid niet in url');
	}

?>