<?php

use Illuminate\Foundation\Testing\WithoutMiddleware;
use Illuminate\Foundation\Testing\DatabaseMigrations;
use Illuminate\Foundation\Testing\DatabaseTransactions;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Illuminate\Http\Response as HttpResponse;
use JWTAuth;

class UpdateUserTest extends TestCase
{
    /**
     * A basic test example.
     *
     * @return void
     */
    public function testExample()
    {$path          = storage_path('testfile/tatsuya.jpg');
        $original_name = 'tatsuya.jpg';
        $mime_type     = 'image/jpeg';
        $size          = 21608;
        $error         = null;
        $test          = true;

        $file = new UploadedFile($path, $original_name, $mime_type, $size, $error, $test);

        $user = \App\User::where('email', 'asami@tomorrowresearch.com')->first();
        $token = JWTAuth::fromUser($user);

        $this->refreshApplication();
        $uri = 'http://surverior.local/api/users/current/update?token=' . $token;

        // TODO: token dikasih secara manual di uri-nya... tapi masih nggak bisa
        // -_- (NB: bukan salah kodingannya)
        $response = $this->call(
            'POST',
            $uri,
            [
                'name' => 'Tatsuya Asami',
                'password' => 'timeranger',
                'gender' => 'm',
                'birth_date' => '1970-03-21',
                'profession' => 'Direktur',
                'province' => 'Jakarta',
                'city' => 'Jakarta',
                'phone_number' => '0215552000'
            ], //parameters
            [], //cookies
            [
                'photo' => $file
            ], // files
            ['HTTP_Authorization' => 'Bearer ' . $token], // server
            []
        );

        var_dump($response);

        $this->assertEquals(HttpResponse::HTTP_OK, $response->status());
    }
}
