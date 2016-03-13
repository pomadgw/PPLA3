<?php

// use App\Http\Controllers\UserController;

/*
|--------------------------------------------------------------------------
| Routes File
|--------------------------------------------------------------------------
|
| Here is where you will register all of the routes in an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('/', function () {
    return view('welcome');
});

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| This route group applies the "web" middleware group to every route
| it contains. The "web" middleware group is defined in your HTTP
| kernel and includes session state, CSRF protection, and more.
|
*/

Route::group(['middleware' => ['web']], function () {
    //
});

Route::group(['prefix' => 'api'], function()
{
    Route::resource('authenticate', 'AuthenticateController', ['only' => ['index']]);

    // route untuk login
    Route::post('authenticate', 'AuthenticateController@authenticate');

    // route untuk register
    // POST /api/users/register
    Route::post('users/register', 'UserController@register');

    // route untuk mendapatkan info user yang login
    // GET /api/users/current
    Route::get('users/current', 'UserController@get_current_user_info');
});
