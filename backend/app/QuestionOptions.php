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
    protected $hidden = ['id', 'created_at', 'updated_at'];
    protected $appends = ['options'];

    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }

    public function options()
    {
        return $this->hasMany('App\Options', 'question_id', 'id');
    }

    public function getOptionsAttribute() {
        $ret = array();
        foreach($this->options()->get() as $option) {
            array_push($ret, $option->options);
        }
        return $ret;
    }
}
