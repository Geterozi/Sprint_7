package orders;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.orders.OrderData;
import org.example.orders.OrdersClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersCreateTest {
    private OrdersClient client = new OrdersClient();
    private OrderData order;


    public static Stream<List<String>> orderColorsProvider() {
        return Stream.of(
                List.of("BLACK"),     // один цвет  BLACK
                List.of("GREY"),      // один цвет GREY
                List.of("BLACK", "GREY"), // два цвета
                null                 // без цвета
        );
    }


    @ParameterizedTest
    @MethodSource("orderColorsProvider") // 💡
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