<?php 
	require_once('../../mysql_connect.php'); 
?> 

<html>
<head>
	<title>admin/users</title>
</head>
<body>

	<h1>Users</h1>
	<?php

		// Create connection
		$conn = connect_db();
		// Check connection
		if ($conn->connect_error) {
		    die("Connection failed: " . $conn->connect_error);
		} 

		$sql = "SELECT id, firstname, lastname, username, password FROM mad_users";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		        echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. " - Username: " . $row["username"]. " - Password: " . $row["password"]."<br>";
		    }
		} else {
		    echo "0 results";
		}
		$conn->close();
	?>
</body>
</html>