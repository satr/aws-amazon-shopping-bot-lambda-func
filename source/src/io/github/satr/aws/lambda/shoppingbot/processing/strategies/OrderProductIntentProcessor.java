package io.github.satr.aws.lambda.shoppingbot.processing.strategies;

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;

import java.util.List;

public class OrderProductIntentProcessor implements IntentProcessor {
    private ShoppingCartService shoppingCartService;

    public OrderProductIntentProcessor(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
            return LexResponseHelper.createFailedLexResponse("Product or amount are not specified.", lexRequest);

        String userId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        ShoppingCartItem cartItem = shoppingCart.getItemByProduct(lexRequest.getRequestedProduct());
        updateCartItemWithRequested(lexRequest, cartItem);
        shoppingCartService.save(shoppingCart);
        LexResponse lexResponse = LexResponseHelper.createLexResponse(lexRequest, buildContent(lexRequest),
                                                    DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled);
        return lexResponse;
    }

    private void updateCartItemWithRequested(LexRequest lexRequest, ShoppingCartItem cartItem) {
        double requestedAmount = Double.parseDouble(lexRequest.getRequestedAmount());
        String requestedUnit = lexRequest.getRequestedUnit();
        String existingUnit = cartItem.getUnit();
        if(existingUnit == null){
            cartItem.setAmount(requestedAmount);
            cartItem.setUnit(requestedUnit);
            //TODO: inform about set amount and unit
        } else if(existingUnit.equals(requestedUnit)) {
            cartItem.addAmount(requestedAmount);//TODO: inform about added abount
        } else {
            cartItem.setAmount(requestedAmount);
            cartItem.setUnit(requestedUnit);//TODO: inform about changed unit
        }
    }

    private ShoppingCart getOrCreateShoppingCart(String userId) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if (shoppingCart != null)
            return shoppingCart;
        shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        return shoppingCart;
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
