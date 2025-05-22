FROM openjdk:8-jdk

LABEL author=Ulyana

# Установка необходимых библиотек для AWT/Swing GUI
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    libfontconfig1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY ./src/main/java /app/src

COPY target/HexEditorGUI-1.0-SNAPSHOT.jar app.jar

RUN mkdir -p /app/docs
# генерация javadoc
RUN javadoc -d /app/docs -sourcepath /app/src -subpackages ru.khav.ProjectNIC

VOLUME ["/app/docs"]
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["echo", "javadoc сгенерирован в /app/docs"]