
FROM tomcat:10.1-jdk17

WORKDIR /usr/local/tomcat/webapps/

# Ajusta "mi-app.war" al nombre real del WAR generado
COPY "entrega proyecto enero 2026/api/target/api.war" ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]