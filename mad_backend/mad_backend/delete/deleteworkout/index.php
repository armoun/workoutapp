<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$workoutid;

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['workoutname']) && !empty($_GET['username'])) 
	{
		$workoutnameFromURL = $_GET['workoutname'];
		$usernameFromURL = $_GET['username'];

		$result = mysqli_query($conn, "SELECT id FROM mad_workouts WHERE name = '$workoutnameFromURL' AND owner_username = '$usernameFromURL'"); 

		while($row=mysqli_fetch_array($result)) 
		{ 

			$workoutid=$row['id']; 

		} 

		//alle exercises deleten gekoppeld aan de workout
		mysqli_query($conn, "DELETE FROM mad_workouts_exercises WHERE workout_id = '$workoutid'"); 

		//workout deleten
		mysqli_query($conn, "DELETE FROM mad_workouts WHERE name = '$workoutnameFromURL' AND owner_username = '$usernameFromURL'"); 

		echo("workout verwijderd");

	}
	else
	{
		echo('workoutname en/of username niet in url');
	}

?>