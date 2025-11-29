package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.CourierClient;
import org.example.CourierRandom;
import org.example.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierDeleteTest {
    CourierClient client = new CourierClient();
    private String courierId;
    private CourierRandom createdCourier;

    @BeforeEach
    @Step("Создание курьера для тестирования удаления")
    public void setUp() {
        createdCourier = new CourierRandom();
        Response createResponse = client.createCourier(createdCourier);

        // Получаем ID созданного курьера
        Response loginResponse = client.loginCourier(
                new Credentials(createdCourier.getLogin(), createdCourier.getPassword())
        );
        courierId = loginResponse.jsonPath().getString("id");
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("ОР: код ответа - 200")
    public void deleteCourierSuccess() {
        Response deleteResponse = client.deleteCourier(courierId);

        assertThat("Статус ответа должен быть 200",
                deleteResponse.statusCode(),
                equalTo(200));
    }

    @Test
    @DisplayName("Удаление курьера без ID")
    @Description("ОР: код ответа - 400, сообщение 'Недостаточно данных для удаления курьера'")
    public void deleteCourierWithoutId() {
        // Попытка удалить курьера без ID (null)
        Response deleteResponse = client.deleteCourier(null);

        assertThat("Статус ответа должен быть 400",
                deleteResponse.statusCode(),
                equalTo(400));

        assertThat("Сообщение об ошибке должно быть верным",
                deleteResponse.jsonPath().getString("message"),
                equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление несуществующего курьера")
    @Description("ОР: код ответа - 404, сообщение 'Курьера с таким id нет'")
    public void deleteNonExistentCourier() {
        // Создаем несуществующий ID
        String nonExistentId = "99999999999";

        Response deleteResponse = client.deleteCourier(nonExistentId);

        assertThat("Статус ответа должен быть 404",
                deleteResponse.statusCode(),
                equalTo(404));

        assertThat("Сообщение об ошибке должно быть верным",
                deleteResponse.jsonPath().getString("message"),
                equalTo("Курьера с таким id нет"));
    }
}