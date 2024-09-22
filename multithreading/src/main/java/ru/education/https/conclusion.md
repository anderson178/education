## Как работает https:
### 1. механизм шифрования данных
Клиент и сервер обмениваются ключами используя алгоритм Диффи-Хелмана.
У обоих из сторон есть: закрытый и открытый ключи, два числа root и prime. Оба значения пересылаются в открытом виде. Для обоих сторон эти числа одинаковые.
Каждый из сторон производит вычисление используя закрытый ключ root и prime получая число. 
Каждая из сторон передает полученное число на основе которого производятся другие вычисление для получения секретного ключа сессии.
И далее уже по секретному ключу сессии производится расшифровка данных. И при этом никто не отправил свой личный закрытый ключ
Алгоритм:

Вычисление перед отправкой:
Alice’s mixture = (root ^ Alice’s Secret) % prime
Bob’s mixture = (root ^ Bob’s Secret) % prime
Вычисление после приема:
Вычисления Алисы
(Bob’s mixture ^ Alice’s Secret) % prime

Вычисления Боба
(Alice’s mixture ^ Bob’s Secret) % prime

### 2. аутентификация
Что бы обе стороны могли быть уверенными, что они общаются именно друг с другом, 
а не с кем-то другим кто мог перехватить секретный ключ канала, происходит сверка сертификатов.
Сертификат формируется на основе цифровой подписи, на выходе имея закрытый и открытый ключ. Именно благодаря цифровой
подписи происходит удостоверение подлинности сервера.



### Что шифрует https:
1. заголовки
2. тело
3. url
4. куки


## Статья
- https://habr.com/ru/articles/188042/