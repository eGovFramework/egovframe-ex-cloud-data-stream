# OpenSearch Docker Compose

- https://opensearch.org/docs/latest/
- https://opensearch.org/downloads.html
- https://github.com/opensearch-project/OpenSearch
- https://github.com/opensearch-project/OpenSearch-Dashboards
- https://opensearch.org/docs/latest/clients/logstash/index/

## docker-compose.yml 확인

elasticsearch 를 대체하는 opensearch, kibana 를 대체하는 opensearch-dashboards 그리고, logstash-oss-with-opensearch-output-plugin 의 도커 파일들을 하나로 묶어준다.

- OpenSearch: Data store and search engine
- OpenSearch Dashboards: Search frontend and visualizations
- logstash-oss-with-opensearch-output-plugin: real-time event processing engine

## 구동 (OpenSearch, OpenSearch Dashboards, Logstash)

```
docker-compose up -d
```

## Logstash 확인

logstash 폴더의 config/logstash.yml 과 pipeline/logstash.conf 환경설정으로 구동된다.
logstash의 pipeline은 input, filter, output으로 구성된다.

- input: 마이크로서비스들로부터 log 이벤트들을 받는다
- filter: input으로 받은 이벤트들을 output으로 전송하기 전에 원하는 형태로 변형한다.
- output: OpenSearch 로 filtered 이벤트들을 전송한다.

logstash.conf 파일에 다음과 같이 input, filter, output pipeline을 정의한다.
Kafka 의 "egov-logs" 토픽을 구독하고 있다가, 이벤트를 수신하면 grok 으로 로그 문자열을 필드로 분해한 뒤, stdout으로 그대로 출력함과 동시에, opensearch로 "egov-logs-%{+YYYY.MM.dd}" 를 인덱스 패턴으로 하여 전송한다.

```
# logstash.conf 파일

input {
  kafka {
    bootstrap_servers => "192.168.100.50:9092"
    group_id => "logstash"
    topics => ["egov-logs"]
    consumer_threads => 1
    decorate_events => true
  }

}

filter {
  grok {
    match => {
	  "message" => "\[%{TIMESTAMP_ISO8601:timestamp}] \[%{DATA:thread}] %{LOGLEVEL:logLevel} %{JAVACLASS:packageClass} - %{GREEDYDATA:msg}"
    }
  }
}

output {

  stdout {}

  opensearch {
    hosts => ["https://192.168.100.50:9200"]
    index => "egov-logs-%{+YYYY.MM.dd}"
    user => "admin"
    password => "admin"
    ssl => true
    ssl_certificate_verification => false
  }

}
```

Kafka 토픽을 구독하고 있는지 컨테이너 로그로 확인한다.

```
docker logs openlogstash | grep "Subscribed to topic"

[2026-09-09T20:28:58,156][INFO ][org.apache.kafka.clients.consumer.KafkaConsumer][main] [Consumer clientId=logstash-0, groupId=logstash] Subscribed to topic(s): egov-logs
```

최종 30 라인을 실시간 로그를 확인할 수 있다.

```
docker logs --tail 30 -f openlogstash
```

수신한 이벤트는 output 에 지정한 인덱스로 색인된다.

```
curl -k -u admin:'Egov2015!' "https://localhost:9200/_cat/indices/egov-logs-*?v"
```

## OpenSearch Dashboards 확인

브라우저에서 다음 URL로 이동하여 admin / Egov2015! 로 접속한다.

```
http://localhost:5601/
```

- OpenSearch Dashboards > Discover 에서 확인할 수 있다.

## logstash.conf 변경 적용

logstash.conf를 변경한 후 적용하려면

```
docker stop openlogstash
docker start openlogstash

# 또는 restart
docker restart openlogstash
```

## logstash 컨테이너 터미널 접속

```
docker exec -it openlogstash /bin/bash 
```