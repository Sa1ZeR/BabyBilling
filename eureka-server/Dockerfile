FROM openjdk:17

ARG PRODUCT_NAME=eureka
ARG USER_UID=1001
ARG USER_GID=1001
ARG USER_NAME=$PRODUCT_NAME
ARG USER_GROUP=$PRODUCT_NAME
ARG USER_HOME=/home/$USER_NAME

RUN microdnf clean all && \
        rm -rf /var/cache/yum/* && \
        rm -rf ~/.cache/ && \
        rm -rf /tmp/* && \
        groupadd --gid $USER_GID $USER_GROUP && \
        useradd --create-home \
                --shell /bin/bash \
                --uid $USER_UID \
                --gid $USER_GROUP \
                --home $USER_HOME $USER_NAME && \
        chown -R $USER_NAME:$USER_GROUP $USER_HOME

USER $USER_NAME
WORKDIR $USER_HOME

ARG JAR_FILE=./build/libs/eureka-server-0.0.1-SNAPSHOT.jar
ENV JAR_FILE ${JAR_FILE}

ENV SERVER_PORT=3000

EXPOSE ${SERVER_PORT}

COPY --chown=$USER_NAME:$USER_GROUP ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar", "SERVER_PORT=${SERVER_PORT}"]