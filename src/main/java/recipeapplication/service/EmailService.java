package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;

@Service
public class EmailService
{
    private static final String FROM_EMAIL = "noreply@thecookingpot.co.uk";

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void sendPasswordReset(User user, String token, String serverName)
    {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("The Cooking Pot - Password reminder");
        message.setFrom(FROM_EMAIL);
        message.setTo(user.getEmail());
        message.setText(String.format("http://%s/changePassword?token=%s", serverName, token));

        mailSender.send(message);
    }
}
