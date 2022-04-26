## customer-rewards
 A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.
 A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every
 dollar spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
 Given a record of every transaction during a three month period, calculate the reward points earned for
 each customer per month and total.
 
## Requirements
For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher
- [Maven 3](https://maven.apache.org)
- Git
- Curl or Postman to execute the Rest-API

## Building the Project
Clone the project and use maven to build the server.
- $ mvn -f "pathtoPomFile" clean install

   Where "pathtoPomFile"  is the path to the POM file.
   It generates the Jar file which is located in the folder target/rewards-0.0.1-SNAPSHOT.jar
	
## Launch the Application
Execute the following command to launch the application

$ Java -jar <pathtoproject>/target/rewards-0.0.1-SNAPSHOT.jar
	
application will be running on the port 8080.

## API
   purchases
	  API used to post the transactions made by the customers. will be used to setup the Test data for the application.
	
	Domain : http://localhost:8080
	URI : /api/purchases
	Method : POST
	Headers:
	    Content-Type : application/JSON
	
	Body :
	   [{
    "customerId":"C001", // customer Id
    "amount":"120.30",  // Amount
    "transactionDate":"04/25/2022" //purchased Date }]
	
	
  Purchases
    Read the transactions uploaded
	
	Domain : http://localhost:8080
	URI : /api/purchases
	Method : GET
	Headers:
	    Accept : application/JSON
	
	Response :
	    A JSON Array of the purchases made by the customers.
   Sample Output
    [{
 "Id" : "1" // unique identifier for the record.
    "customerId":"C001", // customer Id
    "amount":"120.30",  // Amount
    "transactionDate":"04/25/2022" //purchased Date}]	

# Rewards
	  
	Compute the rewards based on the purchases made by the customer.
	
	Domain : http://localhost:8080
	URI : /api/rewards/{customerId}
	Method : GET
	Headers:
	    Accept : application/JSON
	
	Response:
	    A JSON Object with the details - total rewards and rewards for each month.
	sample response:
	   {
    "customerId": "C001",// customer ID
    "total": 1309.8,  // total rewards
    "mothlyRewards": { // rewards for each month.
        "MARCH": 150.4, 
        "FEBRUARY": 22.599999999999994,
        "APRIL": 1136.8}}

Test Data : 
   To upload the purchases
	
Use curl or the Postman to execute the REST-Api.
	
	
[{
    "customerId":"C001",
    "amount":"120.30",
    "transactionDate":"04/25/2022"
},{
    "customerId":"C001",
    "amount":"598.10",
    "transactionDate":"04/20/2022"
},{
    "customerId":"C001",
    "amount":"150.00",
    "transactionDate":"03/10/2022"
},{
    "customerId":"C001",
    "amount":" 50.00",
    "transactionDate":"03/30/2022"
},{
    "customerId":"C001",
    "amount":"50.40",
    "transactionDate":"03/05/2022"
},{
    "customerId":"C001",
    "amount":"72.60",
    "transactionDate":"02/10/2022"
},{
    "customerId":"C002",
    "amount":"117.30",
    "transactionDate":"04/25/2022"
},{
    "customerId":"C002",
    "amount":"99.30",
    "transactionDate":"04/15/2022"
},{
    "customerId":"C002",
    "amount":"1017.30",
    "transactionDate":"03/20/2022"
},{
    "customerId":"C002",
    "amount":"315.99",
    "transactionDate":"02/25/2022"
},{
    "customerId":"C002",
    "amount":"119.67",
    "transactionDate":"03/05/2022"
},
{
    "customerId":"C002",
    "amount":"617.39",
    "transactionDate":"02/28/2022"
}]
 
	
	



