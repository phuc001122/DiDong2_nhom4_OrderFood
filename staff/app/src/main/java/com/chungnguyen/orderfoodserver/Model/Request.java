package com.chungnguyen.orderfoodserver.Model;

import java.util.List;


public class Request {
    private String phone,name,address,total,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private List<Order> documents;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Order> documents) {
        this.documents = documents;
    }

    public Request(String phone, String name, String address, String total, List<Order> documents) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.documents = documents;
        this.status="0";
    }

    public Request() {
    }
}
