package com.example.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@EnableBatchProcessing
@MapperScan("com.example.batch.mapper")
@RequiredArgsConstructor
public class BatchConfig implements BatchConfigurer {

    private final PlatformTransactionManager transactionManager;

    private JobRepository jobRepository;
    private JobExplorer jobExplorer;
    private JobLauncher jobLauncher;

    @Value("${batch.max-threads:5}")
    private int maxThreads;

    @Value("${batch.enabled:true}")
    private boolean batchEnabled;

    @PostConstruct
    public void initialize() {
        log.info("Initializing BatchConfig with maxThreads={}, batchEnabled={}", maxThreads, batchEnabled);
    }

    @Bean
    @Qualifier("batchTaskExecutor")
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(maxThreads);
        executor.setMaxPoolSize(maxThreads);
        executor.setQueueCapacity(maxThreads * 2);
        executor.setThreadNamePrefix("BatchExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    @Bean
    @Qualifier("asyncTaskExecutor")
    public TaskExecutor asyncTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("AsyncExecutor-");
        executor.setConcurrencyLimit(maxThreads * 2);
        return executor;
    }

    @Override
    public JobRepository getJobRepository() throws BatchConfigurationException {
        try {
            if (jobRepository == null) {
                MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
                factory.setTransactionManager(getTransactionManager());
                factory.afterPropertiesSet();
                jobRepository = factory.getObject();
                log.info("Created in-memory JobRepository: {}", jobRepository);
            }
            return jobRepository;
        } catch (Exception e) {
            log.error("Failed to initialize JobRepository {0}", e);
            throw new BatchConfigurationException(e);
        }
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws BatchConfigurationException {
        try {
            if (jobLauncher == null) {
                SimpleJobLauncher launcher = new SimpleJobLauncher();
                launcher.setJobRepository(getJobRepository());
                launcher.setTaskExecutor(batchTaskExecutor());
                launcher.afterPropertiesSet();
                jobLauncher = launcher;
                log.info("Created JobLauncher: {}", jobLauncher);
            }
            return jobLauncher;
        } catch (Exception e) {
            log.error("Failed to initialize JobLauncher {0}", e);
            throw new BatchConfigurationException(e);
        }
    }

    @Override
    public JobExplorer getJobExplorer() throws BatchConfigurationException {
        try {
            if (jobExplorer == null) {
                MapJobExplorerFactoryBean factoryBean = new MapJobExplorerFactoryBean(new MapJobRepositoryFactoryBean());
                factoryBean.afterPropertiesSet();
                jobExplorer = factoryBean.getObject();
                log.info("Created JobExplorer: {}", jobExplorer);
            }
            return jobExplorer;
        } catch (Exception e) {
            log.error("Failed to initialize JobExplorer {0}", e);
            throw new BatchConfigurationException(e);
        }
    }
} 