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

class CheckConfirmMiddleware
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
        if (! $user = JWTAuth::parseToken()->authenticate()) {
            return response()->json(['message' => 'User tidak ditemukan', 'status_code' => 404], 404);
        }
        if (!$user->confirmed) {
            return response()->json(['message' => 'Email belum dikonfirmasi', 'status_code' => 403], 403);
        }

        $response = $next($request);

        return $response;
    }
}
