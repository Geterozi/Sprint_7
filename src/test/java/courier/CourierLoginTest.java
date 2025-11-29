package courier;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.example.CourierClient;
import org.example.CourierRandom;
import org.example.Credentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourierLoginTest {

    CourierClient client = new CourierClient();
    Credentials credentials;
    Response createResponse;
    Response loginResponse;
    String courierId;

    @BeforeEach
    @Step("Создание курьера для получения учетных данных")
    public void setUp() {
        // Создаем объект Credentials, который сам сгенерирует случайные данные
        credentials = new Credentials();
        // Создаем курьера с этими учетными данными
        CourierRandom courier = new CourierRandom(credentials.login, credentials.password, "");
        createResponse = client.createCourier(courier);
    }

    @AfterEach
    @Step("Удаление созданного курьера после создания и логина")
    public void tearDown() {
        if (courierId != null) {
            Response deleteResponse = client.deleteCourier(courierId);

            assertThat("Статус удаления курьера должен быть 200",
                    deleteResponse.statusCode(),
                    equalTo(200));

            System.out.println("Курьер с ID " + courierId + " успешно удален");
        }
    }

    @Test
    @DisplayName("Логин курьера в системе")
    @Description("ОР: ответ 200, возвращает id")
    public void loginCourierOk() {

        loginResponse = client.loginCourier(credentials);

        // Проверяем статус ответа
        assertThat("Статус ответа должен быть 200",
                loginResponse.statusCode(),
                equalTo(200));

        // Сохраняем ID в поле класса
        courierId = loginResponse.jsonPath().getString("id");

        // Выводим ID курьера
        System.out.println("Сохраненный ID курьера: " + courierId);

        // Проверяем, что ID не пустой
        assertThat("ID должен быть не пустым",
                courierId,
                not(emptyString()));

        // Проверяем, что ID состоит только из цифр
        assertThat("ID должен содержать только цифры",
                courierId,
                matchesPattern("^\\d+$"));

    }

    @Test
    @DisplayName("Логин курьера без логина или пароля")
    @Description("ОР: ответ 400, возвращает сообщение 'Недостаточно данных для входа'")
    public void loginCourierWithoutCredentials() {
        // Тест без логина
        Credentials noLogin = new Credentials("", "validPassword");
        Response responseNoLogin = client.loginCourier(noLogin);

        assertThat("Статус ответа должен быть 400 без логина",
                responseNoLogin.statusCode(),
                equalTo(400));

        assertThat("Сообщение об ошибке должно быть верным",
                responseNoLogin.jsonPath().getString("message"),
                equalTo("Недостаточно данных для входа"));

        // Тест без пароля
        Credentials noPassword = new Credentials("validLogin", "");
        Response responseNoPassword = client.loginCourier(noPassword);

        assertThat("Статус ответа должен быть 400 без пароля",
                responseNoPassword.statusCode(),
                equalTo(400));

        assertThat("Сообщение об ошибке должно быть верным",
                responseNoPassword.jsonPath().getString("message"),
                equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера с несуществующей учетной записью")
    @Description("ОР: ответ 404, возвращает сообщение 'Учетная запись не найдена'")
    public void loginCourierWithInvalidCredentials() {
        // Создаем несуществующие учетные данные
        Credentials invalidCredentials = new Credentials("nonexistentLogin", "wrongPassword");

        Response response = client.loginCourier(invalidCredentials);

        assertThat("Статус ответа должен быть 404",
                response.statusCode(),
                equalTo(404));

        assertThat("Сообщение об ошибке должно быть верным",
                response.jsonPath().getString("message"),
                equalTo("Учетная запись не найдена"));
    }

}