#### PASSOS PARA RODAR O DOCKET
#
# ./mvnw package
#
# CRIAÇÃO DA IMAGEM
#
# docker build -f src/main/docker/Dockerfile.jvm -t quarkus/hackathon .
#
# RODAR O CONTAINER:
#
# docker run -i --rm -p 8080:8080 --env-file .env quarkus/hackathon
#
# OBS: Caso queira debugar a aplicação remotamente (ex: usando o IntelliJ)
# Expor a porta de debug (default 5005), exemplo :  EXPOSE 8080 5005.
# Setar -e JAVA_DEBUG=true and -e JAVA_DEBUG_PORT=*:5005 quando estiver rodando o container
#
FROM registry.access.redhat.com/ubi9/openjdk-21:1.21

ENV LANGUAGE='en_US:en'

COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
