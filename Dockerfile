# 基于Java的官方镜像
FROM openjdk:21-jdk

## 设置工作目录
WORKDIR /home/starla/app

# 先拷贝依赖到镜像中（缓存层）
COPY build/dependencies /home/starla/app/libs/
# 设置环境变量，指定依赖目录
ENV CLASSPATH="/home/starla/app/libs/*"

# 再拷贝瘦 JAR（应用代码）
COPY build/libs/springboot-0.0.1-SNAPSHOT.jar /home/starla/app/springboot-0.0.1-SNAPSHOT.jar

# 启动应用程序
ENTRYPOINT ["java", "-jar", "/home/starla/app/springboot-0.0.1-SNAPSHOT.jar"]
#
# Copy wait-for-it.sh script to the image
#COPY C:/Users/Starla_Huang/Documents/project/Java/training/wait-for-it.sh .

# Assign proper permissions to the script to make it executable
#RUN chmod +x wait-for-it.sh

# 设置运行时容器监听的端口号
EXPOSE 8080

# ENTRYPOINT ["wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "springboot-0.0.1-SNAPSHOT.jar"]