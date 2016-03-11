<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('users', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name');
            $table->string('email')->unique();
            $table->string('password');
            $table->enum('gender', ['m', 'f']);
            $table->date('birth_date');
            $table->string('profession');
            $table->string('city');
            $table->string('province');

            // Jika butuh konfirmasi email....

            // $table->boolean('confirmed')->default(0);
            // $table->string('confirmation_code')->nullable();
            
            // Untuk login ke web (jika ada mekanisme login lweat web, adakah?)
            $table->rememberToken();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('users');
    }
}
