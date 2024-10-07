# crm-ox
OX Tests task
Як запустити?
Варіант 1. (Із використанням IDEA)

В папці проекту потрібно ввести команду:

docker-compose -f docker-compose-local.yml up --build

Для запуску Application в Edit Configuration потрібно зробити наступну
Modify Options -> Add VM options і в поле VM options ввести цю строку:

-Dspring.profiles.active=dev

І запустити проект

в браузері відкрити посилання для логіну http://localhost:8089/auth/login
або для реєстрації http://localhost:8089/auth/register

Варіант 2. Повністю через докер

В папці проекту ввести команду 
docker-compose up

дочекатись запуску і в браузері відкрити посилання для логіну http://localhost:8089/auth/login
або для реєстрації http://localhost:8089/auth/register


