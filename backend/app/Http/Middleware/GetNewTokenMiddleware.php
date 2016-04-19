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
        $response = $next($request);

        $isRefreshed = false;
        $token = JWTAuth::getToken();

        try {
            if (! $user = JWTAuth::parseToken()->authenticate()) {
                return response()->json(['user_not_found'], 404);
            }
        } catch (TokenExpiredException $e) {
            $tmp = GetNewTokenMiddleware::refresh($token);
            $token = $tmp[0];
            $isRefreshed = $tmp[1];
        } catch (TokenInvalidException $e) {
            return response()->json(['token_invalid'], $e->getStatusCode());
        } catch (JWTException $e) {
            return response()->json(['token_absent'], $e->getStatusCode());
        }

        if (!$isRefreshed) {
            $tmp = GetNewTokenMiddleware::refresh($token);
            $token = $tmp[0];
            $isRefreshed = $tmp[1];
        }
        $response->headers->set('Authorization', 'Bearer '.$token);

        return $response;
    }

    public static function refresh($token) {
        $isRefreshed = false;
        try {
            $token = JWTAuth::refresh($token);
            $isRefreshed = true;
        } catch(TokenInvalidException $e){
            throw new AccessDeniedHttpException('The token is invalid');
        } catch (TokenBlacklistedException $e) {
            return response()->json(['message' => "Token can no longer be refresh. Please re-login" ], $e->getStatusCode());
        }

        return [$token, $isRefreshed];
    }
}
