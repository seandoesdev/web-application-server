package controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final DataBase Database = new DataBase();

    public String login(String body){

        Map<String, String> bodyParams = HttpRequestUtils.parseQueryString(body);

        String userId   = bodyParams.get("userId");

        User user = Database.findUserById(userId);

        if(user == null || !user.isValid()){
            log.error("user 정보가 유효하지 않습니다. {}", user);
            return "/user/login_failed.html";
        }
        return "/index.html";
    }

}
