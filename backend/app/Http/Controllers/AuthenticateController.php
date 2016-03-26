<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Dingo\Api\Routing\Helpers;

use App\Http\Requests;
use JWTAuth;
use Auth;
use TymonJWTAuthExceptionsJWTException;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use LucaDegasperi\OAuth2Server\Facades\Authorizer;

class AuthenticateController extends Controller
{
    use Helpers;
    public function __construct()
    {
       // Apply the jwt.auth middleware to all methods in this controller
       // except for the authenticate method. We don't want to prevent
       // the user from retrieving their token if they don't already have it
       $this->middleware('api.auth', ['except' => ['authenticate', 'verifyUser', 'getAccessToken', 'getJWTToken']]);
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

    // dari https://laracasts.com/discuss/channels/general-discussion/how-to-refreshing-jwt-token
    public function getJWTToken()
    {
        $token = JWTAuth::getToken();
        if(!$token){
            throw new \Symfony\Component\HttpKernel\Exception\BadRequestHttpException('Token not provided');
        }

        try {
            $token = JWTAuth::refresh($token);
        } catch(TokenInvalidException $e){
            throw new AccessDeniedHttpException('The token is invalid');
        }

        return $this->response->withArray(['token'=>$token]);
    }

    public function verifyUser($email, $password)
    {
        $credentials = [
            'email'    => $email,
            'password' => $password,
        ];

        if (Auth::attempt($credentials)) {
            return Auth::user()->id;
        }

        return false;
    }

    public function getAccessToken()
    {
        return Authorizer::issueAccessToken();
    }

    public function invalidate()
    {
        $token = JWTAuth::getToken();
        if ($token) {
            JWTAuth::setToken($token)->invalidate();
        }

        return $this->response->withArray(['error' => 'logout_success']);
    }
}
