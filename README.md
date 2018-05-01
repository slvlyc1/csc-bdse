## csc-bdse
Базовый проект для практики по курсу "Программная инженерия больших данных".

Участники:  
  * [Алена Коротеева](https://github.com/pavlin256)
  * [Федор Корчажников](https://github.com/butnotstupid)

## Задания
[Подготовка](INSTALL.md)

[Часто задаваемые вопросы](FAQ.md)

[Задание 1](TASK1.md)

## Описание
Реализованна основная функциональность (добавление, удаление, апдейт, поиск по префиксу ключа). 
Нода (пока?) ничего не кеширует, просто сохраняет/читает базу. В качестве базы мы использовали Mongo.

В интеграционных тестах база и нода запускаются в отдельных докер-контейнерах.

[Задание 2](TASK2.md)

[Задание 3](TASK3.md)

## Описание
Релизовано:
 * основная логика координатора
 * обертка над сообщением клиента
 * логика разрешения конфликтов при получении разных записей от разных нод
 * написаны unit-тесты для такого ConflictResolver-а
 
Не реализовано:
 * интеграционные тесты

[Задание 4](TASK4.md)
