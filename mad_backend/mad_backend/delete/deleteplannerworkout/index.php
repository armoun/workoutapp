<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$workoutid;

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['workoutname']) && !empty($_GET['username']) && !empty($_GET['date']))
	{
		$WorkoutnameFromURL = $_GET['workoutname'];
		$UsernameFromURL = $_GET['username'];
		$DateFromURL = $_GET['date'];

		$result = mysqli_query($conn, "SELECT id FROM mad_workouts WHERE name = '$WorkoutnameFromURL' AND owner_username = '$UsernameFromURL'");

		while($row=mysqli_fetch_array($result)) 
		{ 

			$workoutid=$row['id']; 
		} 


		//exercise deleten
		mysqli_query($conn, "DELETE FROM mad_planner WHERE username = '$UsernameFromURL' AND workout_id = '$workoutid' AND datum = '$DateFromURL' "); 

		echo("workout verwijderd van planner");

	}
	else
	{
		echo('username en/of workoutname en/of date niet in url');
	}

?>