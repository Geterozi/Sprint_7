package Courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.CourierClient;
import org.example.CourierRandom;
import org.example.Credentials;
import org.example.Utils.ResponseValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {
        CourierClient client = new CourierClient();
        private String courierId; // Добавляем поле для хранения ID
        private CourierRandom createdCourier; // Добавляем поле для хранения созданного курьера

        @AfterEach
        @Step("Удаление созданного курьера")
        public void tearDown() {
            if (createdCourier != null) {
                // Выполняем логин для получения ID
                Response loginResponse = client.loginCourier(
                        new Credentials(createdCourier.getLogin(), createdCourier.getPassword())
                );

                // Получаем ID из ответа
                courierId = loginResponse.jsonPath().getString("id");

                if (courierId != null) {
                    // Удаляем курьера
                    Response deleteResponse = client.deleteCourier(courierId);

                    assertThat("Статус удаления курьера должен быть 200",
                            deleteResponse.statusCode(),
                            equalTo(200));

                    System.out.println("Курьер с ID " + courierId + " успешно удален");
                }
            }
        }

        @Test
        @DisplayName("Успешное создание учетной записи с заполненными логином, паролем и именем")
        @Description("ОР: код ответа - 201, возвращает - ok: true")
        public void createCourierOk () {

            createdCourier = new CourierRandom();
            Response response = client.createCourier(createdCourier);

            assertThat(response.statusCode(), equalTo(201));
            assertThat(response.jsonPath().getBoolean("ok"), equalTo(true));

        }

        @Test
        @DisplayName("Успешное создание учетной записи С заполненными логином, паролем и БЕЗ имени")
        @Description("ОР: код ответа - 201, возвращает - ok: true")
        public void createCourierWithoutFirstName() {

            // Случай 1: имя равно null
            createdCourier = new CourierRandom();
            createdCourier.setFirstName(null);
            System.out.println("Тестируем создание курьера с null именем");
            Response responseNull = client.createCourier(createdCourier);
            ResponseValidator.validateSuccessResponseCreateCourier(responseNull, "Случай с null именем");

            // Случай 2: имя - пустая строка
            createdCourier = new CourierRandom();
            createdCourier.setFirstName("");
            System.out.println("Тестируем создание курьера с пустым именем");
            Response responseEmpty = client.createCourier(createdCourier);
            ResponseValidator.validateSuccessResponseCreateCourier(responseEmpty, "Случай с пустой строкой");
        }

        @Test
        @DisplayName("Создание курьера с повторяющимся логином")
        @Description("ОР: код ответа - 409, возвращает - Этот логин уже используется. Попробуйте другой.")
        public void createCourierWithExistLogin () {

            createdCourier = new CourierRandom();

            Response firstResponse = client.createCourier(createdCourier);
            assertThat(firstResponse.statusCode(), equalTo(201));

            Response secondResponse = client.createCourier(createdCourier);
            assertThat(secondResponse.statusCode(), equalTo(409));
            String errorMessage = secondResponse.jsonPath().getString("message");
            assertThat(errorMessage, equalTo("Этот логин уже используется. Попробуйте другой."));

        }

        @Test
        @DisplayName("Создание курьера без логина")
        @Description("ОР: код ответа - 400, возвращает - Недостаточно данных для создания учетной записи")
        public void createCourierWithoutLogin() {

            // Проверка с пустой строкой
            createdCourier = new CourierRandom();
            createdCourier.setLogin("");
            System.out.println("Тестируем случай с пустым логином");
            Response responseEmpty = client.createCourier(createdCourier);
            ResponseValidator.validateErrorResponseCreateCourier(responseEmpty, "Пустой логин");

            // Проверка с null
            createdCourier = new CourierRandom();
            createdCourier.setLogin(null);
            System.out.println("Тестируем случай с null логином");
            Response responseNull = client.createCourier(createdCourier);
            ResponseValidator.validateErrorResponseCreateCourier(responseNull, "Null логин");
        }

        @Test
        @DisplayName("Создание курьера без пароля")
        @Description("ОР: код ответа - 400, возвращает - Недостаточно данных для создания учетной записи")
        public void createCourierWithoutPassword() {

            // Проверка с пустой строкой
            CourierRandom courier1 = new CourierRandom();
            courier1.setPassword("");
            System.out.println("Тестируем случай с пустым паролем");
            Response responseEmpty = client.createCourier(courier1);
            ResponseValidator.validateErrorResponseCreateCourier(responseEmpty, "Пустой пароль");

            // Проверка с null
            CourierRandom courier2 = new CourierRandom();
            courier2.setPassword(null);
            System.out.println("Тестируем случай с null паролем");
            Response responseNull = client.createCourier(courier2);
            ResponseValidator.validateErrorResponseCreateCourier(responseNull, "Null пароль");
        }

        @Test
        @DisplayName("Создание курьера без логина и пароля")
        @Description("ОР: код ответа - 400, возвращает - Недостаточно данных для создания учетной записи")
        public void createCourierWithoutLoginAndPassword() {
            CourierRandom courier = new CourierRandom();

            // Случай 1: оба поля пустые строки
            courier.setLogin("");
            courier.setPassword("");
            System.out.println("Тестируем случай с пустыми логином и паролем");
            Response responseEmpty = client.createCourier(courier);
            ResponseValidator.validateErrorResponseCreateCourier(responseEmpty, "Пустые логин и пароль");

            // Случай 2: логин null, пароль пустая строка
            courier.setLogin(null);
            courier.setPassword("");
            System.out.println("Тестируем случай с null логином и пустым паролем");
            Response responseNullLogin = client.createCourier(courier);
            ResponseValidator.validateErrorResponseCreateCourier(responseNullLogin, "Null логин и пустой пароль");

            // Случай 3: логин пустая строка, пароль null
            courier.setLogin("");
            courier.setPassword(null);
            System.out.println("Тестируем случай с пустым логином и null паролем");
            Response responseNullPassword = client.createCourier(courier);
            ResponseValidator.validateErrorResponseCreateCourier(responseNullPassword, "Пустой логин и null пароль");

            // Случай 4: оба поля null
            courier.setLogin(null);
            courier.setPassword(null);
            System.out.println("Тестируем случай с null логином и паролем");
            Response responseBothNull = client.createCourier(courier);
            ResponseValidator.validateErrorResponseCreateCourier(responseBothNull, "Оба поля null");
        }

    }


