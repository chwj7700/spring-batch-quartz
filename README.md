# Spring Batch with Quartz Scheduler

JDK8 기반의 Spring Batch와 Quartz Scheduler를 이용한 배치 작업 프로젝트입니다.

## 기술 스택
* Java 8
* Spring Boot 2.1.7.RELEASE
* Spring Batch
* Quartz Scheduler
* MyBatis
* Oracle Database
* Log4j2

## 프로젝트 구조
```
src/main/java/com/example/batch/
├── common/              # 공통 유틸리티 클래스
├── config/              # 설정 클래스
│   ├── BatchConfig.java       # 배치 설정
│   ├── ListenerConfig.java    # 리스너 설정
│   └── LoggingConfig.java     # 로깅 설정
├── domain/              # 도메인 모델 클래스
├── exception/           # 예외 클래스
├── job/                 # 배치 작업 관련 클래스
│   ├── definition/      # 작업 정의
│   ├── listener/        # 작업 리스너
│   ├── param/           # 작업 파라미터
│   └── sampleBatch/     # 샘플 배치 작업
│       ├── model/       # 데이터 모델
│       └── step/        # 작업 스텝
│           ├── chunk/   # 청크 기반 처리 컴포넌트
│           └── tasklet/ # 태스클릿 기반 처리 컴포넌트
├── mapper/              # MyBatis 매퍼
├── quartz/              # Quartz 작업
├── service/             # 비즈니스 서비스
└── util/                # 유틸리티 클래스

src/main/resources/
├── config/              # 설정 파일
│   ├── local/           # 로컬 환경 설정
│   ├── dev/             # 개발 환경 설정
│   └── prod/            # 운영 환경 설정
├── mappers/             # MyBatis 매퍼 XML 파일
├── schema/              # 스키마 정의 파일
├── sql/                 # SQL 스크립트
└── log4j2.xml           # Log4j2 설정
```

## 특징
* 환경별 설정 분리 (로컬, 개발, 운영)
* Oracle 데이터베이스 사용
* Spring Batch 메타데이터 저장소를 사용하지 않음 (MapJobRepository 사용)
* Quartz 메타데이터 테이블을 사용하지 않음 (RAMJobStore 사용)
* Log4j2를 이용한 체계적인 로깅 설정
* 유연한 배치 작업 구조 (AbstractBatchJob 상속)
* 청크 기반 배치 처리 지원 (ItemReader, ItemProcessor, ItemWriter)
* 태스클릿 기반 배치 처리 지원
* MDC를 활용한 로그 추적

## 설정 방식
* application.properties 기반 설정 사용
* 데이터소스, Quartz 스케줄러 등 Spring Boot 자동 구성 활용
* 명시적인 Java Config 최소화

## 배치 처리 방식
### 1. 청크(Chunk) 기반 처리
* ItemReader: 데이터 소스에서 아이템 읽기
* ItemProcessor: 아이템 처리 및 변환
* ItemWriter: 처리된 아이템 저장
* 트랜잭션 내에서 일정 단위(청크)로 처리
* 대용량 데이터 처리에 적합

### 2. 태스클릿(Tasklet) 기반 처리
* 단일 트랜잭션 내에서 모든 로직 처리
* 간단한 작업이나 전체 데이터를 한 번에 처리해야 하는 경우 적합

## 로깅 설정
* Log4j2를 사용하여 로그 관리
* 로그 파일은 `logs` 디렉토리에 생성됨
* 애플리케이션 로그: `application.log`
* 배치 작업 로그: `batch.log`
* 에러 로그: `error.log`
* JSON 형식 로그 (ELK 연동용): `json/application.json`
* MDC를 활용한 배치 작업 정보 추적

## 환경 설정
* 환경별 속성 파일 분리
    * `application.properties`: 공통 설정
    * `application-local.properties`: 로컬 환경 설정
    * `application-dev.properties`: 개발 환경 설정
    * `application-prod.properties`: 운영 환경 설정
* 환경 전환 방법:
    ```
    java -jar spring-batch-quartz.jar --spring.profiles.active=prod
    ```

## 데이터베이스 설정
* Oracle 데이터베이스 설정은 환경별 속성 파일에서 변경
* HikariCP 커넥션 풀 사용
* 기본 설정 (로컬):
    ```properties
    spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
    spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
    spring.datasource.username=system
    spring.datasource.password=password
    spring.datasource.hikari.pool-name=BatchHikariPool
    spring.datasource.hikari.maximum-pool-size=10
    ```

## 실행 방법
### Maven 사용
```shell
# 로컬 환경에서 실행
mvn spring-boot:run

# 특정 환경에서 실행
mvn spring-boot:run -Dspring.profiles.active=dev

# 특정 배치 작업만 실행
mvn spring-boot:run -Dspring.batch.job.names=sampleBatchJob
```

### Jar 파일 실행
```shell
mvn clean package
java -jar target/spring-batch-quartz-0.0.1-SNAPSHOT.jar
```

### 운영 환경 실행 예시
```shell
java -jar spring-batch-quartz-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.username=produser \
  --spring.datasource.password=P@ssw0rd \
  --logging.level.root=WARN
```

## 배치 작업 확장
1. `AbstractBatchJob`을 상속하는 클래스 생성
2. `getJobName()` 및 `executeJob()` 메소드 구현
3. 필요한 경우 커스텀 `JobExecutionListener` 구현

예시:
```java
@Configuration
@RequiredArgsConstructor
public class CustomBatchJob extends AbstractBatchJob {
    @Override
    public String getJobName() {
        return "customBatchJob";
    }
    
    @Override
    protected JobExecutionListener jobExecutionListener() {
        return new DefaultJobExecutionListener();
    }
    
    @Override
    protected void executeJob(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 비즈니스 로직 구현
    }
}
```

## 청크 기반 배치 작업 구현
```java
// 청크 기반 배치 스텝 정의
@Configuration
public class SampleChunkStep {
    @Bean
    public Step sampleChunkStep() {
        return stepBuilderFactory.get("sampleChunkStep")
                .<SampleItem, SampleItem>chunk(10) // 청크 크기 10으로 설정
                .reader(sampleItemReader)
                .processor(sampleItemProcessor)
                .writer(sampleItemWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
```

## Quartz 스케줄링
* 기본 설정으로 메모리 기반 작업 저장소 사용 (RAMJobStore)
* application.properties에서 Quartz 설정 관리
* 스케줄 정의: `sampleJobTrigger()` 메소드

## 테이블 생성 스크립트
* `src/main/resources/sql/create_sample_table.sql`

## 주요 기능
* Spring Batch를 이용한 배치 작업 설정
* Quartz Scheduler를 이용한 배치 작업 스케줄링
* MyBatis를 이용한 데이터 접근
* Oracle 데이터베이스 연동
* 청크 기반 및 태스클릿 기반 배치 처리 지원

## 배치 작업 흐름
1. Quartz Scheduler에 의해 스케줄된 시간에 Job 실행
2. Quartz Job이 Spring Batch Job 호출
3. Spring Batch Job이 Step 실행
4. Step에서 Tasklet 또는 Chunk 기반 처리 수행
5. 작업 결과는 로그로 출력 