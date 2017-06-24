package io.github.satr.aws.lambda.shoppingbot.request;

import io.github.satr.aws.lambda.shoppingbot.intent.BakeryDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.GreetingsIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.MilkDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.intent.VegetableDepartmentIntent;
import io.github.satr.aws.lambda.shoppingbot.request.strategies.intentloading.*;

import java.util.HashMap;
import java.util.Map;

public class LexRequestFactory {


    private final static Map<String, IntentLoaderStrategy> intentLoaderStrategies = new HashMap<>();
    private final static IntentLoaderStrategy unsupportedIntentLoaderStrategy = new UnsupportedIntentLoaderStrategy();

    static {
        intentLoaderStrategies.put(GreetingsIntent.Name, new GreetingsIntentLoadingStrategy());
        intentLoaderStrategies.put(BakeryDepartmentIntent.Name, new BakeryDepartmentIntentLoadingStrategy());
        intentLoaderStrategies.put(MilkDepartmentIntent.Name, new MilkDepartmentIntentLoadingStrategy());
        intentLoaderStrategies.put(VegetableDepartmentIntent.Name, new VegetableDepartmentIntentLoadingStrategy());
    }

    public static LexRequest createFromMap(Map<String, Object> input) {
        LexRequest request = new LexRequest();
        if(input == null)
            return request;

        loadSessionAttributes(input, request);
        loadBotName(input, request);

        Map<String, Object> currentIntent = loadCurrentIntent(input);
        if (currentIntent != null)
            loadIntentParameters(currentIntent, request);

        return request;
    }

    private static void loadSessionAttributes(Map<String, Object> input, LexRequest request) {
        Map<String, Object> sessionAttrs = (Map<String, Object>) input.get(LexRequestAttr.SessionAttributes);
        if (sessionAttrs != null)
            request.setSessionAttributes(sessionAttrs);
    }

    private static void loadIntentParameters(Map<String, Object> currentIntent, LexRequest request) {
        request.setConfirmationStatus((String) currentIntent.get(LexRequestAttr.ConfirmationStatus));
        request.setIntentName((String) currentIntent.get(LexRequestAttr.CurrentIntentName));
        request.setInvocationSource((String) currentIntent.get(LexRequestAttr.InvocationSource));
        request.setOutputDialogMode((String) currentIntent.get(LexRequestAttr.OutputDialogMode));

        loadIntentSlots(currentIntent, request);
    }

    private static Map<String, Object> loadCurrentIntent(Map<String, Object> input) {
        return (Map<String, Object>) input.get(LexRequestAttr.CurrentIntent);
    }

    private static void loadBotName(Map<String, Object> input, LexRequest request) {
        Map<String, Object> bot = (Map<String, Object>) input.get(LexRequestAttr.Bot);
        if (bot != null)
            request.setBotName((String) bot.get(LexRequestAttr.BotName));
    }

    private static void loadIntentSlots(Map<String, Object> currentIntent, LexRequest request) {
        IntentLoaderStrategy strategy = getIntentLoadingStrategyBy(request.getIntentName());
        Map<String, Object> slots = (Map<String, Object>) currentIntent.get(LexRequestAttr.Slots);
        strategy.load(request, slots);
    }

    private static IntentLoaderStrategy getIntentLoadingStrategyBy(String intentName) {
        return intentLoaderStrategies.containsKey(intentName)
                ? intentLoaderStrategies.get(intentName)
                : unsupportedIntentLoaderStrategy;
    }

}
