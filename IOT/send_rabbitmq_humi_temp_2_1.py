#!/usr/bin/python3 

import Adafruit_DHT      # 라이브러리 불러오기
import pika # AMQP 라이브러리

class Publisher:
    def __init__(self):
        self.__url = '192.168.100.60'
        self.__port = 5672
        self.__vhost = 'egov'
        self.__cred = pika.PlainCredentials('guest', 'guest')
        self.__exchange = 'sensor';
        self.__queue = 'q_temp_humi';
        return

    def main(self):
    
        sensor = Adafruit_DHT.DHT22     #  sensor 객체 생성 
        pin = 4                        # Data핀의 GPIO핀 넘버

        humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)   # 센서 객체에서 센서 값(ㅇ노도, 습도) 읽기
        result = ''
  
        if humidity is not None and temperature is not None:   #습도 및 온도 값이 모두 제대로 읽혔다면 
            result = '{{ "temp":{0:0.1f} , "humidity":{1:0.1f} }}'.format(temperature, humidity)
            print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity))  # 온도, 습도 순으로 표시
        else:                                                  # 에러가 생겼다면 
            result = '{"temp":-100.0 , "humidity": -1.0}'
            print('Failed to get reading. Try again!')        #  에러 표시

        print(result)  # 온도, 습도 순으로 표시
    
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




