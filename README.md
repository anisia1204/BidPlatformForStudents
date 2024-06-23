https://github.com/anisia1204/StudentCollab
# StudentCollab
Aplicație web pentru colaborarea între studenți
## Tehnologii necesare

Pentru a rula acest proiect este necesară instalarea următoarelor tehnologii:

- IntelliJ IDEA
- PostgreSQL
- Java 19
- Maven
- Node.js v18 și npm

> **Notă**: La instalarea PostgreSQL, este necesară setarea parolei utilizatorului `postgres` ca `anisia` pentru a evita modificarea fișierului `application.properties` din backend-ul aplicației.

## Configurare PostgreSQL

Aplicația folosește baza de date implicită a PostgreSQL. Este important ca nota de mai sus să fie respectată pentru a evita pași suplimentari.

## Instalare și Rulare

### 1. Rularea backend-ului

1. În proiectul deschis în IntelliJ IDEA se caută clasa `StudcollabApplication`.
2. Se rulează această clasă pentru a porni serverul. Proiectul se va construi implicit la rularea clasei, deci nu este necesară construirea sa separată.

### 2. Configurarea și rularea frontend-ului

1. Se caută fișierul `package.json` în directorul proiectului.
2. Se deschide terminalul în folderul părinte al acestui fișier.
3. Se rulează următoarele comenzi:
    ```bash
    npm install
    ```
4. După instalare, se rulează frontend-ul cu ajutorul comenzii:
    ```bash
    npm start
    ```
   > Aceasta comandă va rula linia "start": "ng serve" din fișierul `package.json`.