package com.example.batch.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 배치 작업과 관련된 상수 정의 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BatchConstants {

    /**
     * 작업 파라미터
     */
    public static final class JobParameters {
        public static final String RUN_ID = "run_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String SCHEDULE_DATE = "schedule_date";
        public static final String JOB_NAME = "job_name";
    }

    /**
     * 작업 실행 상태
     */
    public static final class JobStatus {
        public static final String COMPLETED = "COMPLETED";
        public static final String FAILED = "FAILED";
        public static final String RUNNING = "RUNNING";
        public static final String STOPPED = "STOPPED";
        public static final String STARTING = "STARTING";
    }

    /**
     * 작업 컨텍스트 키
     */
    public static final class JobContext {
        public static final String READ_COUNT = "READ_COUNT";
        public static final String WRITE_COUNT = "WRITE_COUNT";
        public static final String FILTER_COUNT = "FILTER_COUNT";
        public static final String ERROR_COUNT = "ERROR_COUNT";
    }

    /**
     * 로그 MDC 키
     */
    public static final class LogMdcKeys {
        public static final String JOB_ID = "JOB_ID";
        public static final String JOB_NAME = "JOB_NAME";
        public static final String STEP_ID = "STEP_ID";
        public static final String QUARTZ_JOB_KEY = "QUARTZ_JOB_KEY";
        public static final String QUARTZ_TRIGGER = "QUARTZ_TRIGGER";
    }
} 