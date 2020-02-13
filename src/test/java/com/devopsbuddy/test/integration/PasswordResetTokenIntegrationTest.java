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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {

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

        createPasswordResetToken(token, user, now, expirationTimeMinutes);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());
        Assert.assertNotNull(passwordResetToken.getUser());
    }

    @Test
    public void tesDeleteTokenById() throws Exception {
        User user = super.createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now, expirationTimeMinutes);
        long tokenId = passwordResetToken.getId();
        Assert.assertNotNull(tokenId);
        passwordResetTokenRepository.deleteById(tokenId);

        Optional<PasswordResetToken> shouldNotExists = passwordResetTokenRepository.findById(tokenId);
        Assert.assertFalse(shouldNotExists.isPresent());
    }

    @Test
    public void testDeleteCascadeFromUserEntity() throws Exception {
        User user = super.createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now, expirationTimeMinutes);
        Assert.assertNotNull(passwordResetToken);
        userRepository.deleteById(user.getId());
        log.warn("user.getId():: {}", user.getId());
        Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(5);
        Assert.assertTrue(shouldBeEmpty.isEmpty());
    }

    public void testMultipleTokensAreReturnedWhenQueringByUserId(){
        User user = super.createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> passwordResetTokens = new HashSet<>();
        passwordResetTokens.add(createPasswordResetToken(token1, user,now, expirationTimeMinutes));
        passwordResetTokens.add(createPasswordResetToken(token2, user,now, expirationTimeMinutes));
        passwordResetTokens.add(createPasswordResetToken(token3, user,now, expirationTimeMinutes));

        passwordResetTokenRepository.saveAll(passwordResetTokens);

        Optional<User> findUser = userRepository.findById(user.getId());
        Set<PasswordResetToken> passwordResetTokens1 = passwordResetTokenRepository.findAllByUserId(user.getId());
        Assert.assertEquals(passwordResetTokens.size(), passwordResetTokens1.size());

        List<String> tokenAsList = passwordResetTokens.stream().map( ptr -> ptr.getToken()).collect(Collectors.toList());
        List<String> actualTokenAsList = passwordResetTokens1.stream().map( ptr -> ptr.getToken()).collect(Collectors.toList());
        Assert.assertEquals(tokenAsList.size(), actualTokenAsList.size());
        Assert.assertEquals(tokenAsList, actualTokenAsList);

    }

}
