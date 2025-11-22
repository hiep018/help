# Giai đoạn 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Run
# Đổi sang ảnh nền eclipse-temurin để không bị lỗi "not found"
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 3040
ENTRYPOINT ["java","-jar","app.jar"]