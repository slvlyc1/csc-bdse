#Что нужно для старта разработки:
- Необходимо установить Java SE Development Kit 8 http://www.oracle.com/technetwork/java/javase/downloads/2133151  
- Необходимо установить Docker CE https://www.docker.com/community-edition
- Для первоначальной сборки контейнеров интеграционных тестов необходим доступ до Docker HUB (openjdk:8-jdk-alpine)
- Рекомендуется использовать IntelliJ IDEA

#Структура модулей проекта
- bdse-app содержит код реализуемого бизнес-приложения 
- bdse-kvnode содержит код Persistent Storage Unit 
- bdse-integration-tests содержит утилиты и тесты для интеграционного тестирования

#Сборка и запуск интеграционных тестов
./mvnw --projects bdse-kvnode clean package
./mvnw --projects bdse-integration-tests clean test





