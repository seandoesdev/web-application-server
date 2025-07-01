package controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import db.DataBase;
import util.HttpRequestUtils;

public class SignupController {
    private static final Logger log = LoggerFactory.getLogger(SignupController.class);
    private static final DataBase Database = new DataBase();

    /**
     * 회원가입
     * @param url 
     * @return 1: 성공, 0: 실패
     */
    public String getSignup(String url){
        int queryStartIndex = url.indexOf("?");
        String queryString = url.substring(queryStartIndex + 1);
        Map<String, String> queryParams = HttpRequestUtils.parseQueryString(queryString);

        User user = new User(queryParams.get("userId"), queryParams.get("password"), 
        queryParams.get("name"), queryParams.get("email"));

        if(!user.isValid()){
            log.error("user 정보가 유효하지 않습니다. {}", user);
            return "/user/create_failed.html";
        }

        log.info("user => {}", user);
        return "/index.html";
    }

    /**
     * POST 방식으로 회원가입
     * @param body
     * @return 1: 성공, 0: 실패
     */
    public String postSignup(String body){
        Map<String, String> bodyParams = HttpRequestUtils.parseQueryString(body);
        User user = new User(bodyParams.get("userId"), bodyParams.get("password"), 
        bodyParams.get("name"), bodyParams.get("email"));

        if(!user.isValid()){
            log.error("user 정보가 유효하지 않습니다. {}", user);
            return "/user/create_failed.html";
        }

        Database.addUser(user);
        log.info("user => {}", user);
        return "/index.html";
    }
}
