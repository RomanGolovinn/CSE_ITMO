# Лабораторная работа №2

## Структура

- `frontend/` — статическая страница, стили и JavaScript для Apache.
- `backend/` — Java FastCGI сервер и Gradle проект.
- `deployment/` — сюда добавим конфигурацию Apache и инструкции запуска на «Гелиосе».

Gradle проект находится в `backend/`. Исходный код сервера размещается в
`backend/src/main/java/ru/itmo/`, а ресурсы — в `backend/src/main/resources/`.

Apache будет раздавать содержимое `frontend/` и проксировать POST-запросы
к запущенному FastCGI серверу из `backend/`.
