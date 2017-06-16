package io.github.satr.aws.lambda.shoppingbot.request;

import io.github.satr.aws.lambda.shoppingbot.entity.BakeryDepartment;
import io.github.satr.aws.lambda.shoppingbot.entity.MilkDepartment;
import io.github.satr.aws.lambda.shoppingbot.entity.VegetableDepartment;

import java.util.Map;

public class LexRequestFactory {
    public final class Property {
        public static final String CurrentIntent = "currentIntent";
        public static final String CurrentIntentName = "name";
        public static final String ConfirmationStatus = "confirmationStatus";
        public static final String Slots = "slots";
        public static final String InvocationSource = "invocationSource";
        public static final String OutputDialogMode = "outputDialogMode";
    }

    public static LexRequest readFromMap(Map<String, Object> input) {
        LexRequest request = new LexRequest();
        if(input == null)
            return request;

        Map<String, Object> bot = (Map<String, Object>) input.get("bot");
        if (bot != null) {
            String name = (String) bot.get("name");
            request.setBotName(name);
        }

        Map<String, Object> currentIntent = (Map<String, Object>) input.get(Property.CurrentIntent);
        if (currentIntent != null) {
            request.setConfirmationStatus((String) currentIntent.get(Property.ConfirmationStatus));
            request.setIntentName((String) currentIntent.get(Property.CurrentIntentName));
            request.setInvocationSource((String) currentIntent.get(Property.InvocationSource));
            request.setOutputDialogMode((String) currentIntent.get(Property.OutputDialogMode));
            readRequestedProduct(currentIntent, request);
        }
        return request;
    }

    private static void readRequestedProduct(Map<String, Object> currentIntent, LexRequest request) {
        Map<String, Object> slots = (Map<String, Object>) currentIntent.get(Property.Slots);
        if (slots == null)
            return;
        switch (request.getIntentName()) {
            case BakeryDepartment.IntentName:
                readSlots(request, slots, BakeryDepartment.Slot.Product, BakeryDepartment.Slot.Amount);
                break;
            case MilkDepartment.IntentName:
                readSlots(request, slots, MilkDepartment.Slot.Product, MilkDepartment.Slot.Amount);
                break;
            case VegetableDepartment.IntentName:
                readSlots(request, slots, VegetableDepartment.Slot.Product, VegetableDepartment.Slot.Amount);
                break;
            default:
                request.setError("Requested department is not recognized.");
        }
    }

    private static void readSlots(LexRequest request, Map<String, Object> slots, String productSlotName, String amountSlotName) {
        String productSlot = (String)slots.get(productSlotName);
        if (productSlot == null)
            return;
        request.setProduct(productSlot);
        String requestedAmount = (String) slots.get(amountSlotName);
        if (requestedAmount == null)
            return;
        request.setRequestedAmount(requestedAmount);
    }
}
