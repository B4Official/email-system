package io.b4official.mail.repository;

import io.b4official.mail.domain.Email;
import java.util.List;

public interface EmailRepository {

    void addEmail();
    List<Email> getEmails();
}
