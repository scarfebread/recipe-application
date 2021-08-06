package thecookingpot.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import thecookingpot.model.User;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class EmailServiceTest
{
    private static final String EMAIL = "EMAIL";
    private static final String TOKEN = "TOKEN";
    private static final String SERVER = "SERVER";
    private static final String USERNAME = "USERNAME";

    @Test
    public void shouldSendPasswordResetEmailWithTheCorrectArguments()
    {
        String expectedText = "Dear USERNAME,\n" +
                "\n" +
                "A password reset was requested. Please follow the following link to reset your password:\n" +
                "http://SERVER/change-password-with-token?token=TOKEN\n" +
                "\n" +
                "Regards,\n" +
                "The Cooking Pot";

        JavaMailSender mailSender = mock(JavaMailSender.class);

        User user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        new EmailService(mailSender).sendPasswordReset(user, TOKEN, SERVER);

        verify(mailSender, timeout(50)).send(argumentCaptor.capture());

        SimpleMailMessage mailMessage = argumentCaptor.getValue();

        assertNotNull(mailMessage.getTo());
        assertEquals(1, mailMessage.getTo().length);
        assertEquals(EMAIL, mailMessage.getTo()[0]);
        assertEquals("noreply@thecookingpot.co.uk", mailMessage.getFrom());
        assertEquals("The Cooking Pot - Password reset", mailMessage.getSubject());
        assertEquals(expectedText, mailMessage.getText());
    }
}
