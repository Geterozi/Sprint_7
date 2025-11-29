package org.example.orders;

import java.util.List;

public class OrderData {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    // Метод для создания базового заказа
    public static OrderData createBaseOrder() {
        return new OrderData(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha"
        );
    }




    // Конструктор с основными полями
    public OrderData(String firstName, String lastName, String address, String metroStation,
                     String phone, int rentTime, String deliveryDate, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    // сеттер для цвета
    public void setColor(List<String> color) {
        this.color = color;
    }

}