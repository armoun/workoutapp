<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();
		
	$conn = connect_db();
	
	$result = mysqli_query($conn, "SELECT id, name, description, image, target, musclegroup FROM mad_exercises"); 

	$response = array();
	$posts = array();
	while($row=mysqli_fetch_array($result)) 
	{ 

		$id=$row['id']; 
		$name=$row['name']; 
		$description=$row['description']; 
		$image=$row['image']; 
		$target=$row['target']; 
		$musclegroup=$row['musclegroup'];

	$posts[] = array('id'=> $id, 'name'=> $name, 'musclegroup'=> $musclegroup,'target'=> $target, 'description'=> $description, 'image'=> $image);

	} 

	$response['exercises'] = $posts;

	echo (json_encode($response));

?> 
