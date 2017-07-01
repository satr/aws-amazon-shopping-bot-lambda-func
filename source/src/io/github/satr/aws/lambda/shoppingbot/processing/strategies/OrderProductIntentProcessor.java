package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.Product;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.ProductService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

public class OrderProductIntentProcessor extends IntentProcessor {
    private ShoppingCartService shoppingCartService;
    private UserService userService;
    private ProductService productService;

    public OrderProductIntentProcessor(ShoppingCartService shoppingCartService, UserService userService, ProductService productService, Logger logger) {
        super(logger);
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
            return createLexErrorResponse(lexRequest, "Product or amount are not specified.");

        String userId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        if(userId == null || userId.length() == 0)
            return createLexErrorResponse(lexRequest, "User is not recognized - UserId is not specified.");

        String requestedProduct = lexRequest.getRequestedProduct();
        Product product = productService.getByProductId(requestedProduct);
        if(product == null)
            return createLexErrorResponse(lexRequest, String.format("Product \"%s\" is not found", requestedProduct));

        User user = userService.getUserById(userId);
        if(user == null)
            return createLexErrorResponse(lexRequest, String.format("UserId is not recognized by UserId %s", userId));

        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        ShoppingCartItem cartItem = shoppingCart.getItemByProduct(requestedProduct);
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
