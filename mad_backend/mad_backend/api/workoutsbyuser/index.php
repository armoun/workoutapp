<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$usernameFromURL = $_GET['username'];

	if (!empty($_GET)) 
	{
		//workouts ophalen
		$result = mysqli_query($conn, "SELECT id, name, owner_username FROM mad_workouts WHERE owner_username = '$usernameFromURL'"); 

		$response = array();
		$posts = array();

		$row_cnt = $result->num_rows;

		if($row_cnt > 0 )
		{
			while($row=mysqli_fetch_array($result)) 
			{ 
				$id=$row['id']; 
				$name=$row['name']; 
				$owner_username=$row['owner_username']; 

				$result1 = mysqli_query($conn, "SELECT id, workout_id, exercise_id, reps FROM mad_workouts_exercises WHERE workout_id = '$id'");

				$row_cnt1 = $result1->num_rows;
				if($row_cnt1 > 0 )
				{
					while($row1=mysqli_fetch_array($result1))
					{
						$exercise_id_temp=$row1['exercise_id']; 
						$result2 = mysqli_query($conn, "SELECT name FROM mad_exercises WHERE id = '$exercise_id_temp'"); 

						$row2=mysqli_fetch_array($result2);
						
						$id_row=$row1['id']; 
						$workout_id=$row1['workout_id']; 
						$exercise_id=$row1['exercise_id'];
						$reps=$row1['reps'];
						$exercise_name=$row2['name'];

						$exercises[] = array('id_row'=> $id_row, 'workout_id'=> $workout_id, 'exercise_id'=> $exercise_id, 'exercise_name'=> $exercise_name, 'reps'=> $reps);
						
					}
				}

				$posts[] = array('id'=> $id, 'name'=> $name, 'owner_username'=> $owner_username, 'exercises' => $exercises);

			} 

			$response['workouts'] = $posts;

			echo (json_encode($response));
		}
		else
		{
			echo 'username heeft geen workouts';
		}
	}
	else
	{
		echo "no username in url";
	}


	
		

	?> 
