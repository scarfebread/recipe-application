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

        message.setSubject("The Cooking Pot - Password reset");
        message.setFrom(FROM_EMAIL);
        message.setTo(user.getEmail());

        String text = String.format(
                "Dear %s,\n\nA password reset was requested. Please follow the following link to reset your password:\n" +
                "http://%s/changePassword?token=%s\n\nRegards,\nThe Cooking Pot",
                user.getUsername(),
                serverName,
                token
        );

        message.setText(text);

        mailSender.send(message);
    }
}
