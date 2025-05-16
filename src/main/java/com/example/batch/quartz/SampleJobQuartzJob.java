package com.example.batch.quartz;

import com.example.batch.exception.BatchException;
import com.example.batch.util.BatchJobUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 샘플 배치 작업을 실행하는 Quartz Job
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SampleJobQuartzJob extends QuartzJobBean {

    private final Job sampleTaskletJob;
    private final Job sampleChunkJob;

    private final JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jobKey = context.getJobDetail().getKey().toString();
        String triggerKey = context.getTrigger().getKey().toString();
        Date scheduledTime = context.getScheduledFireTime();

        // MDC에 Quartz 작업 정보 설정
        MDC.put("QUARTZ_JOB_KEY", jobKey);
        MDC.put("QUARTZ_TRIGGER", triggerKey);

        log.info("===== Quartz Job [{}] triggered by [{}] at {} =====", jobKey, triggerKey, scheduledTime);

        try {
            // Job 파라미터 생성 - 매번 다른 파라미터를 주어 중복 실행이 가능하도록 함
            // Spring Batch Job 실행
            log.info("Launching Spring Batch Job: {}", sampleTaskletJob.getName());
            JobParameters jobParameters = BatchJobUtils.createJobParameters(sampleTaskletJob.getName());
            jobLauncher.run(sampleTaskletJob, jobParameters);

            log.info("Launching Spring Batch Job: {}", sampleChunkJob.getName());
            jobParameters = BatchJobUtils.createJobParameters(sampleChunkJob.getName());
            jobLauncher.run(sampleChunkJob, jobParameters);

            log.info("===== Quartz Job [{}] completed =====", jobKey);
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Error executing Quartz Job: {}", e.getMessage(), e);
            throw new JobExecutionException(new BatchException("QUARTZ_BATCH_ERROR",
                    "Failed to execute batch job through Quartz: " + e.getMessage(), e));
        } finally {
            // MDC에서 Quartz 작업 정보 제거
            MDC.remove("QUARTZ_JOB_KEY");
            MDC.remove("QUARTZ_TRIGGER");
        }
    }
} 