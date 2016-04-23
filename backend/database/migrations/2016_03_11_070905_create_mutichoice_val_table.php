<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateMutichoiceValTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('checkbox_val', function (Blueprint $table) {
            $table->increments('id');
            $table->string('choice');
            $table->timestamps();
        });

        Schema::table('checkbox_val', function ($table) {
            // $table->integer('survey_id')->unsigned();
            $table->integer('question_id')->unsigned();
            $table->unique(['question_id', 'choice']);
            $table->foreign('question_id')->references('id')->on('checkbox_questions')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('checkbox_val', function ($table) {
            $table->dropForeign(['question_id']);
        });
        Schema::drop('checkbox_val');
    }
}
