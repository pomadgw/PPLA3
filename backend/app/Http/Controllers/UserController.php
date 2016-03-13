<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Auth;
use Hash;
use App\Http\Requests;
use App\User;
use JWTAuth;
use TymonJWTAuthExceptionsJWTException;
use TymonJWTAuthMiddlewareGetUserFromToken;

class UserController extends Controller
{
    public function __construct()
    {
       // Apply the jwt.auth middleware to all methods in this controller.
       $this->middleware('jwt.auth', ['except' => ['register']]);
    }

    public function get_current_user_info()
    {
        return Auth::user();
    }

    /**
     * Method ini akan memasukkan data di parameter $request
     * (dari pengiriman info user baru) ke dalam database
     * dan akan mengembalikan token JSON yang siap dipakai.
     * $request harus berisi:
     * - email
     * - name
     * - password
     * - gender (nilai hanya ada dua: 'm' (untuk laki-laki)
         dan 'f' (untuk perempuan))
     * - birth_date (tanggal lahir, formatnya 'YYYY-MM-DD')
     * - profession
     * - city (kota/kabupaten)
     * - province
     */
    public function register(Request $request)
    {
        $error = [];
        $params = ['email', 'name', 'password', 'gender', 'birth_date', 'profession',
                  'city', 'province'];
        
        foreach ($params as $param) {
            if(!$request->has($param)) {
                array_push($error, $param);
            }
        }

        // jika email yang didaftarkan sudah ada di database,
        // kirim error bahwa email sudah ada
        if (User::where('email', $request->email)->exists()) {
            return response()->json(['error' => 'email_exists'], 409);
        }

        // jika ada data yang belum lengkap, kirim error
        // untuk melengkapi data pendaftaran
        if (!empty($error)) {
            return response()->json(['error' => 'missing_data', 'data' => $error], 400);
        }

        // jika email belum ada (belum terdaftar) dan datanya
        // lengkap...

        $new_user = new User;

        $new_user->name = $request->name;
        $new_user->email = $request->email;
        $new_user->password = Hash::make($request->password);
        $new_user->gender = $request->gender;
        $new_user->birth_date = $request->birth_date;
        $new_user->profession = $request->profession;
        $new_user->city = $request->city;
        $new_user->province = $request->province;

        // simpan ke database
        $new_user->save();
        
        return response()->json(['error' => 'success_register'], 201);
    }
}
