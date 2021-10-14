package dev.huai.models;

import java.util.Date;
import java.util.Objects;

public class Request {
    private int request_id;
    private String description;
    private int user_id;
    private int status;
    private String base64encodedString;
    private String transaction_date;

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getBase64encodedString() {
        return base64encodedString;
    }

    public void setBase64encodedString(String base64encodedString) {
        this.base64encodedString = base64encodedString;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return request_id == request.request_id && user_id == request.user_id && status == request.status && Objects.equals(description, request.description) && Objects.equals(base64encodedString, request.base64encodedString) && Objects.equals(transaction_date, request.transaction_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request_id, description, user_id, status, base64encodedString, transaction_date);
    }

    @Override
    public String toString() {
        return "Request{" +
                "request_id=" + request_id +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", status=" + status +
                ", base64encodedString='" + base64encodedString + '\'' +
                ", transaction_date=" + transaction_date +
                '}';
    }
}
