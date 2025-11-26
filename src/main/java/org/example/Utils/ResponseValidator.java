package org.example.Utils;

import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ResponseValidator {
    public static void validateErrorResponseCreateCourier(Response response, String caseName) {
        try {
            assertThat(response.statusCode(), equalTo(400));
            String errorMessage = response.jsonPath().getString("message");
            assertThat(errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
        } catch (AssertionError e) {
            System.out.println("Ошибка в случае: " + caseName);
            System.out.println("Ответ сервера: " + response.asString());
            throw e;
        }
    }

    public static void validateSuccessResponseCreateCourier(Response response, String caseName) {
        try {
            assertThat(response.statusCode(), equalTo(201));
            assertThat(response.jsonPath().getBoolean("ok"), equalTo(true));

            // Проверяем отсутствие поля firstName в ответе
            // Используем get() и проверяем на null
            assertThat(response.jsonPath().get("firstName"), equalTo(null));
        } catch (AssertionError e) {
            System.out.println("Ошибка в случае: " + caseName);
            System.out.println("Ответ сервера: " + response.asString());
            throw e;
        }
    }
}
