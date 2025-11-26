package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

 public class CourierClient {
    //Документация к базовому URL - https://qa-scooter.praktikum-services.ru/docs/
    public static final String BASE_URL_SCOOTER_PRAKTIKUM_SERVICE = "https://qa-scooter.praktikum-services.ru";
    // Метод Создание курьера
    private static final String CREATE_COURIER_URL_PATH = "/api/v1/courier";
    // Метод Логин курьера в системе
    private static final String LOGIN_COURIER_URL_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response createCourier(CourierRandom courier) {
        return given()
                .baseUri(BASE_URL_SCOOTER_PRAKTIKUM_SERVICE)
                .header("Content-type", "application/json")
                .log().all() // добавляем логирование всех деталей запроса
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL_PATH);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(Credentials credentials) {
        return given()
                .baseUri(BASE_URL_SCOOTER_PRAKTIKUM_SERVICE)
                .header("Content-type", "application/json")
                .log().all()
                .body(credentials)
                .when()
                .post(LOGIN_COURIER_URL_PATH);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return given()
                .baseUri(BASE_URL_SCOOTER_PRAKTIKUM_SERVICE)
                .header("Content-type", "application/json")
                //.log().all()
                .when()
                .delete(CREATE_COURIER_URL_PATH + "/" + id);
    }

}