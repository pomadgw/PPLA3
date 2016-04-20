<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class QuestionOptions extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'option_questions';
    
    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }

    public function options()
    {
        return $this->hasMany('App\Options', 'question_id', 'id');
    }
}
