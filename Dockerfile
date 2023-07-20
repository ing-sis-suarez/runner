FROM gradle:7.6.1-jdk17
COPY . /home/gradle/src
WORKDIR /home/gradle/src

ARG USERNAME
ENV USERNAME ${USERNAME}

ARG TOKEN
ENV TOKEN ${TOKEN}

RUN chmod +x gradlew
RUN ./gradlew build
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/runner-0.0.1-SNAPSHOT.jar"]