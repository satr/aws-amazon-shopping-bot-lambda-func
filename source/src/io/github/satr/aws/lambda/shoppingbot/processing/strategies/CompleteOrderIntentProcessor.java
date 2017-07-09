package io.github.satr.aws.lambda.shoppingbot.processing.strategies;
// Copyright © 2017, github.com/satr, MIT License

import com.sun.media.sound.InvalidDataException;
import io.github.satr.aws.lambda.shoppingbot.common.OperationResult;
import io.github.satr.aws.lambda.shoppingbot.common.OperationResultImpl;
import io.github.satr.aws.lambda.shoppingbot.common.OperationValueResult;
import io.github.satr.aws.lambda.shoppingbot.entities.*;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;
import io.github.satr.aws.lambda.shoppingbot.request.LexRequest;
import io.github.satr.aws.lambda.shoppingbot.response.DialogAction;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponse;
import io.github.satr.aws.lambda.shoppingbot.response.LexResponseHelper;
import io.github.satr.aws.lambda.shoppingbot.services.OrderService;
import io.github.satr.aws.lambda.shoppingbot.services.ShoppingCartService;
import io.github.satr.aws.lambda.shoppingbot.services.UserService;

import java.util.List;

public class CompleteOrderIntentProcessor extends UserSessionIntentProcessor {
    private ShoppingCartService shoppingCartService;
    private OrderService orderService;

    public CompleteOrderIntentProcessor(ShoppingCartService shoppingCartService, OrderService orderService, UserService userService, Logger logger) {
        super(userService, logger);
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if(gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getUserId());
        if(shoppingCart == null || shoppingCart.isEmpty())
            return createLexErrorResponse(lexRequest, "I'm sorry, shopping cart is empty yet.");

        Order order = createOrderFrom(shoppingCart);
        try {
            orderService.save(order);
            OperationResult savingOrderResult = validateSavedOrder(order);
            if(savingOrderResult.isFailed())
                return createLexErrorResponse(lexRequest, savingOrderResult.getErrorsAsString());
            shoppingCartService.delete(shoppingCart);
        } catch (Exception e) {
            logger.log(e);
            return createLexErrorResponse(lexRequest, "An error occurred while creating an order. Please try again.");
        }
        String orderContent = createOrderContent(order);
        return LexResponseHelper.createLexResponse(lexRequest, orderContent,
                DialogAction.Type.Close,
                DialogAction.FulfillmentState.Fulfilled);
    }

    private OperationResult validateSavedOrder(Order order) throws InvalidDataException {
        OperationResult operationResult = new OperationResultImpl();
        Order savedOrder = orderService.getByOrderId(order.getOrderId());
        if(savedOrder == null) {
            operationResult.addError("The order has noot been saved.");
        } else if(!order.toString().equals(savedOrder.toString())) {
            operationResult.addError(String.format("Saved order is different than original order.\n" +
                            "Original order:\n%s\nSaved order:\n%s.",
                             createOrderContent(order), createOrderContent(savedOrder)));
        }
        return operationResult;
    }

    private Order createOrderFrom(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        List<OrderItem> orderItems = order.getItems();
        for(ShoppingCartItem cartItem : shoppingCart.getItems())
            orderItems.add(createOrderItemFrom(cartItem));
        return order;
    }

    private OrderItem createOrderItemFrom(ShoppingCartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setAmount(cartItem.getAmount());
        orderItem.setUnit(cartItem.getUnit());
        orderItem.setPrice(cartItem.getPrice());
        return orderItem;
    }

    private String createOrderContent(Order order) {
        if(order == null || order.isEmpty())
            return "Order is empty";

        StringBuilder messageBuilder = new StringBuilder();
        for(OrderItem cartItem: order.getItems()){
            if(cartItem.isEmpty())
                continue;
            messageBuilder.append(String.format("%s; ", cartItem));
        }
        if(messageBuilder.length() > 0){
            messageBuilder.insert(0, "Order contains: ");
            messageBuilder.append(String.format("Total sum: %s", order.getTotalSum()));
        }
        return messageBuilder.toString();
    }
}
