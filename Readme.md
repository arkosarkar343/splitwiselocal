# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Running the application
* You need to have java 17 & NodeJs present in the system
* Before you run  the application you have to set the following values
  - DB_PWD (can be any string for H2 database)
  - H2_URL (should be jdbc:h2:<any path to local file system>)
  - GRP_ID (Set the group id from Splitwise application which you want to use)
  - APP_TOKEN (Generate the API token from Splitwise application)
    
* To run the backend application execute the following command `java -jar ./target/*.jar --DB_PWD=<password> --GRP_ID=<splitwiseGrpId> --APP_TOKEN=<api_token_splitwise> --H2_URL=jdbc:h2:<db_path>`

* To run the front end React application run  `cd my-app && node start`
* Upload a sample csv file and after each row appears you can send those transactions to splitwise.