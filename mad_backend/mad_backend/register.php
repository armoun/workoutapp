<?php 
	require_once('mysql_connect.php'); 

	ob_start();

	if (isset($_POST["send"]))
	{
		if (isset($_POST["voornaam"]) && !empty($_POST["voornaam"])) 
		{
	    	if (isset($_POST["achternaam"]) && !empty($_POST["achternaam"])) 
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

						$voornaam = $_POST["voornaam"];
						$achternaam = $_POST["achternaam"];
						$username = $_POST["username"];
						$password = $_POST["password"];

						//ingevulde username ophalen en kijken of hij bestaat
						$sql1 = "SELECT username, password FROM mad_users WHERE username = '$username'";
						$result1 = $conn->query($sql1);

						if ($result1->num_rows == 1) 
						{
							echo "username bestaat al!";
						}
						else
						{
							// USER AANMAKEN INDIEN USERNAME NOG NIET BESTAAT
							$sql = "INSERT INTO mad_users (firstname,lastname,username,password) VALUES ('$voornaam','$achternaam','$username','$password')";

							if ($conn->query($sql) === TRUE) 
							{
							    echo "New record created successfully";
							} else {
							    echo "Error: " . $sql . "<br>" . $conn->error;
							}

							$conn->close();

							header('Location: http://www.viktordebock.be/mad_backend');
						}

					}else
					{  
					    echo "geen password";
					}
				}else
				{  
				    echo "geen username";
				}
			}else
			{  
			    echo "geen achternaam";
			}
		}else
		{  
		    echo "geen voornaam";
		}
	}

?> 

<html>
<head>
	<title></title>
</head>
<body>

	<form method="POST">
		<label for="voornaam">Voornaam</label>
		<input type="text" id="voornaam" name="voornaam">

		<label for="achternaam">Achternaam</label>
		<input type="text" id="achternaam" name="achternaam">

		<label for="username">Username</label>
		<input type="text" id="username" name="username">

		<label for="password">Password</label>
		<input type="password" id="password" name="password">

		<button type="submit" name="send">Create</button>
	</form>	

</body>
</html>