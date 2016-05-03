<html>
<head>

</head>
<body style="background-color:orange;">
    <h1 style="text-align: center;color: white;font-family: verdana; font-size: 50px;padding-top: 100px">Thank You For Register</h3>
    <h2 style="text-align: center; color: white;font-family: verdana;"> Please Click Link Below For Confirmation </h2>
    <h3 style="text-align: center; color: #7F6626; font-family: verdana; font-size: 15px;font-style: italic;"> Note: if you can't click the link, please copy and paste this link to your browser </h3>

    <a href="{{url('email-verification/' . $user->confirmation_code)}}">{{url('email-verification/' . $user->confirmation_code)}}</a>
    <h3 style="text-align: center; color: white;font-family: verdana;padding-top: 40px;"> Welcome to Surverior  </h2>
    <h3 style="text-align: center; color: white;font-family: verdana;padding-top: 20px; font-size: 12px;font-style: underline;"> Find us on </h3>
    <p style="text-align: center;">
    <a href="https://www.facebook.com/"><img src="http://lacanterazaragoza.es/FOTOGRAFIAS%20RESTAURANTE/facebook.png.png" width="40px"> </a> </p>
</body>
</html>
