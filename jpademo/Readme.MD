Plan
1. Create a STS Spring Project -- jpademo (jdk 21, starter, maven)
                - JPA Web Lombok MySQL Validator Thymeleaf
2. src-main-java
                    -resources
                         application.properties(MySQL Details)
         -test-java
3. model package
4. Student Class (variables)
         variables
         annotated
         constructors (all fields except id, no arg constructors)
         getter setter (all fields)
         tostring
         equal (based on matricid)
5. repo package
6. Interface called StudentRepository (extend JPARepository)
7. create a resources folder in test package
8. application.properties (Test MySQL Details)
9. In my test/java create a JPATest (StudentTest)
10. Run the server
11. Run the JUnit Test Case
12. Happily going for tea
