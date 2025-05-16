package com.example.batch.exception;

import lombok.Getter;

/**
 * 배치 작업 관련 예외 클래스
 */
@Getter
public class BatchException extends RuntimeException {

    private final String errorCode;
    private final String jobName;
    private final String stepName;

    /**
     * 배치 작업 예외 생성
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    public BatchException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.jobName = null;
        this.stepName = null;
    }

    /**
     * 배치 작업 예외 생성
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    public BatchException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.jobName = null;
        this.stepName = null;
    }

    /**
     * 배치 작업 예외 생성
     * @param errorCode 에러 코드
     * @param jobName 작업 이름
     * @param stepName 스텝 이름
     * @param message 에러 메시지
     */
    public BatchException(String errorCode, String jobName, String stepName, String message) {
        super(message);
        this.errorCode = errorCode;
        this.jobName = jobName;
        this.stepName = stepName;
    }

    /**
     * 배치 작업 예외 생성
     * @param errorCode 에러 코드
     * @param jobName 작업 이름
     * @param stepName 스텝 이름
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    public BatchException(String errorCode, String jobName, String stepName, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.jobName = jobName;
        this.stepName = stepName;
    }
} 