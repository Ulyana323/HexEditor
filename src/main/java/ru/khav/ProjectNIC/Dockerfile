FROM openjdk:8-jdk-alpine

LABEL author=Ulyana

WORKDIR /app

COPY target/HexEditorGUI-1.0-SNAPSHOT app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]