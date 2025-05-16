package com.example.batch.job.sampleBatch.step;

import com.example.batch.job.sampleTasklet.step.SampleTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 샘플 배치 스텝 정의
 */
@Configuration
@RequiredArgsConstructor
public class SampleTaskletStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;
    private final SampleTasklet sampleTasklet;

    /**
     * 샘플 배치 작업의 메인 스텝
     */
    @Bean
    public Step sampleMainStep() {
        return stepBuilderFactory.get("sampleMainStep")
                .tasklet(sampleTasklet)
                .transactionManager(transactionManager)
                .build();
    }
} 