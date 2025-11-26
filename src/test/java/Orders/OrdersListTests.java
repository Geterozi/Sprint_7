package Orders;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.Orders.OrdersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

 public class OrdersListTests {
    private OrdersClient client;
    private Response response;

    @BeforeEach
    void setUp() {
        client = new OrdersClient();
    }

    @Test
    @DisplayName("Получение списка всех заказов")
    @Description("Проверяем получение списка всех заказов без параметров")
    @Step("Получение списка всех заказов")
    public void testGetAllOrders() {
        response = client.getAllOrders(null, null, null, null);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Ответ должен содержать поле orders",
                response.jsonPath().get("orders"),
                notNullValue());

        assertThat("Ответ должен быть не пустым",
                response.jsonPath().getInt("orders.size()"),
                greaterThanOrEqualTo(0));

        assertThat("Ответ должен содержать pageInfo",
                response.jsonPath().get("pageInfo"),
                notNullValue());

        assertThat("Ответ должен содержать availableStations",
                response.jsonPath().get("availableStations"),
                notNullValue());
    }

    @Test
    @DisplayName("Получение заказов по ID курьера")
    @Description("Проверяем получение заказов конкретного курьера")
    @Step("Получение заказов по ID курьера")
    public void testGetOrdersByCourierId() {
        // Используем существующий ID курьера (предположим, что 586686 существует)
        int courierId = 586686;
        response = client.getAllOrders(courierId, null, null, null);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Все заказы должны принадлежать указанному курьеру",
                response.jsonPath().getList("orders.courierId"),
                everyItem(equalTo(courierId)));
    }
    @Test
    @DisplayName("Получение заказов по станциям метро")
    @Description("Проверяем фильтрацию заказов по станциям метро")
    @Step("Получение заказов по станциям метро")
    public void testGetOrdersByStations() {
        String stations = "[\"1\",\"2\"]"; // корректный формат JSON массива
        response = client.getAllOrders(null, stations, null, null);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Ответ должен содержать заказы только с указанными станциями",
                response.jsonPath().getList("orders.metroStation"),
                everyItem(anyOf(equalTo("1"), equalTo("2"))));
    }

    @Test
    @DisplayName("Проверка пагинации")
    @Description("Проверяем работу пагинации при получении списка заказов")
    @Step("Проверка пагинации")
    public void testPagination() {
        int limit = 10;
        int page = 0;
        response = client.getAllOrders(null, null, limit, page);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Количество элементов не должно превышать limit",
                response.jsonPath().getInt("orders.size()"),
                lessThanOrEqualTo(limit));

        assertThat("Текущая страница должна соответствовать запрошенной",
                response.jsonPath().getInt("pageInfo.page"),
                equalTo(page));

        assertThat("Limit в ответе должен соответствовать запрошенному",
                response.jsonPath().getInt("pageInfo.limit"),
                equalTo(limit));
    }

    @Test
    @DisplayName("Проверка получения списка с кастомным лимитом")
    @Description("Проверяем корректность работы параметра limit")
    @Step("Проверка limit")
    public void testCustomLimit() {
        int customLimit = 5;
        response = client.getAllOrders(null, null, customLimit, 0);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Количество элементов должно соответствовать limit",
                response.jsonPath().getInt("orders.size()"),
                lessThanOrEqualTo(customLimit));
    }

    @Test
    @DisplayName("Проверка обработки несуществующего курьера")
    @Description("Проверяем обработку запроса с несуществующим ID курьера")
    @Step("Проверка несуществующего курьера")
    public void testNonExistentCourier() {
        int nonExistentId = 12345; //  несуществующий ID
        response = client.getAllOrders(nonExistentId, null, null, null);

        assertThat("Статус ответа должен быть 404",
                response.statusCode(),
                equalTo(404));

        assertThat("Ответ должен содержать сообщение об ошибке",
                response.jsonPath().getString("message"),
                containsString("не найден"));
    }

    @Test
    @DisplayName("Проверка получения информации о доступных станциях")
    @Description("Проверяем наличие информации о доступных станциях метро")
    @Step("Проверка availableStations")
    public void testAvailableStations() {
        response = client.getAllOrders(null, null, null, null);

        assertThat("Статус ответа должен быть 200",
                response.statusCode(),
                equalTo(200));

        assertThat("Список станций должен быть не пустым",
                response.jsonPath().getInt("availableStations.size()"),
                greaterThan(0));

        assertThat("Каждая станция должна иметь все обязательные поля",
                response.jsonPath().getList("availableStations"),
                everyItem(allOf(
                        hasKey("name"),
                        hasKey("number"),
                        hasKey("color")
                )));
    }
}