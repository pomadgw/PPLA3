<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class QuestionCheckbox extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'checkbox_questions';

    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }

    public function choices()
    {
        return $this->hasMany('App\Choice', 'question_id', 'id');
    }
}
