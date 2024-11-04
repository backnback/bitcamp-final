package bitcamp.project.service;

public interface MailService {
    void makeRandomNumber() throws Exception;
    String joinEmail(String email) throws Exception;
    void mailSend(String setFrom, String toMail, String title, String content) throws Exception;
}
