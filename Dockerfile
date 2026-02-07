FROM gradle:9.0.0-jdk21

WORKDIR /app

COPY /app .

RUN gradleinstallDist

CMD ./build/install/app/bin/app