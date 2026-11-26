// 최초 기동 시 애플리케이션 계정 생성 (MONGO_INITDB_DATABASE 에 dbOwner 권한)
const dbName = process.env.MONGO_INITDB_DATABASE || 'com';
db.getSiblingDB(dbName).createUser({
  user: process.env.MONGO_USERNAME || 'com',
  pwd: process.env.MONGO_PASSWORD || 'com01',
  roles: [{ role: 'dbOwner', db: dbName }]
});
