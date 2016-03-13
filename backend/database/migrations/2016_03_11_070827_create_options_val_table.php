<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateOptionsValTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('options_val', function (Blueprint $table) {
            $table->increments('id');
            $table->string('options');
            $table->timestamps();
        });

        Schema::table('options_val', function ($table) {
            // $table->integer('survey_id')->unsigned();
            $table->integer('question_id')->unsigned();
            $table->unique(['question_id', 'options']);
            $table->foreign('question_id')->references('id')->on('option_questions')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('options_val', function ($table) {
            $table->dropForeign(['question_id']);
        });
        Schema::drop('options_val');
    }
}
