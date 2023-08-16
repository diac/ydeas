# Create jar package
FROM maven:3.8-openjdk-17 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml -pl domain,notifications clean package -Dmaven.test.skip=true

# Launch the app
FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=build /usr/app/notifications/target/notifications-1.0-SNAPSHOT.jar /app/app.jar
CMD java -jar /app/app.jar