package com.example.batch.job.sampleChunk.step;

import com.example.batch.job.sampleChunk.model.SampleItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 샘플 데이터 ItemWriter 구현
 * 처리된 데이터를 저장하거나 다른 시스템으로 전송
 */
@Slf4j
@Component
public class SampleChunkItemWriter implements ItemWriter<SampleItem> {

    @Override
    public void write(List<? extends SampleItem> items) throws Exception {
        if (items.isEmpty()) {
            log.info("No items to write");
            return;
        }

        log.info("Writing {} items", items.size());

        for (SampleItem item : items) {
            log.debug("Writing item: {}", item);
            // 실제로는 여기서 데이터베이스에 저장하거나 외부 시스템으로 전송하는 로직 구현
            // 예: itemRepository.save(item) 또는 externalService.send(item)
        }

        log.info("Successfully wrote {} items", items.size());
    }
} 