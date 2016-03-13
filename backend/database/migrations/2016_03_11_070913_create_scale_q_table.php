<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateScaleQTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('scale_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('min_val');
            $table->integer('max_val');
            $table->string('min_label');
            $table->string('max_label');
            $table->timestamps();
        });

        Schema::table('scale_questions', function ($table) {
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
        Schema::table('scale_questions', function ($table) {
            $table->dropForeign(['id']);
        });
        Schema::drop('scale_questions');
    }
}
