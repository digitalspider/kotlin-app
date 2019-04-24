# kotlin-app
Trying out Kotlin

Initially from:
* <https://www.callicoder.com/kotlin-spring-boot-mysql-jpa-hibernate-rest-api-tutorial/>
* <https://github.com/callicoder/kotlin-spring-boot-jpa-rest-api-demo>

For OAuth/JWT see:
* <https://github.com/quangIO/spring-kotlin-jwt-sample>
* <https://auth0.com/blog/how-to-create-a-kotlin-app-and-secure-it-using-jwt/>

```
curl -i -H "Content-Type: application/json" -X POST \
-d '{"title": "How to learn Spring framework", "content": "Resources to learn Spring framework"}' \
http://localhost:8080/api/articles

curl -i -H "Content-Type: application/json" -X PUT \
-d '{"title": "Learning Spring Boot", "content": "Some resources to learn Spring Boot"}' \
http://localhost:8080/api/articles/1
```

