package study.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

 // 커맨드 객체로 사용될 클래스 정의
 class MyCommandObject {
     // 필드 네임 그대로 전달해야하므로 필드 네임이 중요하다
     private String value1;
     private Integer value2;
     // 반드시 세터 메서드가 있어야 함
     // 세터로 값을 넣기 때문에 무조건 세터 메서드가 존재해야함
     public void setValue1(String value1) { this.value1 = value1; }
     public void setValue2(Integer value2) { this.value2 = value2; }

     @Override
     public String toString() {
         return "MyCommandObject{" +
                 "value1='" + value1 + '\'' +
                 ", value2=" + value2 +
                 '}';
     }
 }

 // Json 직렬화, 역직렬화에 사용될 클래스 정의
class MyJsonData {
    private String value1;
    private Integer value2;
    // 직렬화
    public String getValue1() { return value1; }
    public Integer getValue2() { return value2; }
     // 역직렬화
    public void setValue1(String value1) { this.value1 = value1; }
    public void setValue2(Integer value2) { this.value2 = value2; }

     @Override
     public String toString() {
         return "MyJsonData{" +
                 "value1='" + value1 + '\'' +
                 ", value2=" + value2 +
                 '}';
     }
 }

 class Student {
     private String id;
     public String name;
     public Integer age;

     public String getId() { return id; }

     public String getName() { return name; }

     public Integer getAge() { return age; }

     public void setId(String id) { this.id = id; }

     public void setName(String name) { this.name = name; }

     public void setAge(Integer age) { this.age = age; }

     @Override
     public String toString() {
         return "Student{" +
                 "id='" + id + '\'' +
                 ", name='" + name + '\'' +
                 ", age=" + age +
                 '}';
     }
 }


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

    // "/dog-image"
    @GetMapping(value = "/dog-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] dogImage() throws IOException {
    // resources 폴더의 static 폴더에 이미지 있어야 함
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
    // 파일의 바이트 데이터 모두 읽어오기
        byte[] bytes = Files.readAllBytes(file.toPath());
        return bytes;
    }

    @GetMapping(value = "/dog-image-file", produces =
            MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // 헤더를 직접 조정하고 싶은 경우 ResponseEntity 타입을 반환하도록 설정 가능
    // (꺽쇠 안에는 응답 메시지의 바디 데이터에 포함될 타입을 지정)
    public ResponseEntity<byte[]> dogImageFile() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
        byte[] bytes = Files.readAllBytes(file.toPath());
        String filename = "dog.jpg";

        // 헤더 값 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    // Post요청 Spring식으로 코드 변환
    private ArrayList<String> wordList = new ArrayList<>();
    // 위의 ArrayList에 단어를 추가하는 메서드
    @PostMapping("/words")
    // post로 하면 201을 지정해주는 것이 좋기 때문이다.
    // @ResponseStatus : 응답 메서드의 상태 코드를 받음
    @ResponseStatus(HttpStatus.CREATED)
    // @RequestBody : body에 있는 내용을 가져옴
    // body 영역의 내용이 String 타입임
    public void addWord(@RequestBody String bodyString) {
          String[] words = bodyString.split("\n");
//        trim() : 앞 뒤 공백 문자 제거
          for(String w : words) wordList.add(w.trim());
    }
    
    // 메서드를 받는 것
    @GetMapping("/words")
    public String showWords() {
        return String.join(", ", wordList);
    }

    // 컨트롤러 메서드 정의
    @PostMapping("/test")
    // @ModelAttribute를 타입 앞에 붙여주고 메서드의 파라미터 값으로 전달되게 함
    // @ModelAttribute 붙이지 않아도 되지만 붙이는 것이 좋음!
    public String commandObjectTest(@ModelAttribute MyCommandObject myCommandObject)
    {
        return myCommandObject.toString();
    }

    // 요청 메시지의 Content-Type이 "applicaion/json"인 요청을 받아들이기 위해서 consumes를 MediaType.APPLICATION_JSON_VALUE로 설정
    // 응답 메시지의 Content-Type이 "applicaion/json"이므로 produces를
    // MediaType.APPLICATION_JSON_VALUE로 설정
    @PostMapping(value = "/json-test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    // 반환 타입으로 MyJsonData를 설정하였고 ResponseBody 어노테이션을 통해 해당 값이 메시지 컨버터를 통해서 직렬화되어야 함을 알림
    @ResponseBody
    // RequestBody 어노테이션을 통해 요청 메시지의 바디에 포함된 JSON 문자열이 메시지 컨버터를 통해서 역직렬화되어 객체로 변환되어야 함을 알림

    // consumes - @RequestBody(json -> 객체)
    // produces - @ResponseBody(객체 -> json)
    public MyJsonData jsonTest(@RequestBody MyJsonData myJsonData) {
        System.out.println(myJsonData);
        return myJsonData;
    }

    @PostMapping(value = "/student-test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Student studentTest(@RequestBody Student s) {
        System.out.println(s);
        return s;
    }
}

