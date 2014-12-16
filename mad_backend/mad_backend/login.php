<?php 
	require_once('mysql_connect.php'); 

	ob_start();

	if (isset($_POST["send"]))
	{
	    if (isset($_POST["username"]) && !empty($_POST["username"])) 
		{
	    	if (isset($_POST["password"]) && !empty($_POST["password"])) 
			{
		    	
				// Create connection
				$conn = connect_db();
				// Check connection
				if ($conn->connect_error) {
				    die("Connection failed: " . $conn->connect_error);
				} 

				$usernameForm = $_POST["username"];
				$passwordForm = $_POST["password"];

				$usernameDB;
				$passwordDB;

				$sql = "SELECT username, password FROM mad_users WHERE username = '$usernameForm'";
				$result = $conn->query($sql);

				if ($result->num_rows > 0) 
				{
				    while($row = $result->fetch_assoc()) 
				    {
				        $usernameDB = $row["username"];
				        $passwordDB = $row["password"];
				    }

				    $conn->close();

				    $passwordForm = sha1($passwordForm);

				    if($passwordForm == $passwordDB)
				    {
				    	header('Location: http://www.viktordebock.be/mad_backend');
				    }
				    else
				   	{
				   		echo "wrong password";
				   	}
				}
				else
				{
					echo "username bestaat niet";
				}
			}else
			{  
			    echo "geen wachtwoord";
			}

			}else
			{  
			    echo "geen username";
			}
		}

?> 

<html>
<head>
	<title></title>
</head>
<body>

	<form method="POST">

		<label for="username">Username</label>
		<input type="text" id="username" name="username">

		<label for="password">Password</label>
		<input type="password" id="password" name="password">

		<button type="submit" name="send">Login</button>
	</form>	

</body>
</html>