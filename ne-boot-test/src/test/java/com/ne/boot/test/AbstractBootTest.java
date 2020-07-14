package com.ne.boot.test;

import com.ne.boot.service.logging.LoggingFilter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Robin on 10/8/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {NETestConfig.class}, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
@ActiveProfiles({"test"})
public class AbstractBootTest {

    @Autowired(required = false)
    protected WebApplicationContext wac;
    @Autowired(required = false)
    protected LoggingFilter loggingFilter;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(loggingFilter, "/*").build();
    }
}
