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
    
    public function question() {
        return $this->belongsTo('App\QuestionCheckbox', 'question_id', 'id');
    }
}
