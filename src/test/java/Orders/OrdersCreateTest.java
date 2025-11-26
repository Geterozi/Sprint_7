package Orders;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.Orders.OrderData;
import org.example.Orders.OrdersClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersCreateTest {
    private OrdersClient client = new OrdersClient();
    private OrderData order;

    @ParameterizedTest
    @MethodSource("ScooterApi.Orders.OrderData#orderColorsProvider")
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверяем создание заказа с разными комбинациями цветов")
    public void createOrderWithDifferentColors(List<String> colors) {
        order = OrderData.createBaseOrder();
        if (colors != null) {
            order.setColor(colors);
        }

        Response response = client.createOrder(order);

        assertThat("Статус ответа должен быть 201",
                response.statusCode(),
                equalTo(201));

        assertThat("Ответ должен содержать track",
                response.jsonPath().get("track"),
                notNullValue());
    }
}