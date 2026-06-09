package fz.spring.ai.tutorial.hello.scope.context;

import java.io.Serializable;

public class UserContext {

    private String userId;

    public UserContext(String userId) {
        this.userId = userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


}
