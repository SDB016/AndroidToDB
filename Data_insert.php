<?php
    header("Content-Type: text/html;charset=UTF-8");
    $conn = mysqli_connect("localhost","root","","test");
    $data_stream = "'".$_POST['Ind']."','".$_POST['Data1']."','".$_POST['Data2']."','".$_POST['Data3']."'";
    if($_POST['flag'] == "send"){
      $query = "insert into test(Ind,Data1,Data2,Data3) values (".$data_stream.")";
    }
    else if($_POST['flag'] == "modify"){
      $query = "update `test` SET `Data1`='".$_POST['Data1']."',`Data2`='".$_POST['Data2']."',`Data3`='".$_POST['Data3']."' WHERE Ind = '".$_POST['Ind']."'";

    }
    else if($_POST['flag'] == "delete"){
      $query = "delete from `test` WHERE `Ind` ='".$_POST['Ind']."'";
    }
    $result = mysqli_query($conn, $query);

    if($result)
      echo "1";
    else
      echo "-1";

    mysqli_close($conn);
?>
