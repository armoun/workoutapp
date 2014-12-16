<?php 
	require_once('mysql_connect.php'); 

	ob_start();

	$usernameFromURL = $_GET['username'];
	$passwordFromURL = $_GET['password'];

	    if (!empty($usernameFromURL))
		{
	    	if (!empty($passwordFromURL)) 
			{
		    	
				// Create connection
				$conn = connect_db();
				// Check connection
				if ($conn->connect_error) {
				    die("Connection failed: " . $conn->connect_error);
				} 

				$usernameDB;
				$passwordDB;

				$sql = "SELECT username, password FROM mad_users WHERE username = '$usernameFromURL'";
				$result = $conn->query($sql);

				if ($result->num_rows > 0) 
				{
				    while($row = $result->fetch_assoc()) 
				    {
				        $usernameDB = $row["username"];
				        $passwordDB = $row["password"];
				    }

				    $conn->close();

				    $passwordFromURL = sha1($passwordFromURL);

				    if($passwordFromURL == $passwordDB)
				    {
				    	header('X-PHP-Response-Code: 201', true, 201);
				    }
				    else
				   	{
				   		echo "wrong password";
				   		header('X-PHP-Response-Code: 404', true, 404);
				   	}
				}
				else
				{
					echo "username bestaat niet";
					header('X-PHP-Response-Code: 404', true, 404);
				}
			}
			else
			{  
			    echo "geen wachtwoord";
			    header('X-PHP-Response-Code: 404', true, 404);
			}

		}
		else
		{  
		    echo "geen username";
		}

?> 

<html>
<head>
	<title></title>
</head>
<body>

</body>
</html>