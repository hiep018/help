# Giai đoạn 1: Build (Xây dựng file .jar)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Dùng lệnh mvn của Docker để tránh lỗi dòng lệnh Windows
RUN mvn clean package -DskipTests

# Giai đoạn 2: Run (Chạy ứng dụng)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 3040 như trong code của bạn
EXPOSE 3040
ENTRYPOINT ["java","-jar","app.jar"]