package com.example.batch.job.sampleChunk.step;

import com.example.batch.job.sampleChunk.model.SampleItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 청크 기반 샘플 배치 스텝 정의
 * ItemReader, ItemProcessor, ItemWriter를 사용한 청크 처리 방식
 */
@Configuration
@RequiredArgsConstructor
public class SampleChunkStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;

    // 청크 처리를 위한 컴포넌트들
    private final SampleChunkItemReader sampleChunkItemReader;
    private final SampleChunkItemProcessor sampleChunkItemProcessor;
    private final SampleChunkItemWriter sampleItemWriter;

    /**
     * 청크 기반 샘플 배치 스텝 - 한 번에 10개씩 처리
     */
    @Bean
    public Step sampleChunkStep() {
        return stepBuilderFactory.get("sampleChunkStep")
                .<SampleItem, SampleItem>chunk(10) // 청크 크기 10으로 설정
                .reader(sampleChunkItemReader)
                .processor(sampleChunkItemProcessor)
                .writer(sampleItemWriter)
                .transactionManager(transactionManager)
                .build();
    }
} 