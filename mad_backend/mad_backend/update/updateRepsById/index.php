<?php 

	require_once('../../mysql_connect.php'); 
	ob_start();

	$conn = connect_db();

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	} 

	if (!empty($_GET['id']) && !empty($_GET['reps'])) 
	{
		$idFromURL = $_GET['id'];
		$repsFromURL = $_GET['reps'];

		//exercise updaten ophalen

        if(!($stmt = $conn->prepare("UPDATE mad_workouts_exercises SET reps = ? WHERE id = ? "))) {
             echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
        }
        
        if (!$stmt->bind_param("ss", $repsFromURL, $idFromURL)) {
            echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
        }
        
        if (!$stmt->execute()) {
            echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
        }

        if ($stmt->execute()) {
            echo "reps gewijzigd";
        }
    }
	else
	{
		echo('no id or sets');
	}

?>