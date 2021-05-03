#######################################
# resolved-dependencies
# SpringBoot の依存関係を取得
#######################################
FROM gradle:6.8.3-jdk11 AS resolved-dependencies

RUN mkdir -p /home/gradle/cache
ENV GRADLE_USER_HOME /home/gradle/cache
COPY build.gradle.kts /home/gradle/workdir/
WORKDIR /home/gradle/workdir
RUN gradle downloadDependencies
WORKDIR /var/local/my-app


###################################
# for-archive
# ソースの追加とJARファイルのビルド
###################################
FROM resolved-dependencies as for-archive
COPY . ./
RUN gradle build


############################################
# release
# アプリケーションサーバー用のイメージビルド
############################################
FROM openjdk:11.0.8-jre-slim-buster as release

COPY --from=for-archive /var/local/my-app/build/libs/my-app.jar /var/local/my-app/

ARG SPRING_PROFILE
ENV SPRING_PROFILE ${SPRING_PROFILE:-develop}
ENV TZ Asia/Tokyo
ENTRYPOINT java -jar /var/local/my-app/my-app.jar --spring.profiles.active=$SPRING_PROFILE
CMD -Xms1024M -Xmx1024M -Xss2048K
EXPOSE 8080
