package io.b4official.mail.domain;

public class User {

    String userName;
    String name;
    String surname;

    public User(String userName, String name, String surname){
        this.userName = userName;
        this.name = name;
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
