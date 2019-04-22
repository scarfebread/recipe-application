package recipeapplication.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "passwordResetTokens")
public class PasswordResetToken
{
    private static final int ONE_DAY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    private Date expiryDate;

    public PasswordResetToken() {};

    public PasswordResetToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.expiryDate = getDateAfter24Hours();
    }

    private Date getDateAfter24Hours()
    {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, ONE_DAY);

        return calendar.getTime();
    }

    public boolean isExpired()
    {
        return new Date().compareTo(expiryDate) >= 0;
    }

    public User getUser()
    {
        return user;
    }

    public String getToken()
    {
        return token;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }
}
