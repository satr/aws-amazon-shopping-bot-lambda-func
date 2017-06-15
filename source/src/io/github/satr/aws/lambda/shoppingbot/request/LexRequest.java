package io.github.satr.aws.lambda.shoppingbot.request;

public class LexRequest {
    private String botName;
    private String confirmationStatus;
    private String intentName;
    private String requestedProduct;
    private String amount;
    private boolean isProductSet;
    private String invocationSource;
    private String outputDialogMode;
    private String error;

    public void setProduct(String requestedProduct) {
        this.requestedProduct = requestedProduct;
    }

    public String getRequestedProduct() {
        return requestedProduct;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setInvocationSource(String invocationSource) {
        this.invocationSource = invocationSource;
    }

    public void setOutputDialogMode(String outputDialogMode) {
        this.outputDialogMode = outputDialogMode;
    }

    public void isSet(boolean value) {
        this.isProductSet = value;
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

    public boolean isProductRequested() {
        return this.isProductSet;
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
