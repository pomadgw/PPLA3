<?php

use Illuminate\Foundation\Testing\WithoutMiddleware;
use Illuminate\Foundation\Testing\DatabaseMigrations;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class AuthenticateTest extends TestCase
{
    /**
     * A basic test example.
     *
     * @return void
     */
    public function testExample()
    {
        $this->json('POST', '/api/authenticate', ['email' => 'asami@tomorrowresearch.com',
                                                  'password' => 'timerangers'])
             ->seeJsonStructure([
                 'token',
                 'status_code',
                 'user' => [
                     "birth_date",
                     "city",
                     "confirmed",
                     "created_at",
                     "email",
                     "gender",
                     "id",
                     "name",
                     "photo",
                     "profession",
                     "province",
                     "sum_coins",
                     "updated_at"
                 ]
             ]);
    }
}
