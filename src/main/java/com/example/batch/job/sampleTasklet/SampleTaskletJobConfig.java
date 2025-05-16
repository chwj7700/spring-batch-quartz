package com.example.batch.job.sampleTasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 샘플 배치 작업 정의
 * 청크 기반의 배치 작업 구성 (ItemReader, ItemProcessor, ItemWriter)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleTaskletJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step sampleChunkStep; // 청크 방식 스텝
    private final JobExecutionListener defaultJobExecutionListener;

    /**
     * 샘플 배치 작업 정의
     */
    @Bean
    public Job sampleTaskletJob() {
        return jobBuilderFactory.get("sampleTaskletJob")
                .listener(defaultJobExecutionListener)
                .start(sampleChunkStep) // 청크 방식 스텝 사용
                .build();
    }
}