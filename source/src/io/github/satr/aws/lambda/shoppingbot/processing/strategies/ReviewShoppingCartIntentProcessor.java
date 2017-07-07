package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCart;
import io.github.satr.aws.lambda.shoppingbot.entity.ShoppingCartItem;
import io.github.satr.aws.lambda.shoppingbot.entity.User;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequestAttribute;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

public class ReviewShoppingCartIntentProcessor extends IntentProcessor {
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ReviewShoppingCartIntentProcessor(ShoppingCartService shoppingCartService, UserService userService, Logger logger) {
        super(logger);
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        String userId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        if(userId == null || userId.length() == 0)
            return createLexErrorResponse(lexRequest, "I'm sorry, could you please tell your name?");

        User user = userService.getUserById(userId);
        if(user == null)
            return createLexErrorResponse(lexRequest, "I'm sorry, could you please repeat your name?");

        StringBuilder messageBuilder = new StringBuilder();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if(shoppingCart == null){
            messageBuilder.append("Shopping cart is empty");
        } else {
            for(ShoppingCartItem cartItem: shoppingCart.getItems()){
                if(cartItem.getAmount() <= 0.0 || cartItem.getPrice() <= 0.0)
                    continue;
                messageBuilder.append(String.format("%s; ", cartItem));
            }
            if(messageBuilder.length() > 0){
                messageBuilder.insert(0, "Your shopping cart contains: ");
                messageBuilder.append(String.format("Total sum: %s", shoppingCart.getTotalSum()));
            }
        }
        LexResponse lexResponse = LexResponseHelper.createLexResponse(lexRequest, messageBuilder.toString(),
                DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled);
        return lexResponse;
    }
}
