package com.example.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;

/**
 * 기본 배치 작업 리스너 구현
 */
@Slf4j
@Component
public class DefaultJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String jobId = String.valueOf(jobExecution.getJobId());

        // MDC에 작업 정보 설정 (로그에 포함)
        MDC.put("JOB_ID", jobId);
        MDC.put("JOB_NAME", jobName);

        log.info("===== Job [{}] is starting with parameters: {} =====",
                jobName,
                jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        long duration = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("===== Job [{}] completed successfully in {}ms =====",
                    jobName,
                    duration);

            // 작업 결과 요약 로깅
            log.info("Job Summary: read={}, filtered={}, written={}",
                    jobExecution.getExecutionContext().getLong("READ_COUNT", 0),
                    jobExecution.getExecutionContext().getLong("FILTER_COUNT", 0),
                    jobExecution.getExecutionContext().getLong("WRITE_COUNT", 0));

        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("===== Job [{}] failed in {}ms =====",
                    jobName,
                    duration);

            log.error("Exit Status: {}", jobExecution.getExitStatus().getExitDescription());

            if (!jobExecution.getFailureExceptions().isEmpty()) {
                log.error("Failure Exceptions: {}", jobExecution.getFailureExceptions());
            }
        }

        // MDC에서 작업 정보 제거
        MDC.remove("JOB_ID");
        MDC.remove("JOB_NAME");
    }
} 