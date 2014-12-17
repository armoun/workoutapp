<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['id'])) 
	{
		$IdFromURL = $_GET['id'];

		//exercise deleten
		mysqli_query($conn, "DELETE FROM mad_planner WHERE id = '$IdFromURL'"); 

		echo("workout verwijderd van planner");

	}
	else
	{
		echo('id niet in url');
	}

?>