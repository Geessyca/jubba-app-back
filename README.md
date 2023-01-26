# Java Spring Boot com Spring Security, MySQL, JWT e JavaxMail

## Dependência
```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```
## Propriedades do aplicativo
Abra `src/main/resources/application.properties`
```
spring.datasource.url= jdbc:mysql://localhost:3306/login?useSSL=false
spring.datasource.username= root
spring.datasource.password= ****

spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto= update

# App Properties
jubba.app.jwtCookieName= jubba
jubba.app.jwtSecret= jubbaSecretKey
jubba.app.jwtExpirationMs= 86400000
```
## Execute o aplicativo Spring Boot
```
mvn spring-boot:executar
```

## Execute as seguintes instruções de inserção SQL
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
## Propriedades do do e-mail
Abra `src/main/java/com/jubba/utils/SendEmail`

```
    email = "email@email.com"
    final String SERVIDOR_SMTP = "SMTP.office365.com";
    final int PORTA_SERVIDOR_SMTP = 587;
    final String CONTA_PADRAO = email;
    final String SENHA_CONTA_PADRAO = "****";

    final String from = "Jubba <"+email+">";

```

## Link uteis

<a href="https://www.devmedia.com.br/primeiros-passos-com-o-spring-boot/33654">Primeiros passos com o Spring Boot - DevMedia</a>