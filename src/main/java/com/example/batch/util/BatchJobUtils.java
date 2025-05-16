package com.example.batch.util;

import com.example.batch.constants.BatchConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import java.util.UUID;

/**
 * 배치 작업 관련 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BatchJobUtils {

    /**
     * JobParameters 생성
     *
     * @param jobName 작업 이름
     * @return 작업 파라미터
     */
    public static JobParameters createJobParameters(String jobName) {
        String runId = generateRunId();
        String currentDate = DateUtils.getCurrentDateAsString();

        return new JobParametersBuilder()
                .addString(BatchConstants.JobParameters.RUN_ID, runId)
                .addString(BatchConstants.JobParameters.SCHEDULE_DATE, currentDate)
                .addString(BatchConstants.JobParameters.JOB_NAME, jobName)
                .addLong(BatchConstants.JobParameters.TIMESTAMP, System.currentTimeMillis())
                .toJobParameters();
    }

    /**
     * 작업 실행 고유 ID 생성
     *
     * @return 작업 실행 ID
     */
    public static String generateRunId() {
        String timestamp = DateUtils.getCurrentDateTimeAsString();
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid;
    }

    /**
     * MDC에 작업 정보 설정
     *
     * @param jobExecution 작업 실행 정보
     */
    public static void setupJobMdc(JobExecution jobExecution) {
        if (jobExecution != null) {
            MDC.put(BatchConstants.LogMdcKeys.JOB_ID, String.valueOf(jobExecution.getJobId()));
            MDC.put(BatchConstants.LogMdcKeys.JOB_NAME, jobExecution.getJobInstance().getJobName());
        }
    }

    /**
     * MDC에 스텝 정보 설정
     *
     * @param stepExecution 스텝 실행 정보
     */
    public static void setupStepMdc(StepExecution stepExecution) {
        if (stepExecution != null) {
            MDC.put(BatchConstants.LogMdcKeys.STEP_ID, stepExecution.getStepName());
        }
    }

    /**
     * MDC에서 작업 정보 제거
     */
    public static void clearJobMdc() {
        MDC.remove(BatchConstants.LogMdcKeys.JOB_ID);
        MDC.remove(BatchConstants.LogMdcKeys.JOB_NAME);
        MDC.remove(BatchConstants.LogMdcKeys.STEP_ID);
    }

    /**
     * 작업 실행 컨텍스트에 읽기/쓰기 카운트 증가
     *
     * @param executionContext 실행 컨텍스트
     * @param readCount        읽기 건수
     * @param writeCount       쓰기 건수
     */
    public static void updateExecutionCounts(ExecutionContext executionContext, long readCount, long writeCount) {
        long currentReadCount = executionContext.getLong(BatchConstants.JobContext.READ_COUNT, 0);
        long currentWriteCount = executionContext.getLong(BatchConstants.JobContext.WRITE_COUNT, 0);

        executionContext.putLong(BatchConstants.JobContext.READ_COUNT, currentReadCount + readCount);
        executionContext.putLong(BatchConstants.JobContext.WRITE_COUNT, currentWriteCount + writeCount);
    }
} 