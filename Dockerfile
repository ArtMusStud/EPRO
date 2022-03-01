FROM openjdk:11-jdk-slim
RUN groupadd -r spring && useradd --no-log-init -r -g spring spring
USER spring:spring
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
COPY maven/target/*.jar app.jar
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENV spring_profiles_active="prod"
ENTRYPOINT ["java","-jar","/app.jar"]