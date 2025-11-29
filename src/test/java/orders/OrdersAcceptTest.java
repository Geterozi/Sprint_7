package orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.example.orders.OrdersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class OrdersAcceptTest {
    private OrdersClient client;
    private Response response;
    private static final int VALID_ORDER_ID = 536578; // предположительно существующий ID заказа
    private static final int VALID_COURIER_ID = 586686; // предположительно существующий ID курьера
    private static final int INVALID_ORDER_ID = 1;
    private static final int INVALID_COURIER_ID = 1;

    @BeforeEach
    void setUp() {
        client = new OrdersClient();
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    @Description("Проверяем успешное принятие заказа")
    @Step("Принятие существующего заказа существующим курьером")
    public void testAcceptValidOrder() {
        response = client.acceptOrder(VALID_ORDER_ID, VALID_COURIER_ID);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Ответ должен содержать ok: true",
                response.jsonPath().getBoolean("ok"),
                is(true));
    }

    @Test
    @DisplayName("Принятие заказа без ID курьера")
    @Description("Проверяем обработку отсутствия ID курьера")
    @Step("Принятие заказа без ID курьера")
    public void testAcceptOrderWithoutCourierId() {
        response = client.acceptOrder(VALID_ORDER_ID, null);

        assertThat("Статус ответа должен быть 400",
                response.statusCode(),
                equalTo(400));

        assertThat("Ответ должен содержать сообщение об ошибке",
                response.jsonPath().getString("message"),
                containsString("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с несуществующим ID курьера")
    @Description("Проверяем обработку несуществующего ID курьера")
    @Step("Принятие заказа с несуществующим ID курьера")
    public void testAcceptOrderWithInvalidCourierId() {
        response = client.acceptOrder(VALID_ORDER_ID, INVALID_COURIER_ID);

        assertThat("Статус ответа должен быть 404",
                response.statusCode(),
                equalTo(404));

        assertThat("Ответ должен содержать сообщение об ошибке",
                response.jsonPath().getString("message"),
                containsString("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Принятие заказа с несуществующим ID заказа")
    @Description("Проверяем обработку несуществующего ID заказа")
    @Step("Принятие заказа с несуществующим ID заказа")
    public void testAcceptOrderWithInvalidOrderId() {
        response = client.acceptOrder(INVALID_ORDER_ID, VALID_COURIER_ID);

        assertThat("Статус ответа должен быть 404",
                response.statusCode(),
                equalTo(404));

        assertThat("Ответ должен содержать сообщение об ошибке",
                response.jsonPath().getString("message"),
                containsString("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Принятие уже принятого заказа")
    @Description("Проверяем обработку попытки принятия уже принятого заказа")
    @Step("Принятие уже принятого заказа")
    public void testAcceptAlreadyAcceptedOrder() {
        //  принимаем заказ
        client.acceptOrder(VALID_ORDER_ID, VALID_COURIER_ID);

        // Затем пытаемся принять его снова
        response = client.acceptOrder(VALID_ORDER_ID, VALID_COURIER_ID);

        assertThat("Статус ответа должен быть 409",
                response.statusCode(),
                equalTo(409));

        assertThat("Ответ должен содержать сообщение об ошибке",
                response.jsonPath().getString("message"),
                containsString("Этот заказ уже в работе"));
    }
}