# Kuehne + Nagel Technical Task: Contact List

## Overview

- Parsed the `people.csv` file at startup and store it in H2 in-memory database
- For parsing the csv file, I tried first using `Jackson for CSV`, but it didn't work because the data inside the file are not well-formatted. For example, the name sometimes contains a comma, anf the whole name is not escaped using double quotes or slash.
  - So I built my own simple csv parser
- Did not implement any kind of security on the application, as it's out of scope and not required. But ideally, we should at least use JWT-token based authentication. Other alternatives like OAuth2 might add more if we're deploying our app as a microservice in a bigger application.
- Implemented only one API to retrieve contact list with search and pagination: 
  - `/api/v0/contacts?q={searchByNme}&page={0-pageIndex}&size={pageSize}&sort={sortField,dire}`
  - Other CRUD operations are not in the scope of this assignment, but they should be straight-forward to implement at the backend.
- I used `Angular 14` + `Angular Material` for UI components, to give pretty and clean UI/UX


## How to run the application?

---


### BE
#### Prerequisites:
- **JDK** 17

#### Running Backend:
> Go to `contactlist-server` directory from Command Line
> - Run `./mvnw spring-boot:run`
---
### FE

#### Prerequisites:
- **NPM** - v8.5.5
- **Node** - v16.15.0
- **Angular CLI** - v14.1.1

#### Running Frontend:
> Go to `contactlist-frontend` directory from Command Line
>- Run `npm install`, then
>- Run `ng serve`
>- From browser, navigate to: `localhost:4200`
