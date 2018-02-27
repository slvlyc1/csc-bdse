#Что нужно для старта разработки:
- Необходимо установить Java SE Development Kit 8 http://www.oracle.com/technetwork/java/javase/downloads/2133151  
- Для первоначальной сборки проекта необходим доступ до maven-репозиториев зависимостей (может быть неожиданно много,
 но скачиваются один раз)
- Необходимо установить Docker CE https://www.docker.com/community-edition
- Для первоначальной сборки контейнеров интеграционных тестов необходим доступ до Docker HUB (openjdk:8-jdk-alpine)
- На Linux необходимо добавить текущего пользователя в группу docker https://docs.docker.com/install/linux/linux-postinstall/
- Рекомендуется использовать IntelliJ IDEA https://www.jetbrains.com/idea/download/
- Не рекомендуется использовать Windows https://www.testcontainers.org/usage/windows_support.html 

#Структура модулей проекта
- bdse-app содержит код реализуемого бизнес-приложения 
- bdse-kvnode содержит код Persistent Storage Unit 
- bdse-integration-tests содержит утилиты и тесты для интеграционного тестирования

#Сборка и запуск интеграционных тестов
./mvnw --projects bdse-kvnode clean package
./mvnw --projects bdse-integration-tests --also-make test

#Как запустить это все локально (КЭП, но может время сэкономит немножко)
#создает image с названием test (надо только чтобы bdse-kvnode-0.0.1-SNAPSHOT и Dockerfile жили в одной директории, 
#которая ребенок относительно текущей)
docker build -t test -f Dockerfile .
#запускает image, мапит порт
docker run -p 8080:8080 test

#run image with postgress
docker run -p 5432:5432 -d --name postgres -v $(pwd):/workspace postgres:9.6
#setup db
docker run --rm  --name psql --link postgres:postgres -v $(pwd):/workspace postgres:9.6 psql -h postgres -U postgres -f /workspace/create_table.sql
