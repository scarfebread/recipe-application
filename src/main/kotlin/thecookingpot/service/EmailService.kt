package thecookingpot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import thecookingpot.model.User
import kotlin.concurrent.thread

@Service
class EmailService @Autowired constructor(private val mailSender: JavaMailSender) {
    fun sendPasswordReset(user: User, token: String, serverName: String) {
        val message = SimpleMailMessage().apply {
            subject = SUBJECT
            from = FROM_EMAIL
            setTo(user.email)

            text = """
                Dear ${user.username},
                
                A password reset was requested. Please follow the following link to reset your password:
                http://${serverName}/change-password-with-token?token=${token}
                
                Regards,
                The Cooking Pot
            """.trimIndent()
        }

        thread { mailSender.send(message) }
    }

    companion object {
        private const val FROM_EMAIL = "noreply@thecookingpot.co.uk"
        private const val SUBJECT = "The Cooking Pot - Password reset"
    }
}