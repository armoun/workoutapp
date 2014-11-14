<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php echo $this->data['page_title']; ?></title>
</head>
<body>
<?php
foreach ($this->data['data'] as $user) {
    echo $user['id'].' - '.$user['firstname'].' - '.$user['lastname'].' - '.$user['username'].'</br />';
}
?>
</body>
</html>