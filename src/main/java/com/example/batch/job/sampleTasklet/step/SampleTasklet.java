package com.example.batch.job.sampleTasklet.step;

import com.example.batch.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 샘플 Tasklet 구현 클래스
 * 데이터베이스에서 데이터를 조회하고 처리하는 예시 Tasklet
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleTasklet implements Tasklet {
    private final SampleMapper sampleMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("SampleTasklet started");

        try {
            // 샘플 데이터 조회
            List<Map<String, Object>> dataList = sampleMapper.selectSampleData();

            if (dataList != null && !dataList.isEmpty()) {
                log.info("Retrieved {} records from database", dataList.size());

                // 여기에 실제 비즈니스 로직 구현
                dataList.forEach(data -> {
                    log.debug("Processing data: {}", data);
                    // 데이터 처리 로직
                });
            } else {
                log.info("No data found to process");
            }

            log.info("SampleTasklet completed successfully");
            return RepeatStatus.FINISHED;

        } catch (Exception e) {
            log.error("Error in SampleTasklet: {}", e.getMessage(), e);
            throw e;
        }
    }
} 