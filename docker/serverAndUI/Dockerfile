#
# conductor:serverAndUI - Combined Netflix conductor server & UI 
#
# ===========================================================================================================
# 0. Builder client stage
# ===========================================================================================================

FROM node:14-alpine AS builderUI
LABEL maintainer="Netflix OSS <conductor@netflix.com>"

RUN yarn config set registry https://registry.npmmirror.com
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories

# Install the required packages for the node build
# to run on alpine
RUN apk update && apk add --no-cache python3 py3-pip make g++

# Copy the project onto the builder image
COPY ./ui /conductor/ui

# Build the client
WORKDIR /conductor/ui
RUN yarn config set registry https://registry.npmmirror.com
RUN yarn install && yarn build

# ===========================================================================================================
# 1. Builder stage
# ===========================================================================================================
FROM openjdk:11-jdk-buster AS builder
LABEL maintainer="Netflix OSS <conductor@netflix.com>"

# Copy the project onto the builder image
COPY . /conductor

# Build the server
WORKDIR /conductor
RUN ./gradlew build -x test --stacktrace

# ===========================================================================================================
# 2. Bin stage
# ===========================================================================================================

FROM openjdk:11-jdk-buster

LABEL maintainer="Netflix OSS <conductor@netflix.com>"

RUN sed -i "s@http://deb.debian.org@http://mirrors.tuna.tsinghua.edu.cn@g" /etc/apt/sources.list \
  && sed -i "s@http://security.debian.org@http://mirrors.tuna.tsinghua.edu.cn@g" /etc/apt/sources.list

RUN apt-get update -qq \
  && apt-get install -qq --no-install-recommends nginx \
  && rm -rf /var/lib/apt/lists/*

# Make app folders
RUN mkdir -p /app/config /app/logs /app/libs

# Copy the compiled output to new image
COPY --from=builder /conductor/docker/serverAndUI/bin /app
COPY --from=builder /conductor/docker/serverAndUI/config /app/config
COPY --from=builder /conductor/server/build/libs/conductor-server-*-boot.jar /app/libs

# Copy compiled UI assets to nginx www directory
WORKDIR /usr/share/nginx/html
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builderUI /conductor/ui/build .
COPY --from=builder /conductor/docker/serverAndUI/nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy the files for the server into the app folders
RUN chmod +x /app/startup.sh

HEALTHCHECK --interval=60s --timeout=30s --retries=10 CMD curl -I -XGET http://localhost:8080/health || exit 1

EXPOSE 5000
EXPOSE 8080

WORKDIR /app
CMD [ "/app/startup.sh" ]
ENTRYPOINT [ "/bin/sh"]
