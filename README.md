# Конвертор валют

Чтобы запустить проект необходимо склонировать репозиторий и в дирректории проекта ввести команду:

```docker compose up```

Все необходимые начальные данные будут загружены в базу данных при запуске программы.

Проект будет запущен на порту 8080 \
Секретный ключ - ```verysecretkey``` \
Комиссия по умолчанию будет равна 0

Начальные валюты:

```USD - 300``` \
```RUB - 5000```

# Все запросы:

## GET - запросы:

```curl -X GET "http://localhost:8080/currency"``` \
выводит информацию о доступных счетах

```curl -X GET "http://localhost:8080/officialrates?date=2023-11-11&pair=UZS/USD"``` \
выводит информацию курса пары валют по определенной дате

```curl -X GET "http://localhost:8080/convert?from=RUB&to=UZS&amount=54856"``` \
выводит информацию о итоговой сумме конвертации валют с учетом комиссии

```curl -X GET "http://localhost:8080/getcomission"``` \
выводит информацию о текущих значений комиссии

## POST - запросы:

```curl -X POST -H "Content-Type: application/json" -d '{"name" : "UZS", "amount" : 300000}' http://localhost:8080/currency``` \
вводится счет на определенную валюту

```curl -X POST -H "Content-Type: application/json" -d '{"from": "USD", "to": "UZS",  "amount": 3}' http://localhost:8080/convert``` \
ковертирует валюту с учетом комиссии

```curl -X POST -H "Content-Type: application/json" -d '{"comissionFrom": "2", "comissionTo": "0", "key": "{ключ}"}' http://localhost:8080/setcomission``` \
меняет значения комиссии при верном ключе

```curl -X POST -H "Content-Type: application/json" -d '{новый ключ}' http://localhost:8080/key``` \
меняет значение ключа
