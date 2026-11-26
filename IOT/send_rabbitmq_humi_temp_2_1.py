#!/usr/bin/python3 

import os

import Adafruit_DHT  # 온습도 센서 라이브러리
import pika          # AMQP 클라이언트

class Publisher:
    def __init__(self):
        # 접속 정보는 환경 변수로 재정의
        self.__url = os.environ.get('RABBITMQ_HOST', '192.168.100.60')
        self.__port = int(os.environ.get('RABBITMQ_PORT', '5672'))
        self.__vhost = os.environ.get('RABBITMQ_VHOST', 'egov')
        self.__cred = pika.PlainCredentials(os.environ.get('RABBITMQ_USERNAME', 'guest'),
                                            os.environ.get('RABBITMQ_PASSWORD', 'guest'))
        self.__exchange = 'sensor';
        self.__queue = 'q_temp_humi';
        return

    def main(self):
    
        sensor = Adafruit_DHT.DHT22
        pin = 4  # Data 핀의 GPIO 번호

        humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)  # 습도, 온도 측정
        result = ''
  
        if humidity is not None and temperature is not None:
            result = '{{ "temp":{0:0.1f} , "humidity":{1:0.1f} }}'.format(temperature, humidity)
            print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity))
        else:  # 측정 실패 시 오류 표시값 전송
            result = '{"temp":-100.0 , "humidity": -1.0}'
            print('Failed to get reading. Try again!')

        print(result)
    
        conn = pika.BlockingConnection(pika.ConnectionParameters(self.__url, self.__port, self.__vhost, self.__cred))
        chan = conn.channel()
        chan.basic_publish(
            exchange = self.__exchange,
            routing_key = self.__queue,
            body = result
        )
        conn.close()
        return

publisher = Publisher()
publisher.main()




