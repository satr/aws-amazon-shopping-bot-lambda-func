package io.github.satr.aws.lambda.shoppingbot.request;

public class LexRequest {
    private String botName;
    private String confirmationStatus;
    private String intentName;
    private String requestedProduct;
    private String requestedAmount;
    private String invocationSource;
    private String outputDialogMode;
    private String error;

    public void setProduct(String requestedProduct) {
        this.requestedProduct = requestedProduct;
    }

    public String getRequestedProduct() {
        return requestedProduct;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setInvocationSource(String invocationSource) {
        this.invocationSource = invocationSource;
    }

    public void setOutputDialogMode(String outputDialogMode) {
        this.outputDialogMode = outputDialogMode;
    }

    public boolean requestedProductIsSet() {
        return requestedProduct != null && requestedProduct.length() > 0;
    }

    public boolean requestedAmountIsSet() {
        return requestedAmount != null && requestedAmount.length() > 0;
    }

    public String getBotName() {
        return botName;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return getError() != null;
    }
}
