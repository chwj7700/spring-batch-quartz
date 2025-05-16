package com.example.batch.listener;

import org.springframework.batch.core.JobExecution;

/**
 * 배치 작업 실행 리스너 인터페이스
 */
public interface JobExecutionListener extends org.springframework.batch.core.JobExecutionListener {

    /**
     * 작업 시작 전 호출
     */
    @Override
    void beforeJob(JobExecution jobExecution);

    /**
     * 작업 종료 후 호출
     */
    @Override
    void afterJob(JobExecution jobExecution);
} 