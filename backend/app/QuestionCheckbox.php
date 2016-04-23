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
    protected $hidden = ['id', 'created_at', 'updated_at'];
    protected $appends = ['choices'];

    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }

    public function choices()
    {
        return $this->hasMany('App\Choice', 'question_id', 'id');
    }

    public function getChoicesAttribute() {
        $ret = array();
        foreach($this->choices()->get() as $choice) {
            array_push($ret, $choice->choice);
        }
        return $ret;
    }
}
