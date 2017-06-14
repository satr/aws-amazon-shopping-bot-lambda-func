package io.github.satr.aws.lambda.shoppingbot;

import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;

import java.util.LinkedHashMap;

public class Main {


    public static void main(String[] args) {
        LinkedHashMap<String, Object> requestMap = createRequestMap("BakeryDepartment", "BakeryProduct", "bread", "Amount", "2");
        LexResponse a = new ShoppingBotLambda().handleRequest(requestMap, null);
        System.out.println(a.getDialogAction().getMessage().getContent());
    }

    private static LinkedHashMap<String, Object> createRequestMap(String department, String productSlotName, String product, String amountSlotName, String amount) {
        LinkedHashMap<String, Object> botMap = new LinkedHashMap<>();
        botMap.put("name", "ShoppingBot");
        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("bot", botMap);
        LinkedHashMap<String, Object> currentIntentMap = new LinkedHashMap<>();
        currentIntentMap.put("confirmationStatus", "None");
        currentIntentMap.put("name", department);
        LinkedHashMap<String, Object> slotsMap = new LinkedHashMap<>();
        slotsMap.put(amountSlotName, amount);
        slotsMap.put(productSlotName, product);
        currentIntentMap.put("slots", slotsMap);
        requestMap.put("currentIntent", currentIntentMap);
        return requestMap;
    }}
