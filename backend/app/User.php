<?php

namespace App;

use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password', 'gender', 'profession', 'birth_date',
        'city', 'province', 'coins', 'photo'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    public function surveys() {
        return $this->hasMany('App\Survey', 'user_id', 'id');
    }

    public function answers() {
        return $this->hasMany('App\Answer', 'user_id', 'id');
    }
}
