FROM tomcat:10.1-jdk17

WORKDIR /usr/local/tomcat/webapps/

# Copiar el WAR desde la ruta con espacios
COPY ["entrega proyecto enero 2026/api/target/api.war", "ROOT.war"]

EXPOSE 8080

CMD ["catalina.sh", "run"]