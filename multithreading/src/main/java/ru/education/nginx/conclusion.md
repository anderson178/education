## Nginx

### Общие сведения
Высокопроизводительный веб-сервер и обратный прокси-сервер, который широко используется для обслуживания статических файлов, обработки одновременных подключений и как балансировщик нагрузки. Он также может быть настроен как обратный прокси и кэширующий сервер. Вот краткий обзор того, как его установить, запустить и для чего он нужен
Не дублировать код, проявить прагматизм и проверить всю систему на наличие нужного метода/кода. Так как сложно будет поддерживать один и тот же код в разных местах

### Назначение Nginx
1. **Веб-сервер**: Прежде всего, используется для обслуживания статического контента, такого как HTML-файлы, изображения и CSS-стили.
2. **Обратный прокси**: Может действовать как посредник между клиентами и бэкенд-серверами, распределяя запросы и обеспечивая высокую доступность.
3. **Балансировщик нагрузки**: Распределяет трафик между несколькими серверами, чтобы гарантировать высокую доступность и надежность.
4. **SSL/TLS-терминация**: Обрабатывает SSL-сертификаты и шифрование, снижая нагрузку на бэкенд-серверы, позволяя им обмениваться данными по нешифрованному HTTP.
5. **Кэширование**: Сохраняет копии ресурсов для ускорения откликов и снижения нагрузки на бэкенд-серверы.
6. **Gzip-сжатие**: Сжимает контент перед отправкой клиентам для уменьшения использования полосы пропускания.

### Основная настройка
Конфигурационные файлы Nginx обычно находятся в директории `/etc/nginx/`. Основной файл конфигурации — это `nginx.conf`, а также могут быть дополнительные файлы в директориях `sites-available` и `sites-enabled`.

#### Пример конфигурации: 
№1

```nginx
server {
    listen 9090;
    server_name _;

    root /usr/protei/Protei_TRACER_FE/;
        index index.html;
        try_files $uri $uri/ /index.html;
    location ~ /\. {
            deny all;
    }
    location /sigtracer/ {
        proxy_pass http://127.0.0.1:8092/sigtracer/;
        proxy_redirect off;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /search {
        index index.html;
        alias /usr/protei/Protei_TRACER_FE;
    }
    location /history {
        index index.html;
        alias /usr/protei/Protei_TRACER_FE;
    }
    location /diagram {
        index index.html;
        alias /usr/protei/Protei_TRACER_FE;
    }
    location /audit {
        index index.html;
        alias /usr/protei/Protei_TRACER_FE;
    }
}
```
#2
```nginx
server {
    listen 80;

    server_name _;

    return 301 https://$host$request_uri;
}
    
server {
    listen 192.168.126.21:8081;

    server_name _;

    return 301 https://$host$request_uri;
}
    
server {
    listen 192.168.126.21:8083;

    server_name _;

    return 301 https://$host$request_uri;
}
    
server {
    listen 443 ssl;
    server_name localhost;
    #root /var/www/ROOT;
    #index index.html;

    access_log /var/log/nginx/apache-access.log;
    error_log /var/log/nginx/apache-error.log;
            
    location / {
            proxy_set_header    X-Forwarded-Host $host;
            proxy_set_header    X-Forwarded-Server $host;
            proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
            root /var/www/ROOT;
            index index.html;
    }

   location /pbill.admin {
            proxy_set_header    X-Forwarded-Host $host;
            proxy_set_header    X-Forwarded-Server $host;
            proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass          http://localhost:8081/pbill.admin_ie;

            proxy_buffering off;
            proxy_store     off;

            proxy_connect_timeout 120;
            proxy_send_timeout    120;
            proxy_read_timeout    120;

    }

    location /pbill.comp {
            proxy_set_header    X-Forwarded-Host $host;
            proxy_set_header    X-Forwarded-Server $host;
            proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass          http://localhost:8083/pbill.comp;

            proxy_buffering off;
            proxy_store     off;

            proxy_connect_timeout 120;
            proxy_send_timeout    120;
            proxy_read_timeout    120;
            
    }
            
    ssl_certificate      /etc/nginx/ssl_keys/certificate.crt;
    ssl_certificate_key  /etc/nginx/ssl_keys/key-decrypted.key;
    ssl_protocols        SSLv2 SSLv3 TLSv1 TLSv1.1 TLSv1.2;
}
```