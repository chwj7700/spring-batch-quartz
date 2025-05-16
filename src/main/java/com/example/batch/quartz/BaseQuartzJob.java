package com.example.batch.quartz;

import com.example.batch.constants.BatchConstants;
import com.example.batch.util.DateUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.UUID;

public abstract class BaseQuartzJob extends QuartzJobBean {

    /**
     * 배치 작업 파라미터 생성
     */
    public JobParameters createJobParameters() {
        String currentDate = DateUtils.getCurrentDateAsString();
        String runId = UUID.randomUUID().toString().substring(0, 8);

        return new JobParametersBuilder()
                .addString(BatchConstants.JobParameters.RUN_ID, runId)
                .addString(BatchConstants.JobParameters.SCHEDULE_DATE, currentDate)
                .addLong(BatchConstants.JobParameters.TIMESTAMP, System.currentTimeMillis())
                .toJobParameters();
    }
}
