package common;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
//import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ObjectMother {

    public final static String shoppingBotName = "ShoppingBot";

    public static Map<String, Object> createMapFromJson(String jsonFilePath){
        ClassLoader classLoader = ObjectMother.class.getClassLoader();
        File jsonFile = new File(classLoader.getResource(jsonFilePath).getFile());
        if(!jsonFile.exists()) {
            System.out.println("File not found: " + jsonFilePath);
            return new HashMap<>();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> dataMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {
            });
            return dataMap;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    public static LinkedHashMap<String, Object> createRequestMap(String intentName, String productSlotName, String product,
                                                                 String amountSlotName, String amount,
                                                                 String unitSlotName, String unit) {
        LinkedHashMap<String, Object> botMap = new LinkedHashMap<>();
        botMap.put("name", shoppingBotName);
        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("bot", botMap);
        LinkedHashMap<String, Object> currentIntentMap = new LinkedHashMap<>();
        currentIntentMap.put("confirmationStatus", "None");
        currentIntentMap.put("name", intentName);
        LinkedHashMap<String, Object> slotsMap = new LinkedHashMap<>();
        slotsMap.put(productSlotName, product);
        slotsMap.put(amountSlotName, amount);
        slotsMap.put(unitSlotName, unit);
        currentIntentMap.put("slots", slotsMap);
        requestMap.put("currentIntent", currentIntentMap);
        return requestMap;
    }

    public static LinkedHashMap<String, Object> createRequestForBakeryDepartment(String product, String amount, String unit) {
        return ObjectMother.createRequestMap(BakeryDepartmentIntent.Name,
                BakeryDepartmentIntent.Slot.Product, product,
                BakeryDepartmentIntent.Slot.Amount, amount,
                BakeryDepartmentIntent.Slot.Unit, unit);
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

    public static String setSessionAttributeFromRundomString(Map<String, Object> requestMap, String sessionAttributeName) {
        Map<String, Object> sessionAttrsMap = createSessionAttribute(requestMap, sessionAttributeName, createRandomString());
        return (String) sessionAttrsMap.get(sessionAttributeName);
    }

    private static Map<String, Object> createSessionAttribute(Map<String, Object> requestMap, String sessionAttributeName, Object value) {
        Map<String, Object> sessionAttrsMap = (Map<String, Object>) requestMap.get(LexRequestAttr.SessionAttributes);
        if(sessionAttrsMap == null) {
            sessionAttrsMap = new LinkedHashMap<>();
            requestMap.put(LexRequestAttr.SessionAttributes, new LinkedHashMap<String, Object>());
        }
        sessionAttrsMap.put(sessionAttributeName, value);
        return sessionAttrsMap;
    }

    public static String createRandomString() {
        return UUID.randomUUID().toString();
    }
}
