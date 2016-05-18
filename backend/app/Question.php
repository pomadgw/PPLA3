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

    public static $question_types = ['text' => '', 'paragraph' => '',
                              'option' => 'App\QuestionOptions',
                              'checkbox' => 'App\QuestionCheckbox',
                              'scale' => 'App\QuestionScale'];

    public static $question_class = ['text' => '', 'paragraph' => '',
                            'option' => \App\QuestionOptions::class,
                            'checkbox' => \App\QuestionCheckbox::class,
                            'scale' => \App\QuestionScale::class];

    protected $table = 'questions';
    protected $hidden = ['survey_id', 'created_at', 'updated_at'];
    protected $appends = ['args'];

    public function survey() {
        return $this->belongsTo('App\Survey', 'survey_id');
    }

    public function answers() {
        return $this->hasMany('App\Answer', 'question_id');
    }

    public function getArgsAttribute() {
        $type = $this->type;
        if ($type == 'text' || $type == 'paragraph') {
            return null;
        } else {
            return Question::$question_class[$type]::find($this->id);
        }
    }

    public function attributes() {
        $type = $this->type;
        if ($type == 'text' || $type == 'paragraph') {
            return null;
        } else {
            return $this->hasOne(Question::$question_types[$type], 'id', 'id');
        }
    }


}
