<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateMutichoiceQTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('multichoice_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->timestamps();
        });

        Schema::table('multichoice_questions', function ($table) {
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
        Schema::table('multichoice_questions', function ($table) {
            $table->dropForeign(['id']);
        });
        Schema::drop('multichoice_questions');
    }
}