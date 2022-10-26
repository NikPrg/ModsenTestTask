Docker Compose usage:
1) Download project from GitHub
2) Lets clean our project by using Maven:
 - Find the Maven tab in the upper right corner
 - Then, go to Lifecycle folder
 - Click on 'clean' button
 - Then, right click on 'install' button(we need to unnable tests)
 - Click on 'Modify options'
 - Check the box next to 'Skip tests' label
 - Close Lifecycle folder
 - Click on 'Run Configurations' button
3) Go to terminal and use the following command:
 - docker build -t modsen/event-service:0.0.1-SNAPSHOT .
 - docker-compose up 
