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

	if (!empty($_GET['username']) && !empty($_GET['workoutname']) && !empty($_GET['date'])) 
	{
		$usernameFromURL = $_GET['username'];
		$workoutnameFromUrl = $_GET['workoutname'];
		$dateFromUrl = $_GET['date'];

		//workout inputten
		//$result = mysqli_query($conn, "INSERT INTO mad_workouts (name, owner_username) VALUES ('$nameFromURL', '$usernameFromURL')"); 


		$result = mysqli_query($conn, "SELECT id FROM mad_workouts WHERE name = '$workoutnameFromUrl' AND owner_username = '$usernameFromURL'");

		while($row=mysqli_fetch_array($result)) 
		{ 

			$workoutid=$row['id']; 

		} 


		$resultCount = mysqli_query($conn, "SELECT count(*) from mad_planner WHERE workout_id = '$workoutid' AND username = '$usernameFromURL' AND datum = '$dateFromUrl' ");

		if($resultCount < 1)
		{

	        if(!($stmt = $conn->prepare("INSERT INTO mad_planner (datum, username, workout_id) VALUES (?,?,?)"))) {
	            echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
	        }
	        
	        if (!$stmt->bind_param("ssi", $dateFromUrl, $usernameFromURL, $workoutid)) {
	            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
	        }
	    
	        if (!$stmt->execute()) {
	            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
	        }
	        
			echo('workout added');

		}
		else
		{
			echo('workout bestaat al op die dag');
		}
		


	}
	else
	{
		echo('not all parameters in URL');
	}

?>