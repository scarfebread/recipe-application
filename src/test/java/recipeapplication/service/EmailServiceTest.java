package recipeapplication.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import recipeapplication.model.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EmailServiceTest
{
    private static final String EMAIL = "EMAIL";
    private static final String TOKEN = "TOKEN";
    private static final String SERVER = "SERVER";

    @Test
    public void shouldSendPasswordResetEmailWithTheCorrectArguments()
    {
        JavaMailSender mailSender = mock(JavaMailSender.class);

        User user = new User();
        user.setEmail(EMAIL);

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        new EmailService(mailSender).sendPasswordReset(user, TOKEN, SERVER);

        verify(mailSender).send(argumentCaptor.capture());

        SimpleMailMessage mailMessage = argumentCaptor.getValue();

        assert mailMessage.getTo() != null;
        assert mailMessage.getTo().length == 1;
        assert mailMessage.getTo()[0].equals(EMAIL);

        assert mailMessage.getFrom() != null;
        assert mailMessage.getFrom().equals("noreply@thecookingpot.co.uk");

        assert mailMessage.getSubject() != null;
        assert mailMessage.getSubject().equals("The Cooking Pot - Password reset");

        assert mailMessage.getText() != null;
        assert mailMessage.getText().equals(String.format("http://%s/changePassword?token=%s", SERVER, TOKEN));
    }
}
