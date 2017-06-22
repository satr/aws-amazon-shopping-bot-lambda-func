package common;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
//import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;

import java.util.LinkedHashMap;

public class ObjectMother {

    public final static String shoppingBotName = "ShoppingBot";

    public static LinkedHashMap<String, Object> createRequestMap(String department, String productSlotName, String product,
                                                                 String amountSlotName, String amount,
                                                                 String unitSlotName, String unit) {
        LinkedHashMap<String, Object> botMap = new LinkedHashMap<>();
        botMap.put("name", shoppingBotName);
        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("bot", botMap);
        LinkedHashMap<String, Object> currentIntentMap = new LinkedHashMap<>();
        currentIntentMap.put("confirmationStatus", "None");
        currentIntentMap.put("name", department);
        LinkedHashMap<String, Object> slotsMap = new LinkedHashMap<>();
        slotsMap.put(productSlotName, product);
        slotsMap.put(amountSlotName, amount);
        slotsMap.put(unitSlotName, unit);
        currentIntentMap.put("slots", slotsMap);
        requestMap.put("currentIntent", currentIntentMap);
        return requestMap;
    }

    public static AmazonDynamoDB createInMemoryDb() {
        AmazonDynamoDB dynamodb = null;
        try {
            // Create an in-memory and in-process instance of DynamoDB Local
            AmazonDynamoDBLocal amazonDynamoDBLocal = DynamoDBEmbedded.create();
            dynamodb = amazonDynamoDBLocal.amazonDynamoDB();
            return dynamodb;
        } catch (Exception e){
            if(dynamodb != null)
                dynamodb.shutdown();// Shutdown the thread pools in DynamoDB Local / Embedded
        }
        return dynamodb;
    }
}
