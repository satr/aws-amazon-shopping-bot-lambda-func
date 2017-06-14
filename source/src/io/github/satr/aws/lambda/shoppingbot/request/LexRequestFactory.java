package io.github.satr.aws.lambda.shoppingbot.request;

import io.github.satr.aws.lambda.shoppingbot.entity.BakeryDepartment;
import io.github.satr.aws.lambda.shoppingbot.entity.MilkDepartment;

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
        if (slots != null) {
            switch (request.getIntentName()) {
                case BakeryDepartment.IntentName:
                    request.setProduct((String) slots.get(BakeryDepartment.Slot.Product));
                    request.setAmount((String) slots.get(BakeryDepartment.Slot.Amount));
                    request.isSet(true);
                    break;
                case MilkDepartment.IntentName:
                    request.setProduct((String) slots.get(MilkDepartment.Slot.Product));
                    request.setAmount((String) slots.get(MilkDepartment.Slot.Amount));
                    request.isSet(true);
                    break;
            }
        }
    }
}
