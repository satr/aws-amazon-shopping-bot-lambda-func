package io.github.satr.aws.lambda.shoppingbot.request;

import java.util.LinkedHashMap;
import java.util.Map;

public class LexRequest {
    private String botName;
    private String confirmationStatus;
    private String intentName;
    private String requestedProduct;
    private String requestedAmount;
    private String requestedUnit;
    private String invocationSource;
    private String outputDialogMode;
    private String error;
    private String firstName;
    private String lastName;
    private String address;
    private Map<String, Object> sessionAttributes = new LinkedHashMap<>();

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

    public String getRequestedUnit() { return requestedUnit; }

    public void setInvocationSource(String invocationSource) {
        this.invocationSource = invocationSource;
    }

    public void setOutputDialogMode(String outputDialogMode) {
        this.outputDialogMode = outputDialogMode;
    }

    public boolean requestedProductIsSet() {
        return requestedProduct != null && requestedProduct.length() > 0;
    }
    public boolean requestedAmountIsSet() { return requestedAmount != null && requestedAmount.length() > 0; }
    public boolean requestedUnitsIsSet() { return requestedUnit != null && requestedUnit.length() > 0; }
    public boolean firstNameIsSet() { return firstName != null && firstName.length() > 0; }
    public boolean lastNameIsSet() { return lastName != null && lastName.length() > 0; }

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

    public void setRequestedUnit(String requestedUnit) {
        this.requestedUnit = requestedUnit;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes != null ? sessionAttributes : new LinkedHashMap<>();
    }

    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getSessionAttribute(String attributeName) {
        return sessionAttributes.containsKey(attributeName) ? sessionAttributes.get(attributeName) : null;
    }

    public void setSessionAttribute(String attributeName, Object value) {
        sessionAttributes.put(attributeName, value);
    }

    public String getSessionId() {
        return (String) getSessionAttribute(LexRequestAttribute.SessionAttribute.SessionId);
    }}
