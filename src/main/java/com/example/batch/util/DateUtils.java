package com.example.batch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 날짜 관련 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DEFAULT_DATETIME_FORMAT_WITH_MILLIS = "yyyyMMddHHmmssSSS";
    public static final String LOG_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOG_DATETIME_FORMAT_WITH_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /**
     * 현재 날짜를 기본 포맷(yyyyMMdd)의 문자열로 반환
     */
    public static String getCurrentDateAsString() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }
    
    /**
     * 현재 날짜와 시간을 기본 포맷(yyyyMMddHHmmss)의 문자열로 반환
     */
    public static String getCurrentDateTimeAsString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }
    
    /**
     * LocalDate를 Date로 변환
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * LocalDateTime을 Date로 변환
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Date를 LocalDate로 변환
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    /**
     * Date를 LocalDateTime으로 변환
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * 지정된 날짜의 시작 시간을 반환
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date.atTime(LocalTime.MIN);
    }
    
    /**
     * 지정된 날짜의 종료 시간을 반환
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }
} 