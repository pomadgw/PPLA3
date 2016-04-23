<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Question extends Model
{
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'question', 'type'
    ];

    public $question_types = ['text' => '', 'paragraph' => '',
                              'option' => 'App\QuestionOptions',
                              'checkbox' => 'App\QuestionCheckbox',
                              'scale' => 'App\QuestionScale'];

    protected $table = 'questions';

    public function survey() {
        return $this->belongsTo('App\Survey', 'survey_id');
    }

    public function getQuestionDetail() {
        $type = $this->type;
        if ($type == 'text' || $type == 'paragraph') {
            return null;
        } else {
            return $this->hasOne($question_types[$type], 'id', 'id');
        }
    }
}
