<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class QuestionScale extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'scale_questions';
    protected $hidden = ['id', 'created_at', 'updated_at'];

    public function question()
    {
        return $this->belongsTo('App\Question', 'id', 'id');
    }
}
