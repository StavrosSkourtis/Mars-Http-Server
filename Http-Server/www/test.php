<?php 
	print "The php part works!!";
       // print "get".$_GET['id'];

        print 'GET ';
        print_r($_GET);
        print '<br/>';
        print 'POST ';
        print_r($_POST);
        
?>

<html>
    <head>
</head>
    <body>
        <p>The html part works!!</p>

        <form method="post">
        <input name="p" type="text">
        <input type="submit" >
        </form>
    </body>
</html>
