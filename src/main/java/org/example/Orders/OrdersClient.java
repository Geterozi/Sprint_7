package org.example.Orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS_URL = "/api/v1/orders";
    private static final String ACCEPT_ORDER_URL = "/api/v1/orders/accept/";

    @Step("Создание заказа")
    public Response createOrder(OrderData order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .log().all()
                .body(order)
                .when()
                .post(ORDERS_URL);
    }

    @Step("Получение списка заказов")
    public Response getAllOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", nearestStation)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .when()
                .get(ORDERS_URL);
    }

    @Step("Принятие заказа")
    public Response acceptOrder(int orderId, Integer courierId) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierId)
                .log().all()
                .when()
                .put(ACCEPT_ORDER_URL + orderId);
    }

//    @Step("Принятие заказа")
//    public Response acceptOrder(int orderId, Integer courierId) {
//        return given()
//                .baseUri(BASE_URL)
//                .header("Content-Type", "application/json")
//                .queryParam("orderId", orderId) // Передаем orderId как query-параметр
//                .queryParam("courierId", courierId) // Передаем courierId как query-параметр
//                .log().all()
//                .when()
//                .put(ACCEPT_ORDER_URL); // Убрали orderId из URL
//    }
}