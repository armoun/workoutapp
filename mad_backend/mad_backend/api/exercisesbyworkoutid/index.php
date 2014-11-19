<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$workoutidFromURL = $_GET['workoutid'];

	if (!empty($_GET)) 
	{
		//exercises workouts ophalen
		$result = mysqli_query($conn, "SELECT id, workout_id, exercise_id FROM mad_workouts_exercises WHERE workout_id = '$workoutidFromURL'"); 

		$response = array();
		$posts = array();
		
		$row_cnt = $result->num_rows;

		if($row_cnt > 0 )
		{
			while($row=mysqli_fetch_array($result)) 
			{ 
				$exercise_id_temp=$row['exercise_id']; 
				//name exercises ophalen
				$result1 = mysqli_query($conn, "SELECT name FROM mad_exercises WHERE id = '$exercise_id_temp'"); 

				$row1=mysqli_fetch_array($result1);

				$id=$row['id']; 
				$workout_id=$row['workout_id']; 
				$exercise_id=$row['exercise_id']; 

				$exercise_name=$row1['name']; 

				$posts[] = array('id'=> $id, 'workout_id'=> $workout_id, 'exercise_id'=> $exercise_id, 'exercise_name'=> $exercise_name);

			} 

			$response['exercises'] = $posts;

			echo (json_encode($response));
		}
		else
		{
			echo 'id heeft geen exercises';
		}
	}
	else
	{
		echo "no workout id in url";
	}


	
		

	?> 
