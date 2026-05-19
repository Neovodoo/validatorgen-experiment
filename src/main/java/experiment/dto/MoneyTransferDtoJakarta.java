package experiment.dto;

import experiment.manual.jakarta.ManualJakartaGroup;
import experiment.manual.jakarta.ManualMoneyTransferValid;

import java.time.LocalDateTime;

@ManualMoneyTransferValid(groups = ManualJakartaGroup.class)
public class MoneyTransferDtoJakarta {

    private String transferId;
    private String requestId;
    private String sourceSystem;

    private String senderAccount;
    private String receiverAccount;
    private String receiverPhone;
    private String receiverEmail;

    private String transferType;
    private String status;
    private String channel;

    private Long amountCents;
    private Long feeCents;
    private Long taxCents;
    private Long totalDebitCents;
    private Long availableBalanceCents;
    private Long dailyLimitCents;
    private Long minAllowedAmountCents;
    private Long maxAllowedAmountCents;

    private Boolean requiresConfirmation;
    private String confirmationCode;

    private Boolean saveTemplate;
    private String templateName;

    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime executedAt;
    private LocalDateTime cancelledAt;

    private String eventType;
    private String eventKey;
    private String eventVersion;

    private String comment;

    public MoneyTransferDtoJakarta() {
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public Long getFeeCents() {
        return feeCents;
    }

    public void setFeeCents(Long feeCents) {
        this.feeCents = feeCents;
    }

    public Long getTaxCents() {
        return taxCents;
    }

    public void setTaxCents(Long taxCents) {
        this.taxCents = taxCents;
    }

    public Long getTotalDebitCents() {
        return totalDebitCents;
    }

    public void setTotalDebitCents(Long totalDebitCents) {
        this.totalDebitCents = totalDebitCents;
    }

    public Long getAvailableBalanceCents() {
        return availableBalanceCents;
    }

    public void setAvailableBalanceCents(Long availableBalanceCents) {
        this.availableBalanceCents = availableBalanceCents;
    }

    public Long getDailyLimitCents() {
        return dailyLimitCents;
    }

    public void setDailyLimitCents(Long dailyLimitCents) {
        this.dailyLimitCents = dailyLimitCents;
    }

    public Long getMinAllowedAmountCents() {
        return minAllowedAmountCents;
    }

    public void setMinAllowedAmountCents(Long minAllowedAmountCents) {
        this.minAllowedAmountCents = minAllowedAmountCents;
    }

    public Long getMaxAllowedAmountCents() {
        return maxAllowedAmountCents;
    }

    public void setMaxAllowedAmountCents(Long maxAllowedAmountCents) {
        this.maxAllowedAmountCents = maxAllowedAmountCents;
    }

    public Boolean getRequiresConfirmation() {
        return requiresConfirmation;
    }

    public void setRequiresConfirmation(Boolean requiresConfirmation) {
        this.requiresConfirmation = requiresConfirmation;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Boolean getSaveTemplate() {
        return saveTemplate;
    }

    public void setSaveTemplate(Boolean saveTemplate) {
        this.saveTemplate = saveTemplate;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
