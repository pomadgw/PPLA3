<?php

use Illuminate\Foundation\Testing\WithoutMiddleware;
use Illuminate\Foundation\Testing\DatabaseMigrations;
use Illuminate\Foundation\Testing\DatabaseTransactions;

class NotAuthenticateTest extends TestCase
{
    /**
     * A basic test example.
     *
     * @return void
     */
    public function testExample()
    {
        $this->json('POST', '/api/authenticate')
             ->seeJson([
                 'message' => 'Unauthorized',
                 "status_code" => 401
             ]);
    }
}
