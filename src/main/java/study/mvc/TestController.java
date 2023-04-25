package study.mvc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Now {
    private String date;
    private String time;

    public Now(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public void setDate(String date) { this.date = date; }

    public void setTime(String time) { this.time = time; }
}
@RestController
@RequestMapping("/test")
public class TestController {
    //  GET "/hello"
//  문자열 "Hello World!" 화면에 표시되도록 핸들러 메서드 구현
//  (스프링스럽게 구현, request, response 객체 안 쓰고)
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    //  Reverse String
//  GET "/reverse?words=hello,world,mirim"
//  문자열 "mirim,world,hello"를 표시하도록 핸들러 메서드 구현
//  힌트) @RequestParam 어노테이션, split 메서드 쓸 것
    @GetMapping(value = "/reverse", produces = MediaType.TEXT_PLAIN_VALUE)
    public String reverse(
            @RequestParam("words") String words) throws IOException {
        String wordList[] = words.split(",");
        String result = "";
        // 배열은 0부터 시작
        for (int i = wordList.length - 1; i >= 0; i--) {
            result += wordList[i];
        }
        return result;
    }

//  Multiply Two Numbers
//  GET "/mul?num1=3&num2=4"
//  결과로 문자열 "12" 반환
//    @GetMapping(value = "/mul", produces = MediaType.TEXT_PLAIN_VALUE)
//    public String mul(
//            @RequestParam("num1") int num1,
//            @RequestParam("num2") int num2) throws IOException
//    {
//        int mul = num1 * num2;
//        return String.valueOf(mul);
//    }

    //  Basic Calculator
//  GET "/calc/mul?num1=3&num2=4" => "12" 반환
//  GET "/calc/add?num1=3&num2=4" => "7" 반환
//  GET "/calc/sub?num1=3&num2=4" => "-1" 반환
    @GetMapping(value = "/calc/{op}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String calculation(
            @PathVariable(value = "op", required = true) String op,
            @RequestParam("num1") int num1,
            @RequestParam("num2") int num2) throws Exception {

        int result = 0;
        // calculation => "mul", "add", "sub"
        if (op.equals("mul")) {
            result = num1 * num2;
        } else if (op.equals("add")) {
            result = num1 + num2;
        } else if (op.equals("sub")) {
            result = num1 - num2;
        } else {
            throw new Exception("해당 연산자는 지원하지 않습니다");
        }
        return String.valueOf(result);
    }

    //  Count Request Method
//  POST "/count"
//  매번 호출할 때마다 해당 핸들러 메서드가 호출된 횟수를 반환
//  첫 번째 호출 때는 1, 두 번째 호출 때는 2, ...,
//  힌트) 컨트롤러에 count 값을 저장할 정수 필드가 필요함
    private int count = 0;

    @PostMapping("/count")
    public int count() {
        count++;
        return count;
    }

//  GET "/now"
//  현재 날짜 시간을 JSON으로 반환
//  힌트) 응답 메시지에 변환되어서 포함될 객체의 클래스를 하나 정의해야 함
//  {
//      "data": "2023-4-25",
//      "time":"9:35:18"
//  }
    @GetMapping(value = "/now", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Now now() {
        LocalDateTime dateTime = LocalDateTime.now();
        return new Now(
            dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"))
        );
    }


}
