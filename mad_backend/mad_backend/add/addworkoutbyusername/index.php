<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['username']) && !empty($_GET['name'])) 
	{
		$usernameFromURL = $_GET['username'];
		$nameFromURL = $_GET['name'];


		//workouts ophalen
		$result = mysqli_query($conn, "SELECT id, name, owner_username FROM mad_workouts WHERE owner_username = '$usernameFromURL' AND name = '$nameFromURL'"); 

		$response = array();
		$posts = array();

		$row_cnt = $result->num_rows;

		if($row_cnt > 0 )
		{
			echo ("workout name bestaat al");
		}
		else
		{
			//workout inputten
			$result = mysqli_query($conn, "INSERT INTO mad_workouts (name, owner_username) VALUES ('$nameFromURL', '$usernameFromURL')"); 

			echo('workout added');
		}


	}
	else
	{
		echo('no username or name in url');
	}

?>