<?php

use Illuminate\Foundation\Testing\WithoutMiddleware;
use Illuminate\Foundation\Testing\DatabaseMigrations;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class RegisterTest extends TestCase
{
    /**
     * A basic test example.
     *
     * @return void
     */
    public function testApplicationExistsEmail()
    {
        $this->json('POST', '/api/users/register', ['email' => 'asami@tomorrowresearch.com',
                                                  'password' => 'timerangers'])
             ->seeJson([
                 'error' => true, 'message' => 'Email sudah ada.', "type" => "failed"
             ]);
    }

    public function testApplicationIncompleteData()
    {
       $this->json('POST', '/api/users/register', ['email' => 'yuuri@tomorrowresearch.com'])
            ->seeJson([
                'error' => true, 'message' => 'Register gagal', "type" => "failed"
            ]);
    }

    public function testApplicationSuccess()
    {
        DB::table('users')->where('email', '=', 'yuuri@tomorrowresearch.com')->delete();
        $this->json('POST', '/api/users/register', ['email' => 'yuuri@tomorrowresearch.com',
                                                   'password' => 'timepink'])
            ->seeJson([
                'error' => false, 'message' => 'Success register to server', "type" => "success", "status_code" => 201
            ]);
    }
}
