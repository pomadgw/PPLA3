<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class MultipleChoices extends Model
{
    //
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'multichoice_val';
    
    public function question() {
        return $this->belongsTo('App\QuestionMultipleChoices', 'question_id', 'id');
    }
}
