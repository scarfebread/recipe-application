package recipeapplication.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import recipeapplication.model.User;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
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

        assertNotNull(mailMessage.getTo());
        assertEquals(1, mailMessage.getTo().length);
        assertEquals(EMAIL, mailMessage.getTo()[0]);
        assertEquals("noreply@thecookingpot.co.uk", mailMessage.getFrom());
        assertEquals("The Cooking Pot - Password reset", mailMessage.getSubject());
        assertEquals(String.format("http://%s/changePassword?token=%s", SERVER, TOKEN), mailMessage.getText());
    }
}
