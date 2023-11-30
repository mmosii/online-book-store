<p align="center">
  <img src="https://i.postimg.cc/Y0nJH8yf/1.jpg" alt="Alt Text">
</p>

# Welcome to NovelNook Online Bookstore 📖✨

Step into our online bookstore, a sanctuary born from a love for literature and a commitment to redefine the reading experience. Fueled by the desire to blend the traditional bookstores with modern convenience, our project addresses the need for an accessible store where the joy of discovering new worlds aligns with the pace of contemporary life.

Join us on a digital journey where pages transform into portals, inviting you to **explore, imagine, and discover** the timeless magic held within the covers of each book.


## Project Technologies

<div align="center">

<div style="display: inline-block; text-align: left; width: 22%; margin: 0 1.5%;">


#### Java 17 [<img src="https://www.oracle.com/a/tech/img/cb88-java-logo-001.jpg" height="20" alt="Java Logo">](https://www.oracle.com/java/)


#### Spring Boot 3.1.4 [<img src="https://pbs.twimg.com/profile_images/1235868806079057921/fTL08u_H_400x400.png" height="20" alt="Spring Boot Logo">](https://spring.io/projects/spring-boot)


#### Spring Security 6.1.4 [<img src="https://pbs.twimg.com/profile_images/1235983944463585281/AWCKLiJh_400x400.png" height="20" alt="Spring Security Logo">](https://spring.io/projects/spring-security)


#### Spring Data JPA 3.1.4 [<img src="https://pbs.twimg.com/profile_images/1235945452304031744/w55Uc_O9_400x400.png" height="20" alt="Spring Data JPA Logo">](https://spring.io/projects/spring-data-jpa)

</div>

<div style="display: inline-block; text-align: left; width: 22%; margin: 0 1.5%;">

#### Lombok 1.18.3 [<img src="https://blog.kakaocdn.net/dn/bA0QdM/btqQCzxS7vv/RTB3bbZsu7EMKPBefuTn80/img.jpg" height="20" alt="Lombok Logo">](https://projectlombok.org/)

#### MapStruct 1.5.5 [<img src="https://mapstruct.github.io/mapstruct.org.new/images/favicon.ico" height="20" alt="MapStruct Logo">](https://www.mapstruct.org/)

#### Liquibase 4.2.0 [<img src="https://dashboard.snapcraft.io/site_media/appmedia/2020/08/liquibase.jpeg.png" height="20" alt="Liquibase Logo">](https://www.liquibase.org/)

#### MySQL 8.0.33 [<img src="https://www.mysql.com/common/logos/logo-mysql-170x115.png" height="20" alt="MySQL Logo">](https://www.mysql.com/)

</div>

<div style="display: inline-block; text-align: left; width: 22%; margin: 0 1.5%;">

#### Maven 3.11.0 [<img src="https://maven.apache.org/images/maven-logo-black-on-white.png" height="20" alt="Maven Logo">](https://maven.apache.org/)

#### Swagger 2.1.0 [<img src="https://seeklogo.com/images/S/swagger-logo-A49F73BAF4-seeklogo.com.png" height="20" alt="Swagger Logo">](https://swagger.io/)

#### Docker 3.8 [<img src="https://cdn4.iconfinder.com/data/icons/logos-and-brands/512/97_Docker_logo_logos-512.png" height="20" alt="Docker Logo">](https://www.docker.com/)

#### AWS [<img src="https://logowik.com/content/uploads/images/aws-amazon-web-services.jpg" height="20" alt="AWS Logo">](https://aws.amazon.com/)

</div>

</div>

## Class Diagram
![Class Diagram](https://i.postimg.cc/d0sBrfmq/class-diagram.jpg)

## Endpoints

### Endpoints for Registration and Authentication for Users

| Method | Endpoint               | Description                                           | Role   |
|--------|------------------------|-------------------------------------------------------|--------|
| POST   | /api/auth/register     | Register new user by providing required details       | Anyone |
| POST   | /api/auth/login        | Authenticate user by email and password, returning JWT token if valid credentials | Anyone |

### Endpoints for Managing Book Categories

| Method | Endpoint                        | Description                                           | Role   |
|--------|---------------------------------|-------------------------------------------------------|--------|
| GET    | /api/categories/{id}            | Get a category by id, if it exists                     | User   |
| PUT    | /api/categories/{id}            | Update a category by id, if it exists                  | Admin  |
| DELETE | /api/categories/{id}            | Delete a category by id, if it exists                  | Admin  |
| GET    | /api/categories/                | Get a list of all available categories                | User   |
| POST   | /api/categories/                | Create a new category                                  | Admin  |
| GET    | /api/categories/{id}/books      | Get a list of books that belong to the category by id  | User   |

### Endpoints for Managing Books

| Method | Endpoint               | Description                                           | Role   |
|--------|------------------------|-------------------------------------------------------|--------|
| POST   | /api/books              | Create a new book                                     | Admin  |
| GET    | /api/books/{id}         | Get a book by id, if it exists                         | User   |
| PUT    | /api/books/{id}         | Update a book by id, if it exists                      | Admin  |
| DELETE | /api/books/{id}         | Delete a book by id, if it exists                      | Admin  |
| GET    | /api/books              | Get a list of all available books                     | User   |
| GET    | /api/books/search       | Get a list of books within specified parameters       | User   |

### Endpoints for Managing User's Shopping Carts

| Method | Endpoint                        | Description                                           | Role   |
|--------|---------------------------------|-------------------------------------------------------|--------|
| POST   | /api/cart                       | Add a book to the shopping cart                        | User   |
| GET    | /api/cart                       | Retrieve user's shopping cart                         | User   |
| PUT    | /api/cart/cart-items/{cartItemId} | Update quantity of a book in the shopping cart       | User   |
| DELETE | /api/cart/cart-items/{cartItemId} | Remove a book from the shopping cart                  | User   |

### Endpoints for Managing User's Orders

| Method | Endpoint                        | Description                                           | Role   |
|--------|---------------------------------|-------------------------------------------------------|--------|
| GET    | /api/orders                     | Get a list of all user's previous orders              | User   |
| POST   | /api/orders                     | Create an order from user's current shopping cart items | User   |
| PATCH  | /api/orders/{id}                | Change the status of an order by id                    | Admin  |
| GET    | /api/orders/{orderId}/items     | Retrieve all OrderItems for a specific order          | User   |
| GET    | /api/orders/{orderId}/items/{id}| Retrieve a specific OrderItem within an order         | User   |

## Demonstration

For a demonstration of the application, you can watch the following Loom video:

[Watch the Demo Video](https://www.loom.com/share/6dcd8f4ae1ac490085bd2267f02c284b?sid=8ec8a819-528f-4206-bfbe-2116ff6a6033)

## Quick Start

1. **Install Docker:**
   [Install Docker](https://docs.docker.com/get-docker/)

2. **Clone this repository.**

3. **Create a .env file in the root of the project.
Use .env.sample as a reference.
Add necessary environment variables.**
4. **Build the application:**
   ```bash
   mvn clean package
5. **Build and start the Docker containers:**
   ```bash
   docker-compose build && docker-compose up
   
  The application should be running locally at http://localhost:8082.
  
6. **Test with Swagger:
  Open http://localhost:8082/swagger-ui/index.html in your browser.**
## Using Postman Requests Collection

1. Download and install [Postman](https://www.postman.com/downloads/).
2. Open Postman and press CTRL+O (or open File -> Import).
3. Paste the following URL in the input field:
   [Postman Collection](https://api.postman.com/collections/30516355-be9538e3-0e7e-49e5-b912-057c3dd5d7e1?access_key=PMAT-01HG6AA95ZB7WVB39K6X4416S4)

4. Create environment variables in Postman:
    - `port`: Set the value to `8081` (for Docker).
    - `user-token`: Paste the user token obtained from the response in the login endpoint.
    - `admin-token`: Paste the admin token obtained from the response in the login endpoint.

## Test with Swagger on AWS

Access the AWS deployment at [http://ec2-107-20-71-245.compute-1.amazonaws.com/swagger-ui/index.html](http://ec2-107-20-71-245.compute-1.amazonaws.com/swagger-ui/index.html).

If testing admin endpoints, use the following credentials:

- **Email:** admin@gmail.com
- **Password:** 222111
