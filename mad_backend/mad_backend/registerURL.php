<?php 
	require_once('mysql_connect.php'); 

	ob_start();

	$firstnameFromURL = $_GET['firstname'];
	$lastnameFromURL = $_GET['lastname'];
	$usernameFromURL = $_GET['username'];
	$passwordFromURL = $_GET['password'];

		if (!empty($firstnameFromURL)) 
		{
	    	if (!empty($lastnameFromURL)) 
			{
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

						$passwordHash = sha1($passwordFromURL);

						//ingevulde username ophalen en kijken of hij bestaat
						$sql1 = "SELECT username, password FROM mad_users WHERE username = '$usernameFromURL'";
						$result1 = $conn->query($sql1);

						if ($result1->num_rows == 1) 
						{
							echo "username bestaat al!";
							header('X-PHP-Response-Code: 404', true, 404);
						}
						else
						{
							// USER AANMAKEN INDIEN USERNAME NOG NIET BESTAAT
							$sql = "INSERT INTO mad_users (firstname,lastname,username,password) VALUES ('$firstnameFromURL','$lastnameFromURL','$usernameFromURL','$passwordHash')";

							if ($conn->query($sql) === TRUE) 
							{
							    echo "New record created successfully";
							} else {
							    echo "Error: " . $sql . "<br>" . $conn->error;
							}

							$conn->close();

							header('X-PHP-Response-Code: 201', true, 201);

							//header('Location: http://www.viktordebock.be/mad_backend');
						}

					}else
					{  
					    echo "geen password";
					    header('X-PHP-Response-Code: 404', true, 404);
					}
				}else
				{  
				    echo "geen username";
				    header('X-PHP-Response-Code: 404', true, 404);
				}
			}else
			{  
			    echo "geen achternaam";
			    header('X-PHP-Response-Code: 404', true, 404);
			}
		}else
		{  
		    echo "geen voornaam";
		    header('X-PHP-Response-Code: 404', true, 404);
		}

?> 

<html>
<head>
	<title></title>
</head>
<body>

</body>
</html>