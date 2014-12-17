<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$usernameFromURL = $_GET['username'];

	if ( (!empty($_GET["username"])) ) 
	{
		//workouts ophalen
		//$result = mysqli_query($conn, "SELECT id, name, owner_username FROM mad_workouts WHERE owner_username = '$usernameFromURL'"); 
		if(!($stmt = $conn->prepare("SELECT id, datum, username, workout_id FROM mad_planner WHERE username = ?"))) {
             echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
        }
        
        if (!$stmt->bind_param("s", $usernameFromURL)) {
            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
        }

        if (!$stmt->execute()) {
            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
        }

        $res = $stmt->get_result();
        
		$response = array();
		$posts = array();

		$row_cnt = $res->num_rows;

		if($row_cnt > 0 )
		{
			while($row = $res->fetch_assoc()) 
			{ 
				$id=$row['id']; 
				$datum=$row['datum']; 
				$username=$row['username'];
				$workout_id=$row['workout_id']; 

				$posts[] = array('id'=> $id, 'datum'=> $datum, 'username'=> $username, 'workout_id'=> $workout_id);

			} 

			$response['workouts'] = $posts;

			echo (json_encode($response));
		}
		else
		{
			echo 'username heeft geen workouts op deze dag';
		}
	}
	else
	{
		echo "no username in url";
	}


	
		

	?> 
