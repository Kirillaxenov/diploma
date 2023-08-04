# Дипломная работа профессии «Тестировщик ПО»
## Документы
* [План автоматизации](https://github.com/Kirillaxenov/diploma/blob/main/documents/Plan.md)
* [Отчёт по итогам тестирования](https://github.com/Kirillaxenov/diploma/blob/main/documents/Report.md)
* [Отчёт об автоматизации](https://github.com/Kirillaxenov/diploma/blob/main/documents/Summary.md)




Дипломный проект — автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.
## Описание приложения
Приложение предстовляёт собой веб-сервис, который предлагает купить тур по определённой цене двумя способами:
Обычная оплата по дебетовой карте.
Уникальная технология: выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

сервису платежей, далее Payment Gate;
кредитному сервису, далее Credit Gate.
Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом. Данные карт при этом сохранять не допускается.
## Инуструкция по запуску тестов

1. Склонировать проект `git clone git@github.com:Kirillaxenov/diploma.git`
   
3. Открыть проект в IntelliJ IDEA
 
5. Запустить контейнеры командой `docker-compose up --build`
   
7. Для запуска сервиса с указанием пути к базе данных использовать следующие команды:
   
* для mysql `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar aqa-shop.jar`
  
* для postgresql `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar aqa-shop.jar`
  
5. Sut открывается по адресу `http://localhost:8080/`
  
6. Запуск тестов стоит выполнить с параметрами, указав путь к базе данных в командной строке:
   
  * для mysql `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
    
  * для postgresql `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
    
7. Для формирования отчета (Allure), после выполнения тестов выполнть команду `./gradlew allureReport`
   
8. После заврешения тестирования завершить работу приложения (CTRL+C) и остановить контейнеры командой `docker-compose down`

