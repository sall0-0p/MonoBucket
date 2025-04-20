# Stage 1: Build the application
FROM amazoncorretto:17 AS build

WORKDIR /app

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
ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/app.jar"]
