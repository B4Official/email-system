package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.Email;
import io.github.b4official.mail.domain.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class InMemoryEmailRepository {

    public void save(Email email) {
        emails.add(email);
    }
    @PostConstruct
    public void init() {
        User ion = User.builder().id(1L).username("ion").email("ion@gmail.com").build();
        User maria = User.builder().id(2L).username("maria").email("maria@gmail.com").build();

        save(Email.builder().sender(ion).receiver(maria).subject("Salut").body("Ce mai faci?").build());
        save(Email.builder().sender(maria).receiver(ion).subject("Re: Salut").body("Bine, tu?").build());
        save(Email.builder().sender(ion).receiver(maria).subject("Intalnire").body("Mergem maine?").build());
        save(Email.builder().sender(maria).receiver(ion).subject("Re: Intalnire").body("Da, la 10!").build());
        save(Email.builder().sender(ion).receiver(maria).subject("Proiect").body("Ai terminat partea ta?").build());
        save(Email.builder().sender(maria).receiver(ion).subject("Re: Proiect").body("Nu inca, maine gata.").build());
        save(Email.builder().sender(ion).receiver(maria).subject("Test").body("Merge aplicatia?").build());
        save(Email.builder().sender(maria).receiver(ion).subject("Re: Test").body("Da, merge perfect!").build());
        save(Email.builder().sender(ion).receiver(maria).subject("Weekend").body("Ce faci sambata?").build());
        save(Email.builder().sender(maria).receiver(ion).subject("Re: Weekend").body("Stau acasa.").build());
    }
    private final List<Email> emails = new ArrayList<>();

    public List<Email> getAllReceived(User user){

        return emails.stream().filter(email -> email.getReceiver().equals(user)).toList();
    }

    public List<Email> getAllSent(User sender){
        return emails.stream().filter(email -> email.getSender().equals(sender)).toList();
    }
}
