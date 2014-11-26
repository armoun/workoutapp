<?php 

		function connect_db() 
		{
			$server = 'viktordebock.be.mysql'; // this may be an ip address instead
			$user = 'viktordebock_be';
			$pass = '8EN9EcgV';
			$database = 'viktordebock_be';
			$connection = new mysqli($server, $user, $pass, $database);

			return $connection;
		}

 ?>