<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$workoutid;
	$exerciseid;

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['username']) && !empty($_GET['workoutname']) && !empty($_GET['exercisename'])) 
	{
		$usernameFromURL = $_GET['username'];
		$workoutnameFromURL = $_GET['workoutname'];
		$exercisenameFromURL = $_GET['exercisename'];


		$result = mysqli_query($conn, "SELECT id FROM mad_workouts WHERE name = '$workoutnameFromURL' AND owner_username = '$usernameFromURL'");

		while($row=mysqli_fetch_array($result)) 
		{ 

			$workoutid=$row['id']; 
		} 

		$result2 = mysqli_query($conn, "SELECT id FROM mad_exercises WHERE name = '$exercisenameFromURL'");

		while($row=mysqli_fetch_array($result2)) 
		{ 

			$exerciseid=$row['id']; 
		}


		//alle exercises deleten gekoppeld aan de workout
		mysqli_query($conn, "DELETE FROM mad_workouts_exercises WHERE workout_id = '$workoutid' AND exercise_id = '$exerciseid' "); 

		echo("exercise verwijderd");

	}
	else
	{
		echo('params niet in url');
	}

?>