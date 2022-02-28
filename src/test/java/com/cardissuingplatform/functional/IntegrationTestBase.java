package com.cardissuingplatform.functional;

import com.cardissuingplatform.functional.initializer.Postgres;
import com.cardissuingplatform.functional.initializer.RabbitMq;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        Postgres.Initializer.class,
        RabbitMq.Initializer.class
})
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

}
