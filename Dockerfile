FROM quay.io/ukhomeofficedigital/openjdk8:v1.8.0.131


ENV USER user-pttg-rps-scheduler
ENV GROUP group-pttg-rps-scheduler
ENV NAME pttg-rps-scheduler
ENV JAR_PATH build/libs

RUN yum update -y glibc && \
    yum update -y nss && \
    yum update -y bind-license

WORKDIR /app

RUN groupadd -r ${GROUP} && \
    useradd -r -g ${GROUP} ${USER} -d /app && \
    mkdir -p /app && \
    chown -R ${USER}:${GROUP} /app

COPY ${JAR_PATH}/${NAME}*.jar /app
COPY run.sh /app

RUN chmod a+x /app/run.sh

EXPOSE 8081

USER ${USER}

ENTRYPOINT /app/run.sh
