FROM java:8
VOLUME /tmp
RUN mkdir /home/dukeapp
RUN mkdir /home/dukeapp/logs
RUN chmod -R 777 /home/dukeapp
ADD DocumentMicroService-1.0.jar /home/dukeapp/app.jar
RUN bash -c 'touch /home/dukeapp/app.jar'
RUN chmod 777 /home/dukeapp/app.jar
USER 1001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/dukeapp/app.jar"]
EXPOSE 8080
