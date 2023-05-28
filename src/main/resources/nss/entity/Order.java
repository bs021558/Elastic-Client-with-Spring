package nss.entity;

import java.util.List;

public class Order extends Template {
    String order_date;
    List<String> category;
    String taxful_total_price;
    Products products = new Products();
    List<String> sku;

    public List<String> getSku() {
        return sku;
    }

    public void setSku(List<String> sku) {
        this.sku = sku;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTaxful_total_price() {
        return taxful_total_price;
    }

    public void setTaxful_total_price(String taxful_total_price) {
        this.taxful_total_price = taxful_total_price;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
