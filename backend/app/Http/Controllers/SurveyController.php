<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Dingo\Api\Routing\Helpers;

use App\Http\Requests;
use App\Survey;
use App\Question;
use App\QuestionOptions;
use App\QuestionCheckbox;
use App\QuestionScale;
use App\Options;
use App\Choice;
use Symfony\Component\HttpKernel\Exception\AccessDeniedHttpException;

/**
 * @Resource("Survey", uri="/api/surveys")
 */
class SurveyController extends Controller
{
    use Helpers;

    public function __construct()
    {
        // buat semua method di bawah ini
        // membutuhkan otentikasi
        $this->middleware(['get.token', 'check.confirm']);
    }

    /**
     * Membuat survey baru
     *
     * Method ini akan membuat survey baru.
     * *Endpoint* ini membutuhkan otentikasi.
     *
     * @Post("/add")
     * @Versions({"v1"})
     * })
     */
    public function createSurvey(Request $request) {
        $user_id = $this->auth->user()->id;

        $newSurvey = new Survey;
        $newSurvey->title = $request->title;
        $newSurvey->description = $request->description;
        $newSurvey->coins = $request->coins;
        $newSurvey->user_id = $user_id;

        $newSurvey->save();

        foreach($request->questions as $question) {
            SurveyController::createQuestion($newSurvey->id, $question);
        }

        return response()->json(['error' => false, 'message' => "Sukses menambah survey", "status_code" => 200], 200);
    }

    public function getSurvey($id) {
        $ret = Survey::with('questions')->find($id);
        // $questions = $ret->questions;
        // foreach($questions as $question) {
        //     if ($question->type == 'option') {;
        //         $optionq = QuestionOptions::find($question->id);
        //         $question['args'] = $optionq;
        //         $question['args']['options'] = $optionq->options;
        //     }
        // }
        // $ret['questions'] = $questions;
        return $ret->toArray();
    }

    public function getAllSurveys()
    {
        return Survey::paginate(10);
    }

    public function getUserSurveys() {
        $user = $this->auth->user();
        return $user->surveys;
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
                // untuk checkbox:
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
    public function createQuestionEndpoint($surveyId, Request $request)
    {
        return SurveyController::createQuestion($surveyId, $request->all());
    }
    public function createQuestion($surveyId, $request) {
        // Verifikasi
        // $user_id = $this->auth->user()->id;
        // $survey = Survey::find($surveyId);
        // if ($survey->user_id != $user_id) {
        //     throw new AccessDeniedHttpException("Anda tidak bisa memodifikasi survey ini.");
        // }

        $type = $request['type'];

        $newQuestion = new Question;

        $newQuestion->question = $request['question'];
        $newQuestion->type = $type;
        $newQuestion->survey_id = $surveyId;

        $newQuestion->save();
        $specific = NULL;

        switch($type) {
            case "option":
                $specific = new QuestionOptions;
                $specific->type = $request['args']['type'];
                break;
            case "checkbox":
                $specific = new QuestionCheckbox;
                break;
            case "scale":
                $specific = new QuestionScale;
                $specific->min_val = $request['args']['min_val'];
                $specific->max_val = $request['args']['max_val'];
                $specific->min_label = $request['args']['min_label'];
                $specific->max_label = $request['args']['max_label'];
                break;
            default:
                break;
        }

        if (!is_null($specific)) {
            $specific->id = $newQuestion->id;
            $specific->save();
            switch($type) {
                case "option":
                    SurveyController::addOptions($specific->id, $request['args']['options']);
                    break;
                case "checkbox":
                    SurveyController::addChoices($specific->id, $request['args']['choices']);
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
            $newChoice = new Choice;
            $newChoice->question_id = $id;
            $newChoice->choice = $choice;

            $newChoice->save();
        }
    }

    public function deleteSurvey($surveyId) {
        $survey = Survey::find($surveyId);

        foreach($survey->questions as $question) {
            SurveyController::deleteQuestion($surveyId, $question->id);
        }

        $survey->delete();

        return response()->json(['error' => false, 'message' => "Sukses menghapus survey", "status_code" => 200], 200);
    }

    public function deleteQuestion($surveyId, $questionId) {
        $question = Question::find($questionId);
        $specific = NULL;
        switch($question->type) {
            case "option":
                $specific = QuestionOptions::find($questionId);
                SurveyController::deleteOptions($questionId);
                break;
            case "checkbox":
                $specific = QuestionCheckbox::find($questionId);
                SurveyController::deleteCheckbox($questionId);
                break;
            default:
                break;
        }

        if (!is_null($specific)) {
            $specific->delete();
        }

        $question->delete();

        return response()->json(['error' => false, 'message' => "Sukses menghapus pertanyaan", "status_code" => 200], 200);
    }

    public function deleteCheckbox($questionId)
    {
        Options::where('question_id', $questionId)->delete();
    }

    public function deleteOptions($questionId)
    {
        Choice::where('question_id', $questionId)->delete();
    }
}
