<?php
include_once ("database.php");
$mycourses = simplexml_load_file('thefinale.xml');
$i=0;

foreach($mycourses as $course)

{

 $CourseNumber= $mycourses->course[$i] ['number'];
 $CourseTitle = $mycourses->course[$i] ['title'];
 $CourseM = explode(" " ,$CourseNumber);
 $CourseMajor=$CourseM[0];
 echo $CourseNumber;
 echo $CourseTitle;
 echo $CourseMajor;
 $insertString= "insert into purduesubjects( Course , Title , Major  ) VALUES( '$CourseNumber','$CourseTitle','$CourseMajor')";
 echo mysql_query($insertString);
 echo '<br>';
 $i++;
}




?>
