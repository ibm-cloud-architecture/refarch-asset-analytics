FROM debian:jessie

ENV ZOOKEEPER_VERSION 3.4.13
ENV ZK_HOME /opt/zookeeper

RUN apt-get update && apt-get install -y openjdk-7-jre-headless wget
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

RUN wget -q http://mirror.vorboss.net/apache/zookeeper/zookeeper-${ZOOKEEPER_VERSION}/zookeeper-${ZOOKEEPER_VERSION}.tar.gz && \
wget -q https://www.apache.org/dist/zookeeper/KEYS && \
wget -q https://www.apache.org/dist/zookeeper/zookeeper-${ZOOKEEPER_VERSION}/zookeeper-${ZOOKEEPER_VERSION}.tar.gz.asc && \
wget -q https://www.apache.org/dist/zookeeper/zookeeper-${ZOOKEEPER_VERSION}/zookeeper-${ZOOKEEPER_VERSION}.tar.gz.md5

#Verify download
RUN md5sum -c zookeeper-${ZOOKEEPER_VERSION}.tar.gz.md5 && \
gpg --import KEYS && \
gpg --verify zookeeper-${ZOOKEEPER_VERSION}.tar.gz.asc

#Install
RUN tar -xzf zookeeper-${ZOOKEEPER_VERSION}.tar.gz -C /opt
RUN mv /opt/zookeeper-${ZOOKEEPER_VERSION} $ZK_HOME
RUN mkdir $ZK_HOME/data


COPY zoo.cfg $ZK_HOME/conf/zoo.cfg

EXPOSE 2181 2888 3888
WORKDIR $ZK_HOME

CMD $ZK_HOME/bin/zkServer.sh start-foreground
