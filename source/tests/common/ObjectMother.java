package common;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satr.aws.lambda.shoppingbot.entities.*;
import io.github.satr.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intents.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import testdata.FileNames;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class ObjectMother {

    public final static String shoppingBotName = "ShoppingBot";
    public final static Random random = new Random();

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

    public static LinkedHashMap<String, Object> createRequestForBakeryDepartment(String product, Double amount, String unit) {
        return createRequestFor(BakeryDepartmentIntent.Name, BakeryDepartmentIntent.Slot.Product, product, BakeryDepartmentIntent.Slot.Amount, amount, BakeryDepartmentIntent.Slot.Unit, unit);
    }

    public static LinkedHashMap<String, Object> createRequestFor(String intentName, String productSlotName, String product, String amountSlotName, Double amount, String unitSlotName, String unit) {
        return ObjectMother.createRequestMap(intentName,
                productSlotName, product,
                amountSlotName, amount.toString(),
                unitSlotName, unit);
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

    public static String setSessionAttributeWithRundomString(Map<String, Object> requestMap, String sessionAttributeName) {
        return setSessionAttributeWithString(requestMap, sessionAttributeName, createRandomString());
    }

    private static String setSessionAttributeWithString(Map<String, Object> requestMap, String sessionAttributeName, String value) {
        Map<String, Object> sessionAttrsMap = createSessionAttribute(requestMap, sessionAttributeName, value);
        return (String) sessionAttrsMap.get(sessionAttributeName);
    }

    private static Map<String, Object> createSessionAttribute(Map<String, Object> requestMap, String sessionAttributeName, Object value) {
        Map<String, Object> sessionAttrsMap = (Map<String, Object>) requestMap.get(LexRequestAttribute.SessionAttributes);
        if(sessionAttrsMap == null) {
            sessionAttrsMap = new LinkedHashMap<>();
            requestMap.put(LexRequestAttribute.SessionAttributes, sessionAttrsMap);
        }
        sessionAttrsMap.put(sessionAttributeName, value);
        return sessionAttrsMap;
    }

    public static String createRandomString() {
        return UUID.randomUUID().toString();
    }

    public static Double createRandomNumber() {
        return random.nextDouble();
    }

    public static Map<String, Object> createGreetingsIntentMapWithNamesInSlots(String firstName, String lastName) {
        Map<String, Object> mapFromJson = createMapFromJson(FileNames.LexRequestGreetingsWithNamesJson);
        Map<String, Object> slots = (Map<String, Object>) ((Map<String, Object>) mapFromJson.get(LexRequestAttribute.CurrentIntent))
                                                                                            .get(LexRequestAttribute.Slots);
        slots.put(GreetingsIntent.Slot.FirstName, firstName);
        slots.put(GreetingsIntent.Slot.LastName, lastName);
        return mapFromJson;
    }

    public static void removeSessionAttribute(Map<String, Object> requestMap, String attributeName) {
        Map<String, Object> sessionAttrsMap = (Map<String, Object>) requestMap.get(LexRequestAttribute.SessionAttributes);
        if(sessionAttrsMap == null || !sessionAttrsMap.containsKey(attributeName))
            return;
        sessionAttrsMap.remove(attributeName);
    }

    public static void setSessionAttribute(Map<String, Object> requestMap, String attributeName, String value) {
        Map<String, Object> sessionAttrsMap = (Map<String, Object>) requestMap.get(LexRequestAttribute.SessionAttributes);
        if(sessionAttrsMap == null){
            sessionAttrsMap = new LinkedHashMap<>();
            requestMap.put(LexRequestAttribute.SessionAttributes, sessionAttrsMap);
        };
        sessionAttrsMap.put(attributeName, value);
    }

    public static ShoppingCart createShoppingCart() {
        User user = new User();
        return createShoppingCart(user);
    }

    public static ShoppingCart createShoppingCart(String userId) {
        return createShoppingCart(userId, 2017);
    }

    public static ShoppingCart createShoppingCart(String userId, int year) {
        User user = new User();
        user.setUserId(userId);
        ShoppingCart shoppingCart = createShoppingCart(user);
        shoppingCart.setUpdatedOn(createZonedDateTimeForYear(year).toString());
        return shoppingCart;
    }

    public static ShoppingCart createShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return cart;
    }

    public static ZonedDateTime createZonedDateTimeForYear(int year) {
        return ZonedDateTime.of(year,1, 1,1,1,1,1, ZoneId.of("UTC"));
    }

    public static ShoppingCartItem createShoppingCartItem(String product, Double amount, String unit) {
        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setProduct(product);
        cartItem.setAmount(amount);
        cartItem.setUnit(unit);
        return cartItem;
    }

    public static User createUser() {
        return createUser(createRandomString(), createRandomString());
    }

    public static User createUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public static Product createProduct(String productName, Double price, String unit, String[] unitForms) {
        Product product = new Product();
        product.setProductId(productName);
        product.setUnitPrices(ObjectMother.createUnitPriceList(price, unitForms, unit));
        return product;
    }

    public static Product createProduct() {
        return createProduct(createRandomString());
    }

    public static Product createProduct(String productName) {
        String unit = createRandomString();
        return createProduct(productName, createRandomNumber(), unit, new String[]{unit});
    }

    public static List<UnitPrice> createUnitPriceList(Double price, String[] unitForms, String unit) {
        ArrayList<UnitPrice> unitPrices = new ArrayList<>();
        unitPrices.add(createUnitPrice(price, unit, unitForms));
        return unitPrices;
    }

    public static UnitPrice createUnitPrice(Double price, String unit, String[] unitForms) {
        UnitPrice unitPrice = new UnitPrice();
        unitPrice.setPrice(price);
        unitPrice.setUnit(unit);
        unitPrice.setUnitForms(Arrays.asList(unitForms));
        return unitPrice;
    }

    public static Product createProduct(String productName, String unit, String[] unitForms) {
        return createProduct(productName, createRandomNumber(), unit, unitForms);
    }

    public static ShoppingCartItem createShoppingCartItem(String productName, double amount, double price, String unit) {
        ShoppingCartItem cartItem1 = new ShoppingCartItem();
        cartItem1.setPrice(price);
        cartItem1.setProduct(productName);
        cartItem1.setUnit(unit);
        cartItem1.setAmount(amount);
        return cartItem1;
    }

    public static Order createOrder() {
        Order order = new Order();
        order.setUser(createUser());
        return order;
    }

    public static User createUserWithFacebookId(String facebookUserId) {
        User user = createUser();
        user.setFacebookId(facebookUserId);
        return user;
    }
}
