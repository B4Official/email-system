package io.github.b4official.mail.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class User {

    String userName;
    String name;
    String surname;

    public User(String userName, String name, String surname){
        this.userName = userName;
        this.name = name;
        this.surname = surname;
    }

}
