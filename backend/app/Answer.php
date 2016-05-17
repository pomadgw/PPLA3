<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Answer extends Model
{
    protected $table = 'answers';

    public function question() {
        return $this->hasMany('App\Question', 'question_id');
    }
}
