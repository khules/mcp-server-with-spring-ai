FROM openjdk:21-ea-slim-buster as builder

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:21-ea-slim-buster
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./

COPY config config
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher","--spring.config.location=config/application.yml"]