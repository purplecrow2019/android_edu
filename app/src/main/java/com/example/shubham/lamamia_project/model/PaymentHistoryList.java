package com.example.shubham.lamamia_project.model;

public class PaymentHistoryList {

    private String payment_id, price, stage, status;

    public PaymentHistoryList(String payment_id, String price, String stage, String status)
    {
           this.payment_id = payment_id;
           this.price = price;
           this.stage = stage;
           this.status = status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getPrice() {
        return price;
    }

    public String getStage() {
        return stage;
    }

    public String getStatus() {
        return status;
    }
}
