FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
# 创建工作目录
WORKDIR /app

# 复制打包好的 jar（GitLab CI 会提供它）
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 容器启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]