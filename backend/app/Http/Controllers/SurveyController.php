<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Dingo\Api\Routing\Helpers;

use App\Http\Requests;
use App\Survey;
use App\Question;
use App\QuestionOptions;
use App\QuestionMultipleChoices;
use App\QuestionScale;
use App\Options;
use App\MultipleChoices;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;

class SurveyController extends Controller
{
    use Helpers;

    public function __construct()
    {
        // buat semua method di bawah ini
        // membutuhkan otentikasi
        $this->middleware(['get.token']);
    }

    public function createSurvey(Request $request) {
        $user_id = $this->auth->user()->id;

        $newSurvey = new Survey;
        $newSurvey->title = $request->title;
        $newSurvey->description = $request->description;
        $newSurvey->coins = $request->coins;
        $newSurvey->user_id = $user_id;

        $newSurvey->save();

        return response()->json(['error' => false, 'message' => "Sukses menambah survey", "status_code" => 200], 200);
    }

    public function getSurvey($id) {
        return Survey::find($id);
    }

    /**
     * Untuk endpoint ini, dia memerlukan data yang di-post
     * berformat JSON. Formatnya:
     *
{
    "type": "<tipe pertanyaan>",
    "question": "<pertanyaan>",
    "arguments": {
        // argument di sini
        // untuk type option:
        "type": "[dropdown|option_button]",
        "options": [ 
            // daftar pilihan di sini, dalam bentuk array
         ]
        // untuk multiple choices:
        "choices": [
            // pilihan di sini, dalam bentuk array
        ]
        // untuk scale:
        "min_val": 0,
        "max_val": 10,
        "min_label": "<label di sini>",
        "max_label": "<label di sini>"
    }
}
     */
    public function createQuestion($surveyId, Request $request) {
        // Verifikasi
        $user_id = $this->auth->user()->id;
        $survey = Survey::find($surveyId);
        if ($survey->user_id != $user_id) {
            throw new AccessDeniedHttpException("Anda tidak bisa memodifikasi survey ini.");
        }

        $type = $request->input('type');

        $newQuestion = new Question;
        
        $newQuestion->question = $request->input('question');
        $newQuestion->type = $type;
        $newQuestion->survey_id = $surveyId;
        
        $newQuestion->save();
        $specific = NULL;

        switch($type) {
            case "option":
                $specific = new QuestionOptions;
                $specific->type = $request->input('arguments.type');
                break;
            case "multiple_choice":
                $specific = new QuestionMultipleChoices;
                break;
            case "scale":
                $specific = new QuestionScale;
                $specific->min_val = $request->input('arguments.min_val');
                $specific->max_val = $request->input('arguments.max_val');
                $specific->min_label = $request->input('arguments.min_label');
                $specific->max_label = $request->input('arguments.max_label');
                break;
            default:
                break;
        }
        
        $specific->id = $newQuestion->id;
        $specific->save();

        if (!is_null($specific)) {
            switch($type) {
                case "option":
                    SurveyController::addOptions($specific->id, $request->input('arguments.options'));
                    break;
                case "multiple_choice":
                    SurveyController::addChoices($specific->id, $request->input('arguments.choices'));
                    break;
                default:
                    break;
            }
        }


        return response()->json(['error' => false, 'message' => "Sukses menambah pertanyaan", "status_code" => 200], 200);
    }

    public function addOptions($id, $options) {
        foreach ($options as $option) {
            $newOption = new Options;
            $newOption->question_id = $id;
            $newOption->options = $option;

            $newOption->save();
        }
    }

    public function addChoices($id, $choices) {
        foreach ($choices as $choice) {
            $newChoice = new MultipleChoices;
            $newChoice->question_id = $id;
            $newChoice->choice = $choice;

            $newChoice->save();
        }
    }
}