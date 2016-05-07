<?php

use Illuminate\Database\Seeder;
use App\User;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {

    DB::table('users')->delete();

    // gender: 0 = male, 1 = female
    $users = array(
            ['name' => 'Rahadian Yusuf', 'email' => 'rianyusuf@pomadgw.xyz',
             'password' => Hash::make('jangancobacoba'), 'gender' => 'm', 'birth_date' => '1994-05-23',
             'profession' => 'student', 'city' => 'Bogor', 'province' => 'Jawa Barat',
             'confirmed' => true],
             [
                 'name' => 'Tatsuya Asami',
                 'email' => 'asami@tomorrowresearch.example.com',
                 'password' => Hash::make('timerangers'),
                 'gender' => 'm',
                 'birth_date' => '1980-01-22',
                 'profession' => 'Wirausaha',
                 'province' => 'Jakarta',
                 'city' => 'Jakarta',
                 'confirmed' => true
             ]
    );

    // Loop through each user above and create the record for them in the database
    foreach ($users as $user)
    {
        $user_obj = User::firstOrCreate($user);

        $user_obj->save();
    }
    }
}
