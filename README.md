# 2022-05-otus-spring-Kordyukova_02
Создать приложение хранящее информацию о книгах в библиотеке.
<br>
Первое задание:
<br>
Использовать Spring JDBC и реляционную базу (H2 или настоящую реляционную БД). Настоятельно рекомендуем использовать NamedParametersJdbcTemplate.
<br>
Предусмотреть таблицы авторов, книг и жанров.
<br>
Предполагается отношение многие-к-одному (у книги один автор и жанр). Опциональное усложнение - отношения многие-ко-многим (у книги может быть много авторов и/или жанров).
<br>
Интерфейс выполняется на Spring Shell (CRUD книги обязателен, операции с авторами и жанрами - как будет удобно).
<br>
Скрипт создания таблиц и скрипт заполнения данными должны автоматически запускаться с помощью spring-boot-starter-jdbc.
<br>
Написать тесты для всех методов DAO и сервиса работы с книгами. Рекомендации к выполнению работы:
<br>
<br>
<br>
Второе задание:
<br>
Использовать JPA, Hibernate только в качестве JPA-провайдера.
<br>
Для решения проблемы N+1 можно использовать специфические для Hibernate аннотации @Fetch и @BatchSize.
<br>
Добавить сущность "комментария к книге", реализовать CRUD для новой сущности.
<br>
Покрыть репозитории тестами, используя H2 базу данных и соответствующий H2 Hibernate-диалект для тестов.
<br>
Не забудьте отключить DDL через Hibernate
<br>
@Transactional рекомендуется ставить только на методы сервиса.
<br>
<br>
<br>
Третье задание:
<br>
Переписать все репозитории по работе с книгами на Spring Data JPA репозитории.
<br>
Используйте spring-boot-starter-data-jpa.
<br>
Кастомные методы репозиториев (или с хитрым @Query) покрыть тестами, используя H2.
<br>
@Transactional рекомендуется ставить на методы сервисов, а не репозиториев.
<br>
<br>
<br>
Четвертое задание:
<br>
Использовать Spring Data MongoDB репозитории, а если не хватает функциональности, то и *Operations.
<br>
Hibernate, равно, как и JPA, и spring-boot-starter-data-jpa не должно остаться в зависимостях
<br>
<br>
<br>
Пятое задание:
<br>
Создать приложение с хранением сущностей в БД (можно взять библиотеку и DAO/репозитории из прошлых занятий);
<br>
Использовать классический View на Thymeleaf, classic Controllers;
<br>
Для книг (главной сущности) на UI должны быть доступные все CRUD операции. CRUD остальных сущностей - по желанию/необходимости;
<br>
Протестировать контроллер(ы) для CRUD операций над книгами с помощью @WebMvcTest и моков сервисов;
<br>
Локализацию делать НЕ нужно - она строго опциональна;
<br>
<br>
<br>
Шестое задание:
<br>
Переписать приложение с классических View на AJAX архитектуру и REST-контроллеры;
<br>
Минимум: получение одной сущности и отображение её на странице с помощью XmlHttpRequest, fetch api или jQuery;
<br>
Опционально максимум: полноценное SPA приложение на React/Vue/Angular, только REST-контроллеры;
<br>
Протестировать все эндпойнты REST-контроллеров с моками зависимостей;
<br>
В случае разработки SPA - рекомендуется вынести репозиторий с front-end. Используйте proxy при разработке (настройки CORS не должно быть).
<br>
Данное задание, выполненное в виде SPA засчитывает предыдущее ДЗ
<br>
<br>
<br>
Седьмое задание:
<br>
За основу для выполнения работы можно взять ДЗ с Ajax + REST.
<br>
Необходимо использовать Reactive Spring Data Repositories.
<br>
В качестве БД лучше использовать MongoDB или Redis.
<br>
RxJava vs Project Reactor - на Ваш вкус.
<br>
Вместо классического Spring MVC и embedded Web-сервера использовать WebFlux.
<br>
Опционально: реализовать на Functional Endpoints
<br>
<br>
<br>
Восьмое задание:
Добавить в приложение новую сущность - пользователь. Необязательно реализовывать методы по созданию
<br> 
пользователей - допустимо добавить пользователей только через БД-скрипты.
<br>
В существующее CRUD-приложение добавить механизм Form-based аутентификации.
<br>
UsersServices реализовать самостоятельно.
<br>
Авторизация на всех страницах - для всех аутентифицированных. Форма логина - доступна для всех.
<br>
Написать тесты контроллеров, которые проверяют, что все необходимые ресурсы действительно защищены.
<br>
<br>
<br>
Девятое задание:
<br>
Минимум: настроить в приложении авторизацию на уровне URL.
<br>
Максимум: настроить в приложении авторизацию на основе доменных сущностей и методов сервиса.
<br>
Рекомендации по выполнению:
<br>
Не рекомендуется выделять пользователей с разными правами в разные классы - т.е. просто один класс пользователя.
<br>
В случае авторизации на основе доменных сущностей и PostgreSQL не используйте GUID для сущностей.
<br>
Написать тесты контроллеров, которые проверяют, что все необходимые ресурсы действительно защищены.
<br>
<br>
<br>
Выполнила: Kordyukova Galina
