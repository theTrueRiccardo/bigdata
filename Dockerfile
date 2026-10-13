FROM eclipse-temurin:21-jre

WORKDIR /taxi-app

# Copy the packaged server
COPY /backend/target/universal/taxi-trajectory-analytics-1.0-SNAPSHOT.zip /taxi-app

RUN ["sh", "-c", "apt update && apt install unzip"]

RUN ["sh", "-c", "unzip taxi-trajectory-analytics-1.0-SNAPSHOT.zip"]

RUN ["sh", "-c", "chmod +x taxi-trajectory-analytics-1.0-SNAPSHOT/bin/taxi-trajectory-analytics"]

RUN ["sh", "-c", "mkdir /taxi-app/data"]

ENV DATASET=/taxi-app/data

ENV JAVA_OPTS="-Xms10g -Xmx12g \
-XX:+IgnoreUnrecognizedVMOptions \
--add-opens=java.base/java.lang=ALL-UNNAMED \
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens=java.base/java.io=ALL-UNNAMED \
--add-opens=java.base/java.net=ALL-UNNAMED \
--add-opens=java.base/java.nio=ALL-UNNAMED \
--add-opens=java.base/java.util=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED \
--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED \
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
--add-opens=java.base/sun.nio.cs=ALL-UNNAMED \
--add-opens=java.base/sun.security.action=ALL-UNNAMED \
--add-opens=java.base/sun.util.calendar=ALL-UNNAMED \
-Djdk.reflect.useDirectMethodHandle=false \
-Dio.netty.tryReflectionSetAccessible=true"

EXPOSE 9000

ENTRYPOINT ["sh", "-c", "taxi-trajectory-analytics-1.0-SNAPSHOT/bin/taxi-trajectory-analytics"]