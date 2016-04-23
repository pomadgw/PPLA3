<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class QuestionMultipleChoices extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'multichoice_questions';

    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }

    public function choices()
    {
        return $this->hasMany('App\MultipleChoices', 'question_id', 'id');
    }
}
