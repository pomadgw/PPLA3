<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Dingo\Api\Routing\Helpers;

use Hash;
use Validator;
use App\Http\Requests;
use App\User;

/**
 * @Resource("Users", uri="/api/users")
 */
class UserController extends Controller
{
    use Helpers;

    public function __construct()
    {
        // buat semua method di bawah ini kecuali `register`
        // membutuhkan otentikasi
        $this->middleware('api.auth', ['except' => ['register']]);
        // $this->scopes('read_user_data', ['get_current_user_info']);
    }

    /**
     * Mendapatkan data user yang sedang login
     *
     * Method ini akan mengembalikan data user yang
     * sedang login (berdasarkan token yang digunakan).
     * *Endpoint* ini membutuhkan otentikasi.
     * 
     * @Get("/current")
     * @Versions({"v1"})
     * })
     */
    public function get_current_user_info()
    {
        $user = $this->auth->user();
        return $user;
    }

    /**
     * Mendaftarkan user baru
     *
     * Method ini akan memasukkan data di parameter $request
     * (dari pengiriman info user baru) ke dalam database
     * dan akan mengembalikan sukses jika tidak ada error.
     * Jika terdapat error, akan dikembalikan detail errornya.
     *
     * @Post("/register")
     * @Versions({"v1"})
     * })
     */
    public function register(Request $request)
    {
        $error = [];

        // parameter yang dibutuhkan:
        $params = ['email' => 'required|email',
                   'name' => 'required',
                   'password' => 'required|min:8',
                   'gender' => 'required|size:1', // opsi: m (laki-laki) / f (perempuan)
                   'birth_date' => 'required|date_format:Y-m-d',
                   'profession' => 'required',
                   'city' => 'required',
                   'province' => 'required'];

        $messages = [
            'required'=> ':attribute harus ada.',
            'size' => 'Ukuran :attribute harus tepat :size.',
            'min' => ':attribute harus minimal berukuran :min karakter.',
            'email' => ':attribute harus berformat email.',
        ];

        // Validasi data. Jika ada data yang tidak valid/tidak
        // diisi, akan memunculkan error.
        $validator = Validator::make($request->all(), $params, $messages);
        if ($validator->fails()) {
            return response()->json(['error' => 'register_failed', 'description' => $validator->errors()], 422);
        }

        // jika email yang didaftarkan sudah ada di database,
        // kirim error bahwa email sudah ada
        if (User::where('email', $request->email)->exists()) {
            return response()->json(['error' => 'Email is already registered.'], 409);
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
