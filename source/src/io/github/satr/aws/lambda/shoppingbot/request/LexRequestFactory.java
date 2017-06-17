package io.github.satr.aws.lambda.shoppingbot.request;

import io.github.satr.aws.lambda.shoppingbot.entity.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.entity.MilkDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.entity.VegetableDepartmentIntent;

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
            case BakeryDepartmentIntent.Name:
                readSlots(request, slots, BakeryDepartmentIntent.Slot.Product, BakeryDepartmentIntent.Slot.Amount, BakeryDepartmentIntent.Slot.Unit);
                break;
            case MilkDepartmentIntent.Name:
                readSlots(request, slots, MilkDepartmentIntent.Slot.Product, MilkDepartmentIntent.Slot.Amount, MilkDepartmentIntent.Slot.Unit);
                break;
            case VegetableDepartmentIntent.Name:
                readSlots(request, slots, VegetableDepartmentIntent.Slot.Product, VegetableDepartmentIntent.Slot.Amount, VegetableDepartmentIntent.Slot.Unit);
                break;
            default:
                request.setError("Requested department is not recognized.");
        }
    }

    private static void readSlots(LexRequest request, Map<String, Object> slots, String productSlotName, String amountSlotName, String unitSlotName) {
        request.setProduct(getSlotValueFor(slots, productSlotName, null));
        request.setRequestedAmount(getSlotValueFor(slots, amountSlotName, null));
        request.setRequestedUnit(getSlotValueFor(slots, unitSlotName, null));
    }

    private static String getSlotValueFor(Map<String, Object> slots, String productSlotName, String defaultValue) {
        String slotValue = (String)slots.get(productSlotName);
        return slotValue != null ? slotValue : defaultValue;
    }
}
