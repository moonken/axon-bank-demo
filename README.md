# axon-bank-demo

##pre run

    docker run --name axon-bank-demo -d mongo
    
## RUN

    ./mvnw spring-boot:run


### Simple test
        curl -i -X POST -H "Content-Type:application/json" -d '{"amount": 1}' localhost:8080/api/account/4321/action/withdraw-money