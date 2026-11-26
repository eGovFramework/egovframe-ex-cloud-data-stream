# 전자정부 표준프레임워크 실행환경 - 클라우드 데이터 스트림 예제

[![eGovFrame](https://img.shields.io/badge/eGovFrame-5.1.0-134F8C?labelColor=white&logo=data:image/svg%2Bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxNzMuMjgyIDE3My4yODIiPjxwYXRoIGZpbGw9IiNmZmYiIGQ9Ik0xNzMuMjgyIDg2LjY1YzAgNDcuODQ0LTM4Ljc5NCA4Ni42MzItODYuNjQ2IDg2LjYzMkMzOC43OTkgMTczLjI4MiAwIDEzNC40OTQgMCA4Ni42NSAwIDM4Ljc4OCAzOC43OTkgMCA4Ni42MzYgMGM0Ny44NTIgMCA4Ni42NDYgMzguNzg4IDg2LjY0NiA4Ni42NSIvPjxwYXRoIGZpbGw9IiMwMDM3NjQiIGQ9Ik0xMjcuMzg5IDgwLjU5OGMtMTMuNzkxLTkuMzY1LTMxLjQzOS01LjU0Mi00MC43MTcgOC41MzMtNy43MiAxMS43NjctMTkuNDA1IDEzLjIzNS0yMy45MDYgMTMuMjM1LTE0Ljc1MSAwLTI0LjgyNC0xMC4zNjctMjcuODE4LTIxLjA5NWgtLjAxYy0uMDM5LS4xMDgtLjA1OS0uMi0uMDktLjMwNy0uMDI1LS4xMi0uMDU2LS4yMzMtLjA4OS0uMzY2LTEuMTc3LTQuNDY3LTEuNDY3LTYuNjA5LTEuNDY3LTExLjM2OCAwLTI1LjY1IDI2LjMyMS01NC4yMTMgNjQuMjE1LTU0LjIxMyAzOC44MjkgMCA2MS4wNSAyOS41NDUgNjYuNzggNDUuOTc5LS4xMDctLjI5NS0uMjA5LS41ODEtLjI5MS0uODc3LTExLjAxNS0zMi4xMjUtNDEuNDc0LTU1LjIxNi03Ny4zNTctNTUuMjE2LTQ1LjEzIDAtODEuNzI5IDM2LjU5LTgxLjcyOSA4MS43MzkgMCA0MC4zNTEgMjkuMTA4IDc0Ljg5MSA2OS40NzkgNzQuODkxIDMyLjE5NyAwIDUzLjg0Mi0xOC4wNTIgNjMuNzU3LTQyLjkzIDUuNDUtMTMuNjE0IDEuNTk1LTI5LjYwNS0xMC43NTctMzguMDA1Ii8%2BPHBhdGggZmlsbD0iI2U0MDMyZSIgZD0iTTE2NC43ODggNjIuNTg5Yy00Ljc3Ny0xNi4wMjYtMjcuMTUzLTQ3LjU3MS02Ny4yODItNDcuNTcxLTM3Ljg5NCAwLTY0LjIxNCAyOC41NjMtNjQuMjE0IDU0LjIxMiAwIDQuNzU5LjI5IDYuOTAxIDEuNDY2IDExLjM2OC0uNDg5LTEuOTUxLS43NC0zLjkwOC0uNzQtNS44MjMgMC0yNi43MjEgMjYuNzQxLTQ1LjIyNyA1NC4yNDgtNDUuMjI3IDM3LjIxOCAwIDY3LjM4OCAzMC4xNzQgNjcuMzg4IDY3LjM5IDAgMjkuMTczLTE2Ljc4NSA1NC40MzctNDEuMTc5IDY2LjU2NXYuMDIzYzMxLjQ1NS0xMS4zOTEgNTMuOTA4LTQxLjUxMiA1My45MDgtNzYuODg0IDAtOC4zNzgtMS4xMjctMTUuNzU5LTMuNTk1LTI0LjA1MyIvPjwvc3ZnPg%3D%3D)](https://www.egovframe.go.kr)
[![Spring Cloud Stream](https://img.shields.io/badge/Spring%20Cloud%20Stream-4.3.3-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud-stream)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-4.0.1-231F20?logo=apachekafka&logoColor=white)](https://kafka.apache.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13.8-FF6600?logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.0.18-47A248?logo=mongodb&logoColor=white)](https://www.mongodb.com/)

---

## 프로젝트 구조

```
egovframe-ex-cloud-data-stream/
├── DOCKER/                          # Docker Compose 기반 인프라
│   ├── apache-kafka/               # Apache Kafka (KRaft)
│   ├── kafbat-ui/                  # Kafka 모니터링 UI
│   ├── mongodb/                    # MongoDB Community
│   ├── opensearch/                 # OpenSearch
│   └── rabbitmq/                   # RabbitMQ
├── egovframe-cloud-stream-test/    # 기본 송신 테스트 (포트 9003)
├── ex-egovframe-stream-pub-r2dbc/  # 스트림 프로듀서 (R2DBC, 포트 9001)
├── ex-egovframe-stream-sub-mongodb/ # 스트림 컨슈머 (MongoDB, 포트 9002)
└── IOT/                            # 온습도 센서 정보 전송 (Python)
```

---

## Data Stream 개요

### 정의

Spring Cloud Stream은 공유 메시징 시스템과 연결된 확장성이 뛰어난 이벤트 기반 마이크로서비스를 구축하기 위한 프레임워크이다.

**Spring Cloud Stream의 핵심 구성 요소:**
- **대상 바인더(Destination Binder)**: 외부 메시징 시스템과의 통합을 담당하는 구성 요소
- **대상 바인딩(Destination Binding)**: 외부 메시징 시스템과 애플리케이션 코드(생산자/소비자) 사이의 연결고리 제공
- **메시지(Message)**: 생산자와 소비자가 대상 바인더를 통해 통신하는 데 사용하는 표준 데이터 구조

Spring Integration의 메시징 처리 핵심 기능을 기반으로 사용하며, Spring Cloud Stream은 Spring Boot를 기반으로 Binder 연계를 제공하며 메시지 처리를 추상화하여 동일 환경의 분리된 애플리케이션 시스템 또는 다른 환경 간에도 연계 메시지 처리를 지원한다.

### 특징

**Data Stream**

비동기 데이터 처리는 지속적으로 발생하는 데이터를 실시간으로 처리하는 데 주요 목적이 있으며, 시간에 민감한 자료의 처리에 적합하고 다양한 지리적 위치에서 다양한 형식으로 전달할 수 있다.

**Data Stream vs Batch 비교**

![Data Stream vs Batch](doc/images/datastream_vs_batch.png)

| 구분 | 배치 처리 | 비동기 데이터 처리 |
|------|---------|----------------|
| **데이터 특성** | 한정된 대량의 데이터 | 지속적으로 데이터가 발생 |
| **처리 시점** | 스케줄링을 사용하여 특정 시간에 처리 | 데이터 발생 즉시 또는 일정한 간격 주기로 처리 가능 |
| **처리 방식** | 일괄로 정의된 유효단위 처리 | 데이터를 실시간으로 처리 |

### Data Stream 예제 5종

다양한 데이터 소스(IoT, 로그, 파일, DB, Open API)가 메시지 브로커(RabbitMQ, Apache Kafka)를 거쳐 Spring Cloud Stream·eGovFrame 실행환경 및 OpenSearch Architecture로 연동되는 서비스 메시 구성을 아래와 같이 정리할 수 있다.

![Data Stream 예제 5종 - Service mesh 구성도](doc/images/data_stream_example.png)

**연계 유형 요약 (실시간)**

| 순번 | 유형 | 발행(Publisher) | Message Broker | 구독(Subscriber) | 비고 |
|:----:|:-----|:----------------|:---------------|:-----------------|:-----|
| 1 | IOT | 온습도 센서 | RabbitMQ | Websocket(Chart.js) | |
| 2 | Log | Logback Appender | Apache Kafka | OpenSearch | Logstash로 자료 처리 및 연동 |
| 3 | File | Line 기반 파일 | Apache Kafka | Websocket | |
| 4 | DB | H2 DB | Apache Kafka | Websocket(Chart.js) | |
| 5 | Open API | 지하철 전동차 정보 | Apache Kafka | MongoDB | 구독 데이터 저장 |

---

## 인프라 구성 정보

| 오픈소스 명 | 버전 | 라이선스 | 비고 |
|-----------|------|---------|------|
| **RabbitMQ** | 3.13.8 | MPL 2.0 | 3.13.7 이하 취약점 존재 |
| **OpenSearch** | 2.15.0 | Apache 2.0 | |
| **OpenSearch Dashboard** | 2.15.0 | Apache 2.0 | |
| **Logstash OSS** (OpenSearch output 플러그인) | 7.16.2 | Apache 2.0 | |
| **Apache Kafka** | 4.0.1 | Apache 2.0 | KRaft 모드 (Zookeeper 미사용) |
| **Kafbat UI** | 1.4.2 | Apache 2.0 | ghcr.io/kafbat/kafka-ui |
| **MongoDB Community** | 8.0.18 | SSPL v1.0 | |

### 라이선스 주의사항
상용 제품으로 패키징하여 배포 또는 서비스 하는경우 다음을 주의한다.
#### MPL (Mozilla Public License) 2.0
- 사내에서만 사용(배포 없음) 하는 경우엔 추가 의무가 거의 없다
- 수정해서 배포(또는 실행 파일 형태로 외부 제공)하면, 수정된 MPL 적용 파일의 소스 제공 의무가 생길 수 있다
- 참고: https://www.mozilla.org/en-US/MPL/2.0/FAQ

#### SSPL (Server Side Public License) v1.0
- 프로그램(또는 수정본)의 기능을 제3자에게 '서비스로 제공'하면, 소스 공개 의무가 폭넓게 발생할 수 있으므로 주의가 필요하다
- 버전에 v가 공식적으로 붙어 있으며, 오픈소스 정의를 관리하는 기관인 OSI(Open Source Initiative)는 아직 SSPL을 오픈소스 라이선스로 승인하지 않았다

### 네트워크 구성
- **kafka-network**: Kafka ↔ Kafbat UI
- **egov-network**: OpenSearch, RabbitMQ, MongoDB 공통 인프라

### 환경 변수 설정

접속 정보는 환경 변수로 재정의하며, 지정하지 않으면 기본값을 사용한다. (설정 파일을 직접 수정할 필요 없음)

**애플리케이션 / IoT 스크립트**

| 환경 변수 | 기본값 | 사용처 |
|-----------|--------|--------|
| `KAFKA_BOOTSTRAP_SERVERS` | `192.168.100.60:9092` | pub·sub·test `application.yml`, pub `logback.xml`, Logstash |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `192.168.100.60` / `5672` | pub·sub, IoT 스크립트 |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `guest` / `guest` | pub·sub, IoT 스크립트 |
| `RABBITMQ_VHOST` | `egov` | pub·sub, IoT 스크립트 |
| `MONGODB_URI` / `MONGODB_DATABASE` | `mongodb://com:com01@localhost:27017/com` / `com` | sub |
| `WEBSOCKET_HOST` | `192.168.100.60` | sub (브라우저가 접속할 WebSocket 호스트) |
| `SERVER_PORT` | `9001` / `9002` / `9003` | pub / sub / test |
| `LINE_FILE_PATH` | `src/main/resources/file/txtData2.csv` | pub (발행 대상 파일) |
| `LOG_DIR` / `LOG_LEVEL` | `logs` / `INFO` | pub `logback.xml` (저장 위치, root 로그 레벨) |
| `TRAIN_API_URL` / `TRAIN_API_KEY` / `TRAIN_API_LINE` | `http://swopenAPI.seoul.go.kr` / `sample` / `1` | pub (지하철 Open API) |
| `H2_CONSOLE_PORT` | `9995` | pub |

**Docker Compose**

| 환경 변수 | 기본값 | 사용처 |
|-----------|--------|--------|
| `KAFKA_ADVERTISED_HOST` | `192.168.100.60` | `apache-kafka` (호스트에서 접속할 Kafka 주소) |
| `KAFKA_BOOTSTRAP_SERVERS` | `192.168.100.60:9092` | `opensearch` 의 Logstash 입력 |
| `OPENSEARCH_INITIAL_ADMIN_PASSWORD` | `Egov2015!` | `opensearch`, Logstash 출력 비밀번호 |
| `OPENSEARCH_HOSTS` / `OPENSEARCH_USER` | `https://opensearch:9200` / `admin` | Logstash 출력 |
| `MONGO_ROOT_USERNAME` / `MONGO_ROOT_PASSWORD` | `root` / 없음(필수) | `mongodb` |
| `MONGODB_DATABASE` / `MONGO_USERNAME` / `MONGO_PASSWORD` | `com` / `com` / `com01` | `mongodb` (애플리케이션 계정 자동 생성) |
| `RABBITMQ_DEFAULT_USER` / `RABBITMQ_DEFAULT_PASS` | `guest` / 없음(필수) | `rabbitmq` (계정 자동 생성) |

**예시 (로컬 단일 장비)**

```bash
export KAFKA_ADVERTISED_HOST=localhost
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export RABBITMQ_HOST=localhost
export WEBSOCKET_HOST=localhost
```

Docker Compose 는 각 compose 디렉터리의 `.env` 파일에 동일한 변수를 선언해도 된다.

**필수 비밀번호 (`mongodb`, `rabbitmq`)**

MongoDB root 비밀번호와 RabbitMQ 계정 비밀번호는 기본값이 없으며, 지정하지 않으면 컨테이너가 기동되지 않는다. 각 디렉터리의 `.env.example` 을 `.env` 로 복사한 뒤 값을 변경한다. (`.env` 는 git 에 커밋하지 않는다)

```bash
cp DOCKER/mongodb/.env.example DOCKER/mongodb/.env
cp DOCKER/rabbitmq/.env.example DOCKER/rabbitmq/.env
```

애플리케이션과 IoT 스크립트에는 같은 값을 `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD`, `MONGODB_URI` 로 지정한다.

---

## Docker 인프라 기동

### 1. 네트워크 생성 서비스 먼저 기동

```bash
# Kafka 네트워크 생성
cd DOCKER/apache-kafka
docker compose up -d

# Egov 네트워크 생성
cd DOCKER/opensearch
docker compose up -d
```

### 2. 외부 네트워크 사용 서비스 기동

```bash
# Kafka UI (kafka-network 사용)
cd DOCKER/kafbat-ui
docker compose up -d

# RabbitMQ (egov-network 사용)
cd DOCKER/rabbitmq
docker compose up -d

# MongoDB (egov-network 사용)
cd DOCKER/mongodb
docker compose up -d
```

### 3. 초기 데이터 자동 구성

컨테이너 최초 기동 시 예제에 필요한 계정과 메시징 자원이 자동으로 생성된다.

| 대상 | 자동 생성 내용 | 정의 파일 |
|------|----------------|-----------|
| RabbitMQ | 계정(`RABBITMQ_DEFAULT_USER` / `RABBITMQ_DEFAULT_PASS`)과 vhost `egov` 권한 | `DOCKER/rabbitmq/init-account.sh` |
| RabbitMQ | vhost `egov`, exchange `sensor`(direct), 큐 `sensor.q_temp_humi`, 바인딩(routing key `q_temp_humi`) | `DOCKER/rabbitmq/definitions.json` |
| MongoDB | DB `com`, 계정 `MONGO_USERNAME` / `MONGO_PASSWORD`(dbOwner) | `DOCKER/mongodb/init-user.js` |

- RabbitMQ 계정은 `.env` 의 `RABBITMQ_DEFAULT_USER` / `RABBITMQ_DEFAULT_PASS` 로 지정하고, 애플리케이션에는 `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` 를 동일하게 지정한다. exchange·큐를 바꾸려면 `definitions.json` 을 수정한다.
- MongoDB 초기화 스크립트는 데이터가 없는 최초 기동 시에만 실행된다. 기존 컨테이너에는 `DOCKER/mongodb/readme.txt` 의 수동 절차를 따른다.
- 기존 RabbitMQ 를 사용하는 경우 아래와 같이 직접 생성한다. (sub 모듈은 큐가 없으면 선언 재시도로 기동이 지연됨)

```bash
rabbitmqctl add_vhost egov
rabbitmqctl set_permissions -p egov guest ".*" ".*" ".*"
rabbitmqadmin -V egov declare exchange name=sensor type=direct
rabbitmqadmin -V egov declare queue name=sensor.q_temp_humi durable=true
rabbitmqadmin -V egov declare binding source=sensor destination=sensor.q_temp_humi routing_key=q_temp_humi
```

### 4. 서비스 접속 정보

| 서비스 | 포트 | 접속 URL | 비고 |
|--------|------|----------|------|
| Kafka | 9092 | `192.168.100.60:9092` | 브로커 접속 |
| Kafbat UI | 9980 | http://localhost:9980 | Kafka 모니터링 |
| RabbitMQ | 5672, 15672 | http://localhost:15672 | Management UI, 계정은 `.env` 지정값 |
| MongoDB | 27017 | `192.168.100.60:27017` | root / `.env` 지정값 |
| OpenSearch | 9200 | https://localhost:9200 | admin/Egov2015! |
| OpenSearch Dashboards | 5601 | http://localhost:5601 | 대시보드 |
| Logstash | - | - | Kafka `egov-logs` 토픽 구독 |

**⚠️ 주의**: 기본 설정 비번은 반드시 변경한 후 사용한다.

---

## 주요 설정 변경 사항

### 1. Apache Kafka (KRaft 모드)

**변경 내용:**
- Zookeeper 제거, KRaft 전용 모드로 설정
- 호스트 접속을 위해 `ADVERTISED_LISTENERS` IP 지정

```yaml
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://${KAFKA_ADVERTISED_HOST:-192.168.100.60}:9092
```

**주의사항:**
- 호스트 IP가 다르면 `KAFKA_ADVERTISED_HOST` 환경 변수를 지정
- 컨테이너 재시작 필요: `docker compose down && docker compose up -d`

### 2. OpenSearch 보안 설정

**초기 Admin 비밀번호:**
```yaml
OPENSEARCH_INITIAL_ADMIN_PASSWORD: ${OPENSEARCH_INITIAL_ADMIN_PASSWORD:-Egov2015!}
```

OpenSearch 2.12+ 버전부터 보안 플러그인 초기화 시 필수 설정

### 3. 네트워크 구조 통일

**변경 전 (deprecated):**
```yaml
networks:
  default:
    external:
      name: egov-network
```

**변경 후:**
```yaml
services:
  service-name:
    networks:
      - egov-network

networks:
  egov-network:
    name: egov-network
    external: true
```

### 4. Spring Cloud Function Definition 속성 경로 (Spring Cloud Stream 4.x)

**변경 전 (Spring Cloud Stream 3.x에서 허용):**
```yaml
spring:
  cloud:
    stream:
      function:
        definition: basicProducer   # spring.cloud.stream.function.definition
```

**변경 후 (Spring Cloud Stream 4.x에서 필수):**
```yaml
spring:
  cloud:
    function:
      definition: basicProducer     # spring.cloud.function.definition
    stream:
      # ...
```

- `spring.cloud.function.definition`은 Spring Cloud Function 프로젝트 소속 속성
- Spring Cloud Stream 3.x에서는 `spring.cloud.stream.function.definition`도 내부적으로 인식했으나, 4.x에서는 인식하지 않음
- YAML 들여쓰기 한 레벨 차이(`stream` 하위 vs `cloud` 하위)로 완전히 다른 속성 경로가 되므로 주의
- 설정이 잘못되면 `Multiple functional beans were found... can't determine default function definition` 경고 발생 후 Supplier/Consumer가 바인딩되지 않음

### 5. Docker Compose version 필드 제거

Docker Compose V2에서는 `version` 필드가 더 이상 필요하지 않으므로 모든 compose 파일에서 제거

---

## 애플리케이션 설정

### Spring Cloud Stream Kafka 설정

**공통 설정 (`application.yml`):**

```yaml
spring:
  cloud:
    stream:
      kafka:
        binder:
          autoCreateTopics: false
          autoAddPartitions: false
          brokers: ${KAFKA_BOOTSTRAP_SERVERS:192.168.100.60:9092}
          # 클라이언트 텔레메트리 전송 비활성화
          configuration:
            enable.metrics.push: false
```

**주요 포인트:**
- `zkNodes` 제거: Kafka 4.x KRaft 모드에서는 Zookeeper 미사용
- `enable.metrics.push: false`: 텔레메트리 API 불일치 경고 방지
- `brokers`: `KAFKA_BOOTSTRAP_SERVERS` 환경 변수로 지정

### Supplier 호출 주기 (poller)

```yaml
spring:
  cloud:
    stream:
      poller:
        fixed-delay: 1000        # 전체 Supplier 기본 주기(ms)
      bindings:
        trainApiProducer-out-0:
          producer:
            poller:
              fixed-delay: 5000  # 바인딩별 주기(ms)
```

### OpenFeign 설정 (Spring Cloud OpenFeign 4.x)

`feign.client.config.*` 는 4.x 에서 인식되지 않으므로 `spring.cloud.openfeign.client.config.*` 를 사용한다.

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connect-timeout: 5000
            read-timeout: 2000
            logger-level: full
```

### 실행 위치 기준 경로

`LINE_FILE_PATH`(발행 대상 파일), `LOG_DIR`(로그 저장 위치)의 기본값은 실행 위치(모듈 디렉터리) 기준 상대경로이며, 필요 시 환경 변수에 절대경로를 지정한다.

---

## 애플리케이션 빌드 및 기동

각 모듈 디렉터리에서 실행한다.

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

| 모듈 | 포트 | 주요 URL |
|------|------|----------|
| ex-egovframe-stream-pub-r2dbc | 9001 | http://localhost:9001/ (샘플 목록), http://localhost:9001/swagger-ui.html, H2 콘솔 9995 |
| ex-egovframe-stream-sub-mongodb | 9002 | http://localhost:9002/sensor, /file_line, /db, /echo |
| egovframe-cloud-stream-test | 9003 | `test-topic` 으로 1초 주기 발행 |

---

## 버전 호환성

### 표준프레임워크 5.1.0 구성 버전

| 구성 요소 | 버전 |
|-----------|------|
| egovframe-boot-starter-parent | 5.1.0 |
| Spring Boot | 3.5.16 |
| Spring Framework | 6.2.19 (egov 빌드) |
| Spring Cloud | 2025.0.3 |
| Spring Cloud Stream / OpenFeign | 4.3.3 |
| Spring for Apache Kafka / kafka-clients | 3.3.16 / 3.9.2 |
| Spring AMQP | 3.2.12 |
| MongoDB Reactive Streams Driver | 5.13.0 |
| springdoc-openapi | 2.8.17 |
| Lombok | 1.18.48 |

부모 POM 이 버전을 관리하므로 각 모듈에서는 springdoc, Lombok annotationProcessor 버전을 별도로 선언하지 않는다.

### Kafka 클라이언트 ↔ 브로커

- **클라이언트**: kafka-clients 3.9.2 (Spring Boot 3.5.16 관리 버전)
- **브로커**: Apache Kafka 4.0.1
- **호환성**: 정상 동작 (Kafka는 하위 호환성 우수)
- **텔레메트리 경고**: `enable.metrics.push: false`로 해결

### OpenSearch ↔ Logstash

- **OpenSearch**: 2.15.0
- **Logstash**: 7.16.2 (logstash-output-opensearch 플러그인 2.0+)
- **호환성**: 플러그인 2.0+가 OpenSearch 2.x 지원

---

## 트러블슈팅

### 1. Kafka 연결 실패: `UnknownHostException: kafka`

**원인:** 호스트(Docker 밖)에서 앱 실행 시 `kafka` 호스트명 해석 불가

**해결:**
- `KAFKA_ADVERTISED_HOST`, `KAFKA_BOOTSTRAP_SERVERS` 환경 변수를 실제 IP로 지정

### 2. OpenSearch 컨테이너 재시작 반복

**원인:** `OPENSEARCH_INITIAL_ADMIN_PASSWORD` 미설정

**해결:**
```yaml
environment:
  - OPENSEARCH_INITIAL_ADMIN_PASSWORD=Egov2015!
```

---

## 개발 환경

### 필수 요구사항
- 표준프레임워크 v5.1.0 (Spring Boot 3.5.16)
- Spring Cloud 2025.0.3 (Spring Cloud Stream v4.3.3)
- Docker Desktop 4.37.1+ (운영 기반으로 대체 가능)
- Java 17+
- Maven 3.9+
- Python 3.9.2+
- Raspberry Pi 1 Model B 이후 출시모델 (512MB RAM) + IOT(온습도센서)

### 테스트 환경
- macOS / Linux
- 최소 8GB RAM (Docker에 4GB 이상 할당)
- 여유 디스크 공간 20GB+

---

## 위키 가이드 문서
- [Cloud Data Stream 환경설정](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte2:rex:clouddatastream)
- [Data Stream Publish 예제](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte4:rex:publish_data_stream)
- [Data Stream Subscribe 예제](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte4:rex:subscribe_data_stream)

## 참고 문서

### 오픈소스 공식 문서
- [Apache Kafka 4.0 Documentation](https://kafka.apache.org/40/documentation.html)
- [Spring Cloud Stream Kafka Binder](https://spring.io/projects/spring-cloud-stream)
- [OpenSearch Documentation](https://opensearch.org/docs/latest/)
- [Kafbat UI Documentation](https://ui.docs.kafbat.io/)
- [MongoDB Documentation](https://www.mongodb.com/docs/manual/)

