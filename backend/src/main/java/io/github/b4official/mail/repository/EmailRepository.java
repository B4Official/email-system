package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.Email;
import java.util.List;

public interface EmailRepository {

    void addEmail();
    List<Email> getEmails();
}
