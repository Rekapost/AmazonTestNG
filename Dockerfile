FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y maven
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"
RUN apt-get update && apt-get install -y \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxtst6 \
    fonts-liberation \
    libappindicator3-1 \
    xdg-utils \
    libatk-bridge2.0-0 \
    libgtk-3-0
RUN apt update
RUN apt install -y wget gnupg
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list
RUN apt update && apt install -y google-chrome-stable    
RUN java -version
RUN echo "JAVA_HOME is set to: $JAVA_HOME"
COPY ./src/test/resources/ChromeDriver/chromedriver /usr/local/bin/chromedriver
RUN chmod +x /usr/local/bin/chromedriver
COPY . /home/Amazon
WORKDIR /home/Amazon
RUN mvn -f /home/Amazon/pom.xml clean test -DskipTests=true
EXPOSE 8080
#CMD ["mvn", "clean", "test"]
#CMD ["tail", "-f", "/dev/null"]
CMD mvn clean test && tail -f /dev/null

