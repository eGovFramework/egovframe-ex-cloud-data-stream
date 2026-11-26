#!/bin/sh
# 기동 시 환경 변수의 계정으로 계정 정의 파일을 생성한 뒤 RabbitMQ 를 기동한다.
# (definitions 를 사용하면 RABBITMQ_DEFAULT_USER/PASS 가 자동 반영되지 않으므로 직접 생성)
set -eu

: "${RABBITMQ_DEFAULT_PASS:?RABBITMQ_DEFAULT_PASS is required. Copy .env.example to .env and set it.}"

# JSON 문자열 이스케이프 (\ 와 ")
esc() { printf '%s' "$1" | sed -e 's/\\/\\\\/g' -e 's/"/\\"/g'; }

user=$(esc "${RABBITMQ_DEFAULT_USER:-guest}")
pass=$(esc "$RABBITMQ_DEFAULT_PASS")
vhost=$(esc "${RABBITMQ_DEFAULT_VHOST:-egov}")

umask 077
cat > /etc/rabbitmq/definitions.d/10-account.json <<JSON
{
  "vhosts": [
    { "name": "$vhost" }
  ],
  "users": [
    { "name": "$user", "password": "$pass", "tags": ["administrator"] }
  ],
  "permissions": [
    { "user": "$user", "vhost": "$vhost", "configure": ".*", "write": ".*", "read": ".*" }
  ]
}
JSON
chown rabbitmq:rabbitmq /etc/rabbitmq/definitions.d/10-account.json

exec docker-entrypoint.sh rabbitmq-server
