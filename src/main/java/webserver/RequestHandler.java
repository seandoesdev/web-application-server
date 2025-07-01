package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.LoginController;
import controller.SignupController;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final LoginController loginController = new LoginController();
    private static final SignupController signupController = new SignupController();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String requestLine = br.readLine();

            if(requestLine == null) {
                return;
            }

            String[] tokens = requestLine.split(" ");
            int contentLength = 0;

            // 헤더 마지막 확인
            while(!"".equals(requestLine)){
                requestLine = br.readLine();
                if(requestLine.startsWith("Content-Length")){
                    contentLength = getContentLength(requestLine);
                }
            }

            // 응답
            String method = tokens[0];
            String url = tokens[1];
            if(url.equals("/")) {
                url = "/index.html";
            }
            // * 회원가입 GET 방식
            else if(method.equals("GET") && url.startsWith("/user/create")){
                url = signupController.getSignup(url);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            }
            // * 회원가입 POST 방식
            else if(method.equals("POST") && "/user/create".equals(url)){
                url = signupController.postSignup(IOUtils.readData(br, contentLength));
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            }
            // * 로그인 POST 방식
            else if("/user/login".equals(url)) {
                url = loginController.login(IOUtils.readData(br, contentLength));
                DataOutputStream dos = new DataOutputStream(out);
                response302LoginSuccessHeader(dos);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line){
        String[] headerTokens = line.split(":");

        return Integer.parseInt(headerTokens[1].trim());
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
