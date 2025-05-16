package com.example.batch.job.sampleChunk.step;

import com.example.batch.job.sampleChunk.model.SampleItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 샘플 데이터 ItemProcessor 구현
 * 데이터를 필터링하거나 변환하는 역할
 */
@Slf4j
@Component
public class SampleChunkItemProcessor implements ItemProcessor<SampleItem, SampleItem> {

    @Override
    public SampleItem process(SampleItem item) throws Exception {
        if (item == null) {
            return null;
        }

        log.debug("Processing item: {}", item);

        // 상태가 '유효함'인 아이템만 처리 (필터링)
        if ("ACTIVE".equalsIgnoreCase(item.getStatus())) {
            // 데이터 변환 처리
            SampleItem processedItem = SampleItem.builder()
                    .id(item.getId())
                    .name(item.getName() != null ? item.getName().toUpperCase() : null)  // 이름 대문자로 변환
                    .value(item.getValue())
                    .status(item.getStatus())
                    .build();

            log.debug("Item processed: {}", processedItem);
            return processedItem;
        } else {
            log.debug("Item filtered out: {}", item);
            return null;  // null 반환하면 해당 아이템은 Writer로 전달되지 않음
        }
    }
} 