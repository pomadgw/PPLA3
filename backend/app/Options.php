<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Options extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'options_val';

    public function question() {
        return $this->belongsTo('App\QuestionOptions', 'question_id', 'id');
    }
}
