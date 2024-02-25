## Сервис психологичечкой поддержки.

### Запуск проекта

1. mvn clean install
2. docker-compose up для запуска kafka
3. Запустить StartApp
4.Приложение запущено http://localhost:8080

### API

get /help-service/v1/support

post /help-service/v1/support


### Подключение и настройка кафки

```
kafka:
    support-service:
        server: урл кафки
        enabled: включение/отключение (true/false)
        topic: имя топика для данного сервиса
        dlq: суффикс для топика необработтаных сообщений
        backoff:
            interval:интервал для повторных попыток, после неудачной обработки сообщения
            max-attempts: максимальное количество попыток
```