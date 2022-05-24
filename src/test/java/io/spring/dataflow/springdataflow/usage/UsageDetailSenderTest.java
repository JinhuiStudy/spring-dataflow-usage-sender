package io.spring.dataflow.springdataflow.usage;

import io.spring.dataflow.springdataflow.SpringDataflowApplication;
import io.spring.dataflow.springdataflow.sender.usage.UsageDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.converter.CompositeMessageConverter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UsageDetailSenderTest {


    @Test
    public void testUsageDetailSender() {
        try (var context
                     = new SpringApplicationBuilder(TestChannelBinderConfiguration.getCompleteConfiguration(SpringDataflowApplication.class)).web(WebApplicationType.NONE).run()){

            var target = context.getBean(OutputDestination.class);
            var sourceMessage = target.receive(10000);

            var converter = context.getBean(CompositeMessageConverter.class);
            var usageDetail = (UsageDetail) converter.fromMessage(sourceMessage, UsageDetail.class);

            assertThat(usageDetail.getUserId()).isBetween("user1", "user5");
            assertThat(usageDetail.getData()).isBetween(0L, 700L);
            assertThat(usageDetail.getDuration()).isBetween(0L, 300L);
        }
    }

}