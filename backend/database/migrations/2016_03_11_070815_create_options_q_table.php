<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateOptionsQTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('option_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->enum('type', ['dropdown', 'option_button']);
            $table->timestamps();
        });

        Schema::table('option_questions', function ($table) {
            // $table->integer('survey_id')->unsigned();
            // $table->integer('user_id')->unsigned();
            $table->foreign('id')->references('id')->on('questions')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('option_questions', function ($table) {
            $table->dropForeign(['id']);
        });
        Schema::drop('option_questions');
    }
}
