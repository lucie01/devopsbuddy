package com.devopsbuddy;

import com.devopsbuddy.config.i18n.I18NConfig;
import com.devopsbuddy.backend.service.I18NService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {I18NService.class, I18NConfig.class})
public class DevopsbuddyApplicationTests {

    @Autowired
    private I18NService i18NService;

    @Test
    public void testMessageByLocaleService() throws Exception {
        String expectedResult = "Bootstrap starter template";
        String messageId = "index.main.callout";
        String resultMessage = i18NService.getMessage(messageId);
        Assert.assertEquals("The actual and spected Strings don't match", expectedResult,resultMessage);
    }
}
