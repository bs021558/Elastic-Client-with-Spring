package nss.entity;

import java.util.List;

public class Products {
    List<String> price;
    String product_name;
    List<String> manufacturer;
    public List<String> getPrice() {
        return price;
    }
    public void setPrice(List<String> price) {
        this.price = price;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public List<String> getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(List<String> manufacturer) {
        this.manufacturer = manufacturer;
    }
}
