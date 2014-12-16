<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['exerciseid'])) 
	{
		$exerciseIdFromURL = $_GET['exerciseid'];

		//exercise deleten
		mysqli_query($conn, "DELETE FROM mad_workouts_exercises WHERE id = '$exerciseIdFromURL'"); 

		echo("exercise verwijderd van workout");

	}
	else
	{
		echo('exerciseid niet in url');
	}

?>