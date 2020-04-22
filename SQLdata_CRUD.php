<?php
    header("Content-Type: text/html;charset=UTF-8");
    $conn = mysqli_connect("localhost","root","","info");
    if(mysqli_connect_errno($conn)){
      echo "Failed";
    }
    mysqli_set_charset($conn,"utf8");

    $res = mysqli_query($conn,"select * from `userinfo`");

    $result = array();

    while($row = mysqli_fetch_array($res)){
      array_push($result,array('name'=>$row[0],'kindergarten'=>$row[1],'class'=>$row[2],'gender'=>$row[3],'birth'=>$row[4],'teeth1_have'=>$row[5],'teeth2_have'=>$row[6],'teeth3_have'=>$row[7],'teeth4_have'=>$row[8]));
    }

    echo json_encode(array("result"=>$result),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);




    if($_POST['flag'] == "send"){
      $query = "insert into `userinfo` (`name`,`teeth1_have`,`teeth2_have`,`teeth3_have`) values ('".$_POST['name']."','".$_POST['teeth1_have']."','".$_POST['teeth2_have']."','".$_POST['teeth3_have']."')";
    }
    else if($_POST['flag'] == "modify"){
      $query = "update `userinfo` SET `teeth1_have`='".$_POST['teeth1_have']."',`teeth2_have`='".$_POST['teeth2_have']."',`teeth3_have`='".$_POST['teeth3_have']."' WHERE name = '".$_POST['name']."'";

    }
    else if($_POST['flag'] == "delete"){
      $query = "delete from `userinfo` WHERE `name` ='".$_POST['name']."'";
    }
    $result = mysqli_query($conn, $query);

    if($result)
      echo "1";
    else
      echo "-1";

    mysqli_close($conn);
?>
