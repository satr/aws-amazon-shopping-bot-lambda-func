package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.*;
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
            return createLexErrorResponse(lexRequest, "I'm sorry, could you please tell your name?");

        User user = userService.getUserById(userId);
        if(user == null)
            return createLexErrorResponse(lexRequest, "I'm sorry, could you please repeat your name?");

        String productName = lexRequest.getRequestedProduct();
        Product product = productService.getByProductId(productName);
        if(product == null)
            return createLexErrorResponse(lexRequest, String.format("Product \"%s\" is not found", productName));

        String unit = lexRequest.getRequestedUnit();
        Double unitPrice = product.getUnitPriceFor(unit == null ? "piece" : unit);
        if(unitPrice == null || unitPrice == Product.notFoundPrice)
            return createLexErrorResponse(lexRequest, String.format("I' sorry - price for %s is not found", unit == null ? "this" : unit));

        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        ShoppingCartItem cartItem = shoppingCart.getItemByProduct(productName);
        String message = updateCartItemWithRequested(cartItem, unit, unitPrice, Double.parseDouble(lexRequest.getRequestedAmount()));
        shoppingCartService.save(shoppingCart);
        return LexResponseHelper.createLexResponse(lexRequest, message,
                                                    DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled);
    }

    private String updateCartItemWithRequested(ShoppingCartItem cartItem, String unit, double unitPrice, double amount) {
        String existingUnit = cartItem.getUnit();
        String productName = cartItem.getProduct();
        if (existingUnit != null && existingUnit.equals(unit)) {
            cartItem.addAmount(amount);
            cartItem.setPrice(unitPrice);
            return String.format("Added %s %s of %s.", amount, existingUnit, productName);
        }
        cartItem.setAmount(amount);
        cartItem.setUnit(unit);
        cartItem.setPrice(unitPrice);
        return String.format("Put %s %s of %s, price: %s.", amount, unit != null ? unit : "", productName, unitPrice);
    }

    private ShoppingCart getOrCreateShoppingCart(String userId) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if (shoppingCart != null)
            return shoppingCart;
        shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        return shoppingCart;
    }
}
