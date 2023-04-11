package study.mvc;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/renew")
public class MyRenewController {

    // produces 옵션을 통해서 미디어 타입 지정 가능 (유추해서 자동으로 지정하게 할 수도 있지만가급적 써주는 것을 권장)
    @GetMapping(value = "/echo", produces = MediaType.TEXT_PLAIN_VALUE)
    // 반환한 문자열이 바로 응답 메시지의 바디 데이터에 삽입될 수 있도록 @ResponseBody 어노테이션 추가 (@RestController를 사용하면 생략 가능)
    @ResponseBody
    public String echo(@RequestBody byte[] content) {
    // 메서드 정보 접근할 필요 없음 (GET 메서드)
    // 주소 정보 접근할 필요 없음 (@PathVariable 사용)
    // 쿼리 스트링 정보 접근할 필요 없음 (@RequestParam 사용)
    // 프로토콜, HTTP 버전 정보 접근할 필요가 보통 없음
    // 헤더 정보 접근할 필요 없음 (@RequestHeader 사용)
    // 요청 메시지의 바디 데이터 접근은 @RequestBody 어노테이션을 이용해서 전달받을 수 있음
        String bytesToString = new String(content, StandardCharsets.UTF_8);
        System.out.println(bytesToString);
    // Content-Type 헤더의 경우 produces 옵션을 제공하여 미디어 타입 지정 가능
        return bytesToString;
    }

    // "/hello-html"
    @GetMapping(value = "/hello-html", produces = MediaType.TEXT_HTML_VALUE)
    // 반환 코드도 마찬가지로 그냥 성공적으로 메서드에서 값을 반환하면 자동으로 200이 됨
    // @ResponseStatus를 지워도 출력
    @ResponseStatus(HttpStatus.OK)
    public String helloHTML() {
        return "<h1>Hello</h1>";
    }

    @GetMapping(value = "/hello-xml", produces = MediaType.TEXT_XML_VALUE)
    // 생략가능
    @ResponseStatus(HttpStatus.OK)
    public String helloXML() {
        return "<text>Hello</text>";
    }

    @GetMapping(value = "/hello-json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String helloJSON() {
        return "{ \"data\": \"Hello\" }";
    }

    // @RequestHeader 사용
//    @GetMapping(value = "/echo-repeat", produces = MediaType.TEXT_PLAIN_VALUE)
//    // @RequestHeader 어노테이션을 통해서 X-Repeat-Count에 적힌 숫자 정보 가져오고 없으면 1로 초기화
//    public String echoRepeat(@RequestParam("word") String word, @RequestHeader(value
//            = "X-Repeat-Count", defaultValue = "1") Integer repeatCount) throws IOException
//    {
//        String result = "";
//        for(int i=0;i<repeatCount;i++) {
//            result += word;
//        }
//        return result;
//    }

    // @PathVariable 사용
    @GetMapping(value = "/echo-repeat/{repeatCount}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String echoRepeat2(
            @PathVariable(value = "repeatCount", required = true) Integer repeatCount,
            @RequestParam("word") String word) throws IOException
    {
        String result = "";
        for(int i=0;i<repeatCount;i++) {
            result += word;
        }
        return result;
    }

    // @RequestParam 추가
    @GetMapping(value = "/echo-repeat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String echoRepeat3(
            @RequestParam("word") String word,
            @RequestParam("repeatCount") Integer repeatCount) throws IOException
    {
        String result = "";
        for(int i=0;i<repeatCount;i++) {
            result += word;
        }
        return result;
    }

}
