<?php

namespace App\Http\Middleware;

use Closure;
use JWTAuth;
use TymonJWTAuthExceptionsJWTException;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpFoundation\Response;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Exceptions\TokenExpiredException;
use Tymon\JWTAuth\Exceptions\TokenInvalidException;
use Tymon\JWTAuth\Exceptions\TokenBlacklistedException;

class GetNewTokenMiddleware
{
    public static function check() {

    }

    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $token = JWTAuth::getToken();

        try {
            if (! $user = JWTAuth::parseToken()->authenticate()) {
                return response()->json(['message' => 'User tidak ditemukan', 'status_code' => 403], 404);
            }
        } catch (TokenExpiredException $e) {
            $token = GetNewTokenMiddleware::refresh($token);

            return response()->json(['message' => 'Token is invalid. New token is provided', 'token' => $token], $e->getStatusCode());
        } catch (TokenInvalidException $e) {
            return response()->json(['message' => 'Token is invalid. Please relogin'], $e->getStatusCode());
        } catch (JWTException $e) {
            return response()->json(['message' => 'No token is detected.'], $e->getStatusCode());
        }

        $response = $next($request);

        return $response;
    }

    public static function refresh($token) {
        try {
            $token = JWTAuth::refresh($token);
        } catch(TokenInvalidException $e){
            throw new AccessDeniedHttpException('The token is invalid');
        } catch (TokenBlacklistedException $e) {
            return response()->json(['message' => "Token can no longer be refreshed. Please re-login" ], $e->getStatusCode());
        }

        return $token;
    }
}
