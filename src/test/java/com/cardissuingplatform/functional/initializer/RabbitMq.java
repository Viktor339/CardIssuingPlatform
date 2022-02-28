package com.cardissuingplatform.functional.initializer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

@UtilityClass
public class RabbitMq {

    public static GenericContainer<?> rabbitmqContainer;

    static {
        rabbitmqContainer =
                new GenericContainer<>("rabbitmq:3.9-management")
                        .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("rabbitmq"))
                        .withExposedPorts(5672)
                        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5672), new ExposedPort(5672)))));
        rabbitmqContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbitmqContainer.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbitmqContainer.getMappedPort(5672)
            ).applyTo(configurableApplicationContext);
        }
    }

}
