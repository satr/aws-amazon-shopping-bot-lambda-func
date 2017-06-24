package io.github.satr.aws.lambda.shoppingbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactoryImpl;
import io.github.satr.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestFactory;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ShoppingBotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    private ShoppingBotProcessor shoppingBotProcessor;

    public ShoppingBotLambda() {
        this(new ShoppingBotProcessor(new RepositoryFactoryImpl()));
    }

    public ShoppingBotLambda(ShoppingBotProcessor shoppingBotProcessor) {
        this.shoppingBotProcessor = shoppingBotProcessor;
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            return shoppingBotProcessor.Process(lexRequest);
        } catch (Exception e) {
            logStackTrace(context, e);
            return LexResponseHelper.createFailedLexResponse("Error: " + e.getMessage(), lexRequest);
        }
    }

    private void logStackTrace(Context context, Exception e) {
        if(context == null || context.getLogger() == null)
        {
            e.printStackTrace();
            return;
        }
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            context.getLogger().log(sw.toString());
        } finally {
            if(pw != null)
                pw.close();
        }
    }
}

