<?php 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$response = array("error" => FALSE);

if (isset($_POST['name'])  && isset($_POST['gender']) 
	&& isset($_POST['birth_date']) && isset($_POST['profession'])
    && isset($_POST['city']) && isset($_POST['province']) 
    && isset($_POST['email'])) {

	// receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $gender = $_POST['gender'];
    $birth_date = $_POST['birth_date'];
    $profession = $_POST['profession'];
    $city = $_POST['city'];
    $province = $_POST['province'];

	if ($db->isUserExisted($email)) {
		$user = $db->updateUser($name, $gender, $birth_date, $profession, $city, $province, $email);

		if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["id"];
            $response["user"]["email"] = $user["email"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in update!";
            echo json_encode($response);
        }
	}


}

?>