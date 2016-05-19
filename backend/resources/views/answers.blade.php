<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <style media="screen">
            body {
                font-family: "Calibri", "Arial", sans-serif;
                font-size: 11pt;
            }
        </style>
    </head>
    <body>
        <table>
            @for ($i = 0; $i < count($data); $i++)
            <tr>
                @for($j = 0; $j < count($data[$i]); $j++)
                @if ($i == 0)
                <th>
                    {{$data[$i][$j]}}
                </th>
                @else
                <td>
                    {{$data[$i][$j]}}
                </td>
                @endif
                @endfor
            </tr>
            @endfor
        </table>
    </body>
</html>
