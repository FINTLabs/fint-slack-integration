FROM gradle:5.3.1-jdk8-alpine as builder
USER root
COPY . .
ARG apiVersion
RUN gradle --no-daemon -PapiVersion=${apiVersion} build

FROM gcr.io/distroless/java
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-slack-integration-*.jar /data/fint-slack-integration.jar
CMD ["/data/fint-slack-integration.jar"]
