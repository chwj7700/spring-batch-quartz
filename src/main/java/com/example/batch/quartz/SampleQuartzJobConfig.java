package com.example.batch.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleQuartzJobConfig {

    @Bean
    public Trigger sampleJobTrigger() {
        // 매 시간마다 실행 (예시)
        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail())
                .withIdentity("sampleJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?")
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJobQuartzJob.class)
                .withIdentity("sampleJobQuartzJob")
                .storeDurably()
                .requestRecovery(true)
                .build();
    }
} 