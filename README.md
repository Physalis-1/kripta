# алгоритм cast-128 с режимом CBC
* Программа состоит из клиентской и серверной части.
* В качестве серверной часты выступает сервер, который содержит 2 таблицы: первая с логином и паролем, вторая с содержит логин, хэш хэша первых 64000 бит шифрованного файла, два сгенерированных ключа, вектор инициализации, размер остатка в блоке и длинну изначального ключа.  
* В качестве клиентской часты выступает программа предоставляющая интерфейс и возможности для регистрации/авторизации, шифрования/дешифрования файлов.
* Ключ для генерации двух ключей на полях генерируется с помощью библиотеки для генерации ключей aes, а затем обрезается до нужной длины.
* для расшифровывания файла, в БД отправляются, после авторизации логин и хэш хэша первых 64000 бит шифрованного файла
* Длина ключа 5-16 символов(что соответтвутует 40-128 битам), длина по умолчанию равна 5.
* Язык написания клиентской части Java, серверной Python.
* Запуск криптосистемы на данном этапе осуществляется через Main.java, запуск сервера с бд через server.py
***
* Вид интерфейса шифрования  
![изображение](https://user-images.githubusercontent.com/84348788/141312583-7d555894-0caf-4f1e-b652-d789eb355d5c.png)
  
* Вид интерфейса расшифровывания  
![изображение](https://user-images.githubusercontent.com/84348788/141312803-945e08fa-64fc-410d-8d07-46398a781959.png)
   
* Пример выбора файла    
![изображение](https://user-images.githubusercontent.com/84348788/141312950-b393f401-4141-4440-aaaf-abc00f966b4f.png)
  
* Выбор длины ключа  
![изображение](https://user-images.githubusercontent.com/84348788/141312963-5a8b0d6f-1628-481a-855b-d3ea1476151f.png)
  
* В случае успешного шифрования/дешифрования появляется сообщение об успехе  
![изображение](https://user-images.githubusercontent.com/84348788/141313692-f78ab866-aabe-4c99-92b7-a40103d15384.png)  

* Интерфейс сервера   
![изображение](https://user-images.githubusercontent.com/84348788/144584746-bf970947-0ded-4c60-adaa-7febb9728afa.png)



