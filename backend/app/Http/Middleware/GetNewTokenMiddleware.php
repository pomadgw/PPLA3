<?php

namespace App\Http\Middleware;

use Closure;
use JWTAuth;
use TymonJWTAuthExceptionsJWTException;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpFoundation\Response;

class GetNewTokenMiddleware
{
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

        if(!$token){
            throw new \Symfony\Component\HttpKernel\Exception\BadRequestHttpException('Token not provided');
        }

        try {
            $token = JWTAuth::refresh($token);
        } catch(TokenInvalidException $e){
            throw new AccessDeniedHttpException('The token is invalid');
        }

        $response = $next($request);
        
        $response->headers->set('Authorization', 'Bearer '.$token);

        return $response;
    }
}
