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
* 

### 요구사항 3 - post 방식으로 회원가입
* 

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 