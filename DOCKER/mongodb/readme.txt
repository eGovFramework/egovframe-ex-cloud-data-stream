1. 설치

	- docker-compose.yml 파일을 이용하여 docker image, container 설치
	
		../mongodb/docker-compose up -d

2. docker shell 접속

	docker exec -it my-mongodb bash

3. mongodb 접속

	mongosh -u root -p frame001

	* 5.x 까지는 mongo

4. database 생성

	show dbs;

	use com;

	* exist : use database, not exist : create database

5. user 생성

	use com;

	db.createUser({user:"com",pwd:"com01",roles:[{role:"dbOwner",db:"com"}]});

	db.getUsers();



