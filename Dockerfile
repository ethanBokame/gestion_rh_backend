FROM eclipse-temurin:21

WORKDIR /app

COPY gradle /app/gradle/
COPY grails-app/views /app/grails-app/views
COPY gradlew build.gradle gradle.properties /app/

RUN ./gradlew dependencies

COPY . .

CMD ["./gradlew", "bootRun"]