# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

#### 구현내용
* http://localhost:8080/index.html로 접속시 응답하는 내용은 webapp/index.html 파일에 작성한다.
* webapp/index.html 파일에 작성한 내용을 http://localhost:8080/index.html로 접속하면 응답한다.

#### 배운점
* 이 웹 서버 구현에서 소켓통신으로 요청이 들어오면, 해당 요청에 대한 쓰레드를 생성하고 start() 메서드 호출로 run() 메서드를 실행한다.
* connection.getInputStream()으로 데이터를 읽어오는데, InputStreamReader로 사용해서 문자 단위로 데이터를 처리해서 가변적인 문자열을 매번 입력 받기에는 불편함이 있다. 따라서, 이 웹 서버 구현에서는 BufferedReader를 사용한다.
    * BufferedReader를 사용하면 한번에 한 줄씩 읽어올 수 있어서, 한번에 읽어온 데이터를 문자열로 변환하는 것이 더 효율적이다.
* readAllBytes() :입력 스트림(InputStream)이나 파일(Path)의 전체 내용을 한 번에 바이트 배열로 읽어들이는 메서드
    * 주의할점 : 큰 용량의 파일은 메모리에 한 번에 모두 읽어들여야 하므로, 메모리 부족 문제가 발생할 수 있다.
    * 큰 파일 처리는 read() 메서드나 readLine() 메서드 등을 사용해야 한다.

### 요구사항 2 - get 방식으로 회원가입

#### 구현내용
* url을 통해 form 태그 내에 input 태그의 name 속성값으로 넘어온다.
* url을 통해 전달된 쿼리스트링을 파싱해서 Map에 저장한다.
* Map에 저장된 쿼리스트링을 이용해서 User 객체를 생성한다.
* User 객체를 이용해서 html 파일을 생성한다.
* 생성한 html 파일을 읽어와서 응답한다.

#### 배운점
* startsWith() 메서드 : 문자열이 특정 문자열로 시작하는지 확인하는 메서드. 특정 문자열로 시작하면 true, 아니면 false를 반환한다.

### 요구사항 3 - post 방식으로 회원가입

#### 구현내용
* post 방식으로 회원가입을 구현하기 위해서는 먼저 HTTP 요청 헤더를 확인해야 한다.
* body 담긴 데이터를 읽어와서 간단 값 유무 검사를 한다.

#### 배운점
* HTTP 요청 헤더에 Content-Type이 application/x-www-form-urlencoded인 경우, POST 방식으로 전송된 데이터를 읽어올 수 있다.
* getContentLength() 메서드 : 헤더에서 Content-Length를 가져오는 메서드. Content-Length는 요청 본문의 길이를 바이트 단위로 표시한다.
    * HTTP 요청에서 body에 들어간 데이터를 가져올 때 사용된다.

### 요구사항 4 - redirect 방식으로 이동

#### 구현내용
* 회원가입 후, index.html로 넘겼지만, url은 "/user/create"로 그대로이며 새로고침 시에 회원가입 요청이 들어간다.
    * 이 문제를 해결하기 위해 응답으로 302 Found를 보내고, Location 헤더에 "/index.html"을 넘겨준다.

#### 배운점
* 302 Found : 리다이렉트 응답 코드. 클라이언트가 리소스를 찾지 못했을 때, 서버가 클라이언트에게 리소스가 다른 위치에 있음을 알려주기 위해 사용한다.
* Location 헤더 : 리다이렉트 응답 코드와 함께 사용되며, 클라이언트가 리다이렉트 될 URL을 담고 있다.

### 요구사항 5 - cookie

#### 구현내용
* 회원가입 시 회원의 정보를 저장한다.
* 로그인 시 응답으로 Set-Cookie 헤더를 보내서 클라이언트에 쿠키를 전달한다.

#### 배운점
* Set-Cookie 헤더 : 클라이언트에 쿠키를 전달하는 응답 헤더.
* Cookie 헤더 : 클라이언트가 서버에 쿠키를 전달하는 요청 헤더.

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 