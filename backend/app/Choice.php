<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Choice extends Model
{
    //
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'checkbox_val';
    protected $hidden = ['id', 'question_id', 'created_at', 'updated_at'];

    public function question() {
        return $this->belongsTo('App\QuestionCheckbox', 'question_id', 'id');
    }
}
