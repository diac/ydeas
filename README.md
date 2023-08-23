# Ydeas

## Описание проекта
Проект представляет собой сервис для оценки бизнес-идей.
Сервис позволяет пользователям предлагать свою бизнес-идею для рассмотрения экспертами, которые в последствии 
принимают решение о ее жизнеспособности. Пользователь вводит текстовое описание идеи и опционально загружает 
дополнительные материалы, связанные с идеей (PDF-документы, картинки). Другие пользователи системы имеют 
возможность просматривать все загруженные бизнес-идеи и голосовать за них оценками "Нравится" или "Не нравится".
Пользователи-эксперты должны иметь возможность утвердить или отклонить бизнес-идею.

## Роли пользователей
1. Пользователь -- может предлагать свои идеи, просматривать все бизнес-идеи, созданные другими пользователями, 
оценивать бизнес-идеи, добавленные в систему
2. Эксперт -- может все то же, что и Пользователь, плюс имеет возможность утверждать или отклонять идеи.

## Микросервисы
1. Users -- хранит информацию о пользователях: Фамилия, Имя, роль, email. Данные сервиса обогащаются данными из Keycloak.
2. Ideas -- основной микросервис, в котором хранятся сущности: идеи (с данными о прикрепленных файлах в S3), 
статусы, авторы, лайки.
3. Files -- сервис-обвязка вокруг S3. Отвечает за загрузку файлов в хранилище.
4. Notifications -- сервис, отсылающий уведомления о публикации новых идей, изменениях статусов идей,
оценках идей и т.д. Взаимодействие с этим сервисом осуществляется через Kafka.
5. Gateway -- сервис-шлюз, через который осуществляется доступ в экосистему проекта извне.
6. Eureka Server -- сервис для регистрации и обнаружения сервисов внутри экосистемы.

## Особенности реализации
В проекте используется микросервисная архитектура.<br>
У каждого микросервиса, работающего с пользовательскими данными, есть своя выделенная база данных.<br>
Для авторизации и аутентификации пользователей используется Keycloak в связке со Spring Security.<br>
Для хранения файлов используется Amazon S3.<br>
<br>
В микросервисе Notifications в учебных целях реализован механизм оркестрирования, позволяющий повторять попытки
передачи уведомлений в случае возникновения ошибки. Для этого, при отправке уведомления соответствующий метод сервиса
возвращает ошибку вызова с вероятностью 50%. Сервис учитывает, какие уведомления были отправлены успешно, и какие
не были отправлены в результате ошибки. Неотправленные в результате ошибки уведомления возвращаются 
в очередь на отправку. Отправка уведомлений повторяется по определенному расписанию (1 раз в 15 секунд) 
и осуществляется в многопоточном режиме.

![Архитектура проекта](/img/001_project_architecture.png)

## Стек технологий
- Java 17
- Spring Boot 3.0.5
- Spring Web
- Spring Data
- Spring Security
- Spring Cloud 2022.0.1
- Netflix Eureka
- Keycloak
- Apache Kafka
- Amazon S3
- PostgreSQL 14.5
- Liquibase 4.17.2
- JUnit 5.9.2
- AssertJ 3.23.1
- Mockito 4.8.1
- Docker
- Lombok
- Testcontainers

## Требования к окружению
- Java 17+
- Maven 3.8
- PostgreSQL 14
- Docker
- Keycloak
- Apache Kafka

## Сборка и запуск проекта
### Сборка и запуск проекта через Docker
Для сборки локальной версии через докер необходимо выполнить следующую команду:
```shell
docker compose -f docker-compose-standalone.yml up
```

Файл docker-compose-standalone.yml содержит конфигурацию для тестовой локальной сборки.

### Конфигурация баз данных
Для обеспечения работы системы необходимо создать следующие базы данных:
- ydeas_ideas
- ydeas_notifications

Создать базы данных можно выполнив следующие запросы, например, в интерфейсе командной строки сервера баз данных:
```shell
CREATE DATABASE ydeas_ideas;
CREATE DATABASE ydeas_notifications;
```

Также для целей разработки и тестирования проекта можно создать Docker-образ и контейнер с необходимыми базами данных,
выполнив файл commands.sh в директории db проекта.
```shell
./commands.sh
```

## Контакты
email: nikolai.gladkikh.biz22@gmail.com