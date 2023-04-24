package study.mvc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
            @RequestParam("words") String words) throws IOException
   {
       String wordList[] = words.split(",");
       String result = "";
       // 배열은 0부터 시작
       for(int i = wordList.length - 1; i >= 0; i--) {
           result += wordList[i];
       }
       return result;
   }

//  Multiply Two Numbers
//  GET "/mul?num1=3&num2=4"
//  결과로 문자열 "12" 반환
    @GetMapping(value = "/mul", produces = MediaType.TEXT_PLAIN_VALUE)
    public String mul(
            @RequestParam("num1") int num1,
            @RequestParam("num2") int num2) throws IOException
    {
        int sum = num1 * num2;
        return String.valueOf(sum);
    }

}
