<?php 
	header('Content-Type: application/json');
	require_once('../../mysql_connect.php'); 
	ob_start();
		
	$conn = connect_db();
	
	$result = mysqli_query($conn, "SELECT id, firstname, lastname, username, password FROM mad_users"); 

	$response = array();
	$posts = array();
	while($row=mysqli_fetch_array($result)) 
	{ 

		$id=$row['id']; 
		$firstname=$row['firstname']; 
		$lastname=$row['lastname']; 
		$username=$row['username']; 
		$password=$row['password']; 

		$hashedPassword = md5($password);

	$posts[] = array('id'=> $id, 'firstname'=> $firstname, 'lastname'=> $lastname, 'username'=> $username, 'password'=> $hashedPassword);

	} 

	$response['users'] = $posts;

	echo (json_encode($response));

?> 
