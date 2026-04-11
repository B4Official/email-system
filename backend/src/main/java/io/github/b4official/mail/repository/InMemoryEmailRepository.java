package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.Email;
import io.github.b4official.mail.domain.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class InMemoryEmailRepository {

    public Email save(Email email) {
        Email emailWithId = Email.builder()
                .id((long) emails.size() + 1)
                .sender(email.getSender())
                .receiver(email.getReceiver())
                .subject(email.getSubject())
                .body(email.getBody())
                .sentTime(email.getSentTime())
                .build();
        emails.add(emailWithId);
        return emailWithId;
    }
    @PostConstruct
    public void init() {
        User ion = User.builder().id(1L).username("ion").email("ion@test.com").build();
        User maria = User.builder().id(2L).username("maria").email("maria@test.com").build();

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
        return emails.stream().filter(email -> email.getReceiver().getEmail().equals(user.getEmail())).toList();
    }

    public List<Email> getAllSent(User sender){
        return emails.stream().filter(email -> email.getSender().getEmail().equals(sender.getEmail())).toList();
    }
}
