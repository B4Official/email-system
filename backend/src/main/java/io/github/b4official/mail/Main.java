package io.github.b4official.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Aceasta e ștampila magică ce trezește tot proiectul la viață!
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        System.out.println("The app is running...");
    }
}