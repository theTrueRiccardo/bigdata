FROM eclipse-temurin:21-jre

WORKDIR /taxi-app

# Copy the packaged server
COPY /backend/target/universal/taxi-trajectory-analytics-1.0-SNAPSHOT.zip /taxi-app

RUN ["sh", "-c", "apt update && apt install unzip"]

RUN ["sh", "-c", "unzip taxi-trajectory-analytics-1.0-SNAPSHOT.zip"]

RUN ["sh", "-c", "chmod +x taxi-trajectory-analytics-1.0-SNAPSHOT/bin/taxi-trajectory-analytics"]

RUN ["sh", "-c", "mkdir /taxi-app/data"]

ENV DATASET=/taxi-app/data

EXPOSE 9000

ENTRYPOINT ["sh", "-c", "taxi-trajectory-analytics-1.0-SNAPSHOT/bin/taxi-trajectory-analytics"]