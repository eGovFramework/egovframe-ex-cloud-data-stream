1. 설치

	cp .env.example .env   (MONGO_ROOT_PASSWORD 등 실제 값으로 변경, 미설정 시 기동 실패)

	docker compose up -d

	* 최초 기동 시 init-user.js 가 애플리케이션 계정(MONGO_USERNAME / MONGO_PASSWORD, 기본 com / com01, DB com)을 자동 생성한다.
	* 이미 데이터가 있는 컨테이너에는 실행되지 않으므로 아래 4~5 를 수동으로 수행한다.

2. docker shell 접속

	docker exec -it my-mongodb bash

3. mongodb 접속

	mongosh -u root -p <MONGO_ROOT_PASSWORD>

	* MongoDB 6.0 이상은 mongosh, 5.x 이하는 mongo

4. database 확인 (수동 생성 시)

	show dbs;

	use com;

5. user 생성 (수동 생성 시)

	use com;

	db.createUser({user:"com",pwd:"com01",roles:[{role:"dbOwner",db:"com"}]});

	db.getUsers();
