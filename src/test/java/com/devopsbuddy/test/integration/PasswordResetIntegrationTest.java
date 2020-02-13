package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class PasswordResetIntegrationTest extends AbstractIntegrationTest {

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeMinutes;
    @Rule
    public TestName testName = new TestName();

    @Before
    public void init(){
        Assert.assertFalse(expirationTimeMinutes <= 0);
    }

    @Test
    public void testTokenExpirationLength() throws Exception {

        User user = super.createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();
        LocalDateTime expectedTime = now.plusMinutes(expirationTimeMinutes);
        Assert.assertNotNull(expectedTime);

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeMinutes);

        LocalDateTime actualTime = passwordResetToken.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(actualTime,expectedTime);
    }

    @Test
    public void testFindTokenByTokenValue() throws Exception {
        User user = super.createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        //createPasswordResetToken(token, user, now);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());
        Assert.assertNotNull(passwordResetToken.getUser());
    }
}
