<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	$usernameFromURL = $_GET['username'];
	$ALL = "ALL";

	if (!empty($_GET)) 
	{
		//workouts ophalen
		//$result = mysqli_query($conn, "SELECT id, name, owner_username FROM mad_workouts WHERE owner_username = '$usernameFromURL'"); 
		if(!($stmt = $conn->prepare("SELECT id, name, owner_username, isPaid FROM mad_workouts WHERE owner_username = ? OR owner_username = ?"))) {
             echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
        }
        
        if (!$stmt->bind_param("ss", $usernameFromURL, $ALL)) {
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
				$name=$row['name']; 
				$owner_username=$row['owner_username'];
				$isPaid=$row['isPaid']; 

                if(!($stmt1 = $conn->prepare("SELECT id, workout_id, exercise_id, reps FROM mad_workouts_exercises WHERE workout_id = ?"))) {
                     echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
                }

                if (!$stmt1->bind_param("i", $id)) {
                    echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
                }

                if (!$stmt1->execute()) {
                    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                }

                $res1 = $stmt1->get_result();

				$row_cnt1 = $res1->num_rows;
                
				if($row_cnt1 > 0 )
				{
					while($row1=$res1->fetch_assoc())
					{
						$exercise_id_temp=$row1['exercise_id']; 
                        
                            if(!($stmt2 = $conn->prepare("SELECT name FROM mad_exercises WHERE id = ?"))) {
                                 echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
                            }

                        if (!$stmt2->bind_param("i", $exercise_id_temp)) {
                            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
                        }

                        if (!$stmt2->execute()) {
                            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                        }

                        $res2 = $stmt2->get_result();
                        
                        while($row2=$res2->fetch_assoc())
                        {   
                            $id_row=$row1['id']; 
                            $workout_id=$row1['workout_id']; 
                            $exercise_id=$row1['exercise_id'];
                            $reps=$row1['reps'];
                            $exercise_name=$row2['name'];

                            $exercises[] = array('id_row'=> $id_row, 'workout_id'=> $workout_id, 'exercise_id'=> $exercise_id, 'exercise_name'=> $exercise_name, 'reps'=> $reps);
                        }


					}

				}

				$posts[] = array('id'=> $id, 'name'=> $name, 'owner_username'=> $owner_username, 'isPaid'=> $isPaid, 'exercises' => $exercises);
				$exercises = array();

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
