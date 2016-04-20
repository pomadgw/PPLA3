<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Dingo\Api\Routing\Helpers;

use Hash;
use Auth;
use Validator;
use App\Http\Requests;
use App\User;
use Symfony\Component\HttpKernel\Exception\UnauthorizedHttpException;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;
use Response;

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
        $this->middleware(['get.token'], ['except' => ['register']]);
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
                   'name' => '',
                   'password' => 'required|min:8',
                   'gender' => 'size:1', // opsi: m (laki-laki) / f (perempuan)
                   'birth_date' => 'date_format:Y-m-d',
                   'profession' => '',
                   'city' => '',
                   'province' => ''];

        $messages = [
            'required'=> 'Parameter :attribute harus ada.',
            'size' => 'Ukuran :attribute harus tepat :size.',
            'min' => 'Parameter :attribute harus minimal berukuran :min karakter.',
            'email' => 'Parameter :attribute harus berformat email.',
        ];

        // Validasi data. Jika ada data yang tidak valid/tidak
        // diisi, akan memunculkan error.
        $validator = Validator::make($request->all(), $params, $messages);
        if ($validator->fails()) {
            return response()->json(['error' => true, 'message' => 'Register gagal', "type" => "failed", 'data' => $validator->errors(), "status_code" => 422], 422);
            
        }

        // jika email yang didaftarkan sudah ada di database,
        // kirim error bahwa email sudah ada
        if (User::where('email', $request->email)->exists()) {
            return response()->json(['error' => true, 'message' => 'Email sudah ada.', "type" => "failed", 'data' => $validator->errors(), "status_code" => 409], 409);
        }

        // jika email belum ada (belum terdaftar) dan datanya
        // lengkap...

        $new_user = new User;

        $new_user->name = $request->name;
        $new_user->email = $request->email;
        $new_user->password = Hash::make($request->password);
        if (!is_null($request->gender))
            $new_user->gender = $request->gender;
        if (!is_null($request->birth_date))
            $new_user->birth_date = $request->birth_date;
        if (!is_null($request->profession))
            $new_user->profession = $request->profession;
        if (!is_null($request->city))
            $new_user->city = $request->city;
        if (!is_null($request->province))
            $new_user->province = $request->province;

        // simpan ke database
        $new_user->save();
        
        return response()->json(['error' => false, 'message' => 'Success register to server', "type" => "success", "status_code" => 201, "user" => $new_user], 201);
    }

    /**
     * Men-update data user
     *
     * Method ini akan udpate data user berdasarkan parameter $request
     * ke dalam database
     * dan akan mengembalikan sukses jika tidak ada error.
     * Jika terdapat error, akan dikembalikan detail errornya.
     *
     * @Post("/{:id}/update")
     * @Versions({"v1"})
     * })
     */
    public function updateUser(Request $request) {
        $curr_id = $this->auth->user()->id;

        // if ($curr_id == $id) {
            $name = $request->name;
            $password = $request->password;
            $gender = $request->gender;
            $birth_date = $request->birth_date;
            $profession = $request->profession;
            $city = $request->city;
            $province = $request->province;

            if ($request->has('password')) {
                $password = Hash::make($password);
            }

            if ($request->has('name')) {
                $this->auth->user()->name = $name;
            }
            if ($request->has('password')) {
                $this->auth->user()->password = $password;
            }
            if ($request->has('gender')) {
                $this->auth->user()->gender = $gender;
            }
            if ($request->has('birth_date')) {
                $this->auth->user()->birth_date = $birth_date;
            }
            if ($request->has('profession')) {
                $this->auth->user()->profession = $profession;
            }
            if ($request->has('city')) {
                $this->auth->user()->city = $city;
            }
            if ($request->has('province')) {
                $this->auth->user()->province = $province;
            }
            if ($request->hasFile('photo') && $request->file('photo')->isValid()) {
                $folder = "/photo/users/" . $curr_id;
                $uri = $folder . "/photo.jpg";
                $destinationPath = public_path() . $uri;

                if (!file_exists(public_path() . $folder)) {
                    mkdir(public_path() . $folder, 0775, true);
                }
                
                $originalFile = $request->file('photo')->getPathname();

                // $image = imagecreatefrompng($originalFile);
                // $request->file('photo')->move($destinationPath);

                UserController::cropImage($originalFile, $destinationPath);
                $this->auth->user()->photo = $uri;
            }

            $this->auth->user()->save();

            return response()->json(['error' => false, 'message' => "Success update info", "status_code" => 200], 200);
        // } else {
        //     throw new \Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException("You don't have access to other user.");
            
        // }
    }

    // Taken from https://gist.github.com/jasdeepkhalsa/4339969
    private static function cropImage($source, $dest, $thumb_width=300, $thumb_height=300) {
        list($width, $height, $type) = getimagesize($source);
        $image = NULL;
        switch ($type) {
            case IMAGETYPE_GIF  :
                $image = imagecreatefromgif($source);
                break;
            case IMAGETYPE_JPEG :
                $image = imagecreatefromjpeg($source);
                break;
            case IMAGETYPE_PNG  :
                $image = imagecreatefrompng($source);
                break;
            default             :
                throw new InvalidArgumentException("Image type $type not supported");
        }

        $filename = $dest;
        $width = imagesx($image);
        $height = imagesy($image);
        $original_aspect = $width / $height;
        $thumb_aspect = $thumb_width / $thumb_height;
        if ( $original_aspect >= $thumb_aspect )
        {
           // If image is wider than thumbnail (in aspect ratio sense)
           $new_height = $thumb_height;
           $new_width = $width / ($height / $thumb_height);
        }
        else
        {
           // If the thumbnail is wider than the image
           $new_width = $thumb_width;
           $new_height = $height / ($width / $thumb_width);
        }
        $thumb = imagecreatetruecolor( $thumb_width, $thumb_height );

        // Resize and crop
        imagecopyresampled($thumb,
                           $image,
                           0 - ($new_width - $thumb_width) / 2, // Center the image horizontally
                           0 - ($new_height - $thumb_height) / 2, // Center the image vertically
                           0, 0,
                           $new_width, $new_height,
                           $width, $height);

        imagejpeg($thumb, $filename, 80);
    }
}
