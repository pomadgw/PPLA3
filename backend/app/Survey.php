<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Survey extends Model
{
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'title', 'description', 'coins'
    ];

    protected $table = 'surveys';

    public function user() {
        return $this->belongsTo('App\User', 'user_id');
    }

    public function questions() {
        return $this->hasMany('App\Question', 'survey_id', 'id');
    }
}
