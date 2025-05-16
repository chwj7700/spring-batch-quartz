package com.example.batch.job.sampleChunk.step;

import com.example.batch.job.sampleChunk.model.SampleItem;
import com.example.batch.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 샘플 데이터 ItemReader 구현
 * 데이터베이스에서 데이터를 읽어와 SampleItem 객체로 변환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleChunkItemReader implements ItemReader<SampleItem> {

    private final SampleMapper sampleMapper;
    private List<SampleItem> items;
    private int currentIndex = 0;

    /**
     * 데이터를 한 번에 읽어와서 큐에 저장한 뒤 하나씩 반환
     */
    @Override
    public SampleItem read() {
        if (items == null) {
            // 최초 호출 시 데이터를 모두 조회
            log.info("Reading sample data from database");
            items = fetchDataFromDatabase();
            currentIndex = 0;
            log.info("Retrieved {} items from database", items.size());
        }

        // 큐에서 아이템 하나를 반환
        SampleItem nextItem = null;

        if (currentIndex < items.size()) {
            nextItem = items.get(currentIndex);
            currentIndex++;
            log.debug("Reading item: {}", nextItem);
        } else {
            // 모든 데이터를 다 읽었으면 null을 반환하여 읽기 종료
            log.info("No more items to read, reading phase complete");
            // 다음 Job 실행을 위한 초기화
            items = null;
            currentIndex = 0;
        }

        return nextItem;
    }

    /**
     * 데이터베이스에서 데이터를 조회하여 SampleItem 리스트로 변환
     */
    private List<SampleItem> fetchDataFromDatabase() {
        List<Map<String, Object>> dataList = sampleMapper.selectSampleData();
        if (dataList == null || dataList.isEmpty()) {
            return Collections.emptyList();
        }

        return dataList.stream()
                .map(SampleItem::fromMap)
                .collect(Collectors.toList());
    }
} 