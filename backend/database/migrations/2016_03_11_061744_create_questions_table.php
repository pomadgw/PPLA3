<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateQuestionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('questions', function (Blueprint $table) {
            $table->increments('id');
            $table->text('question');
            $table->enum('type',['text', 'paragraph', 'option', 'checkbox', 'scale']);
            $table->timestamps();
        });

        Schema::table('questions', function ($table) {
            $table->integer('survey_id')->unsigned();
            // $table->integer('user_id')->unsigned();
            $table->foreign('survey_id')->references('id')->on('surveys')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('questions', function ($table) {
            $table->dropForeign(['survey_id']);
        });
        Schema::drop('questions');
    }
}
