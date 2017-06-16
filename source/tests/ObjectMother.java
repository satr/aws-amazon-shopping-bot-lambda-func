import java.util.LinkedHashMap;

public class ObjectMother {

    public final static String shoppingBotName = "ShoppingBot";

    public static LinkedHashMap<String, Object> createRequestMap(String department, String productSlotName, String product, String amountSlotName, String amount) {
        LinkedHashMap<String, Object> botMap = new LinkedHashMap<>();
        botMap.put("name", shoppingBotName);
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
    }
}
