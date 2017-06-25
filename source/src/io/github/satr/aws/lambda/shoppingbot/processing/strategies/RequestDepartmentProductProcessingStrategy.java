package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;

public class RequestDepartmentProductProcessingStrategy implements IntentProcessingStrategy {
    private ShoppingCartService shoppingCartService;

    public RequestDepartmentProductProcessingStrategy(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
            return LexResponseHelper.createFailedLexResponse("Product or amount are not specified.", lexRequest);

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartBySessionId(lexRequest.getSessionId());

        LexResponse lexResponse = LexResponseHelper.createLexResponse(lexRequest, buildContent(lexRequest),
                                                                        DialogAction.Type.Close,
                                                                        DialogAction.FulfillmentState.Fulfilled);
        return lexResponse;
    }

    private String buildContent(LexRequest lexRequest) {
        String requestedUnit = lexRequest.getRequestedUnit();
        return requestedUnit != null && requestedUnit.length() > 0
                ? String.format("You requested: %s %s of %s.",
                                lexRequest.getRequestedAmount(), requestedUnit, lexRequest.getRequestedProduct())
                : String.format("You requested: %s %s.",
                                lexRequest.getRequestedAmount(), lexRequest.getRequestedProduct());
    }
}
