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

$api = app('Dingo\Api\Routing\Router');

$api->version('v1', function ($api) {
    // route untuk login
    $api->post('authenticate', 'App\Http\Controllers\AuthenticateController@authenticate');

    // route untuk login (lewat OAuth2, eksperimen)
    $api->post('oauth/access_token', 'App\Http\Controllers\AuthenticateController@getAccessToken');

    // route untuk mendapatkan token
    $api->get('token', 'App\Http\Controllers\AuthenticateController@getJWTToken');

    // route untuk men-invalidate token (dengan demikian, logout)
    $api->get('invalidate', 'App\Http\Controllers\AuthenticateController@invalidate');

    // route untuk register
    // POST /api/users/register
    $api->post('users/register', 'App\Http\Controllers\UserController@register');

    // route untuk mendapatkan info user yang login
    // GET /api/users/current
    $api->get('users/current', 'App\Http\Controllers\UserController@get_current_user_info');

    // POST /api/users/{id}/update
    $api->post('users/current/update', 'App\Http\Controllers\UserController@updateUser');


    // survey things

    $api->get('surveys/list', 'App\Http\Controllers\SurveyController@getAllSurveys');

    $api->get('surveys/list/criteria', 'App\Http\Controllers\SurveyController@getSurveyBasedOnCriteria');

    $api->get('surveys/user', 'App\Http\Controllers\SurveyController@getUserSurveys');

    $api->get('surveys/{id}', 'App\Http\Controllers\SurveyController@getSurvey');

    $api->post('surveys/{id}/edit', 'App\Http\Controllers\SurveyController@editSurvey');

    $api->post('surveys/add', 'App\Http\Controllers\SurveyController@createSurvey');

    $api->post('surveys/{id}/questions/add', 'App\Http\Controllers\SurveyController@createQuestionEndpoint');

    $api->delete('surveys/{surveyId}/questions/{questionId}/delete', 'App\Http\Controllers\SurveyController@deleteQuestion');

    $api->delete('surveys/{surveyId}/delete', 'App\Http\Controllers\SurveyController@deleteSurvey');
});

Route::get('email-verification/{id}', 'UserController@confirm');
