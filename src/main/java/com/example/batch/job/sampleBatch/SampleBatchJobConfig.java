package com.example.batch.job.sampleBatch;

import com.example.batch.listener.DefaultJobExecutionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 샘플 배치 작업 정의
 * 스텝과 태스클릿을 조합하여 배치 작업 구성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleBatchJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step sampleMainStep; // SampleStep에서 정의된 빈을 주입
    private final DefaultJobExecutionListener defaultJobExecutionListener;

    /**
     * 샘플 배치 작업 정의
     */
    @Bean
    public Job sampleBatchJob() {
        return jobBuilderFactory.get("sampleBatchJob")
                .listener(defaultJobExecutionListener)
                .start(sampleMainStep)
                .next(sampleMainStep)
                .build();
    }
}