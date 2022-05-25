package io.spring.dataflow.springdataflow.usage;

import io.spring.dataflow.springdataflow.SpringDataflowApplication;
import io.spring.dataflow.springdataflow.UsageDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

class UsageDetailSenderTest {

    // TODO Test Code Error 확인
    @Test
    public void testUsageDetailSender() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration
                        .getCompleteConfiguration(SpringDataflowApplication.class))
                .web(WebApplicationType.NONE)
                .run()) {

            OutputDestination target = context.getBean(OutputDestination.class);
            Message<byte[]> sourceMessage = target.receive(10000);

            final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
            UsageDetail usageDetail = (UsageDetail) converter
                    .fromMessage(sourceMessage, UsageDetail.class);

            assertThat(usageDetail.getUserId()).isBetween("user1", "user5");
            assertThat(usageDetail.getData()).isBetween(0L, 700L);
            assertThat(usageDetail.getDuration()).isBetween(0L, 300L);
        }
    }

}