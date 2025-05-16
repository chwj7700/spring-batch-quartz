package com.example.batch.job.sampleChunk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 샘플 데이터 모델 클래스
 * DB에서 읽고 처리할 데이터 항목을 표현
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleItem {
    private Long id;
    private String name;
    private String value;
    private String status;

    /**
     * Map 데이터를 SampleItem으로 변환
     */
    public static SampleItem fromMap(Map<String, Object> map) {
        return SampleItem.builder()
                .id(getLongValue(map, "ID"))
                .name(getStringValue(map, "NAME"))
                .value(getStringValue(map, "VALUE"))
                .status(getStringValue(map, "STATUS"))
                .build();
    }

    private static Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? Long.valueOf(value.toString()) : null;
    }

    private static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
} 