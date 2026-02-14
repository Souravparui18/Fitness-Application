# Fitness-Application
Project

Backend REST API for a fitness tracking app: user auth (JWT), activity logging, and personalized recommendations. Built with Spring Boot 3, Spring Security, JPA, and MySQL.
Medium (bullet points)
Fitness Monolith — Spring Boot 3.2 REST API for fitness tracking and recommendations.
Auth & security: Registration, login, and JWT-based stateless authentication; role-based access (USER/ADMIN); BCrypt password encoding; Spring Security filter chain.
Activity management: CRUD for workouts with types (Running, Walking, Cycling, Swimming, Weight Training, Yoga, HIIT, etc.), duration, calories, timestamps, and JSON metrics.
Recommendations: Generate and store recommendations per user/activity with improvements, suggestions, and safety notes; query by user or activity.
Data layer: Spring Data JPA, MySQL, UUID primary keys, JSON columns for flexible metrics and list fields; global validation and exception handling.
Tech stack: Java 21, Spring Boot (Web, Data JPA, Security, Validation), JJWT, Lombok, MySQL.
