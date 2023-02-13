FROM ubuntu:20.04

#docker build . -t mkluza/e-biznes:latest
#docker push mkluza/e-biznes:latest

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# base tools
RUN apt-get update -y && \
    apt-get upgrade -y &&\
    apt-get install -y \
    git \
    wget \
    curl \
    gnupg \
    unzip

# Install OpenJDK-8
RUN apt-get update &&\
    apt-get install -y openjdk-8-jdk &&\
    apt-get clean

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install scala 2.12
RUN wget https://downloads.lightbend.com/scala/2.12.2/scala-2.12.2.deb && \
    dpkg -i scala-2.12.2.deb && \
    apt-get update && \
    apt-get install -y scala

# Install SBT
# https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
	apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
	apt update && apt install sbt -y

RUN adduser --disabled-password \
    --gecos '' mkluza
RUN adduser mkluza sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> \
    /etc/sudoers
USER mkluza
WORKDIR /home/mkluza/

RUN cd /home/mkluza/
RUN mkdir target
COPY sklep-1.0.zip ./target
RUN unzip -o -d ./target /home/mkluza/target/sklep-1.0.zip


CMD cd ./target/sklep-1.0/bin && ./sklep -Dplay.evolutions.db.default.autoApply=true -Dhttp.port=9000
EXPOSE 9000
