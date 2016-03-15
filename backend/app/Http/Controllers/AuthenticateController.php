<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Dingo\Api\Routing\Helpers;

use App\Http\Requests;
use JWTAuth;
use TymonJWTAuthExceptionsJWTException;

class AuthenticateController extends Controller
{
    use Helpers;
    public function __construct()
    {
       // Apply the jwt.auth middleware to all methods in this controller
       // except for the authenticate method. We don't want to prevent
       // the user from retrieving their token if they don't already have it
       $this->middleware('jwt.auth', ['except' => ['authenticate']]);
    }

    public function index()
    {
        // TODO: show users
    }
    
    public function authenticate(Request $request)
    {
        $credentials = $request->only('email', 'password');

        try {
            // verify the credentials and create a token for the user
            if (! $token = JWTAuth::attempt($credentials)) {
                return $this->response->errorUnauthorized();
            }
        } catch (JWTException $e) {
            // something went wrong
            return $this->response->error('Can not create token.', 500);
        }

        // if no errors are encountered we can return a JWT
        return response()->json(compact('token'));
    }
}
