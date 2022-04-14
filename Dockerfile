FROM amazoncorretto:17-alpine-jdk as min-jdk17-builder
RUN apk add --update binutils && \
  /usr/lib/jvm/default-jvm/bin/jlink \
    --add-modules java.sql,java.naming,java.net.http,java.management,java.instrument,java.desktop,java.security.jgss,jdk.crypto.ec,jdk.crypto.cryptoki,java.rmi,jdk.unsupported \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /opt/corretto-slim

FROM alpine:3.15
ENV LANG C.UTF-8
ENV API_HOME=/usr/local/api
ENV CONFIG_HOME = $API_HOME/config
ENV JAVA_HOME=/usr/lib/jvm/default-jvm
ENV JAVA_OPTS="-Xms3g -Xmx3g -Duser.timezone=America/New_York"

COPY --from=min-jdk17-builder /opt/corretto-slim $JAVA_HOME

ENV LANG C.UTF-8
ENV PATH=$PATH:$JAVA_HOME/bin

RUN apk update \
    && apk upgrade

COPY target/api.jar $API_HOME/api.jar
EXPOSE 80
WORKDIR $API_HOME
ENTRYPOINT [ "sh", "-c", "java -Dloader.path=$CONFIG_HOME -jar $JAVA_OPTS $API_HOME/api.jar" ]