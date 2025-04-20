# Stage 1: Build the application
FROM amazoncorretto:17 AS build

WORKDIR /app

RUN yum install -y git && yum clean all

COPY gradlew build.gradle settings.gradle ./
COPY gradle/ gradle/

RUN yum install -y findutils \
 && yum clean all

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src/ src/
RUN ./gradlew bootJar --no-daemon --stacktrace

# Stage 2: Final runtime image
FROM amazoncorretto:17
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
