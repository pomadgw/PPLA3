<?php

use Illuminate\Database\Seeder;
use App\User;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        // Model::unguard();

        DB::table('users')->delete();

        // gender: 0 = male, 1 = female
        $users = array(
                ['name' => 'Rahadian Yusuf', 'email' => 'me@localhost',
                 'password' => Hash::make('jangancobacoba'), 'gender' => 'm', 'birth_date' => '1994-05-23',
                 'profession' => 'student', 'city' => 'Bogor', 'province' => 'Jawa Barat'],
        );

        // Loop through each user above and create the record for them in the database
        foreach ($users as $user)
        {
        	$user_obj = User::firstOrCreate($user);

        	// $user_obj->save();
        }

        // Model::reguard();
    }
}
