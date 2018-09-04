# cash-register-exam
Interactive Console Command Line Application (Spring Boot + Spring Shell)

BUILDING BLOCKS:  
-Spring Boot  
-Spring Shell  
-Maven  

HOW TO RUN APPLICATION:

Using Maven:  
   `> mvn clean install`  
   `> mvn spring-boot:run`
   
Note: shell:> prompt will be displayed when application has fully started

COMMANDS:

1. Show current status:  
  `shell:> show`
  
2. Add bills to register:  
   `shell:> put 5,5,5,5,5`  
   `shell:> put "5, 5, 5,5 , 5"`

NOTE: For simplicity purposes, comma delimiter is mandatory to automatically load to an int array parameter

3. Remove bills from register:  
    `shell:> take 5,5,5,5,5`  
    `shell:> take "5, 5, 5,5 , 5"`
    
4. Dispense bills for a change amount:  
    shell:> change 87
    
5. `quit` and `exit` will shutdown the Spring Boot application.
    
    NOTE: `quit` and `exit` are reserved commands for Spring Shell and therefore can not be used as user command.
