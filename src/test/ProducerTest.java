package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.ordersystem.model.AbstractOrder;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;
import com.ordersystem.controller.OrderFactory;
import com.ordersystem.controller.Producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("生產者測試")
public class ProducerTest {
    private BlockingQueue<AbstractOrder> queue;
    private Producer producer;
    
    @BeforeEach
    public void setUp() {
        queue = new LinkedBlockingQueue<>();
        producer = new Producer(queue);
    }

    @DisplayName("測試有效的訂單內容MenuItems")
    @ParameterizedTest(name = "測試訂單-名稱{0},價格:{1},描述:{2},數量:{3}")
    @CsvSource({
        "漢堡, 50.0, 美味漢堡, 1",
        "薯條, 35.0, 酥炸薯條, 99"
    })
    public void testValidAddOrder(String name, double price, String description, int quantity) {
        MenuItem item = new MenuItem(name, price, description);
        Order order = OrderFactory.createNextOrder(item, quantity);
        producer.addOrder(order);

        AbstractOrder queueOrder = queue.poll();
        assertNotNull(queueOrder);
        assertEquals(order.getId(), queueOrder.getId());
        assertEquals(queueOrder.getStatus(), OrderStatus.WAITING);
    }

    @DisplayName("測試無效的訂單內容MenuItems")
    @ParameterizedTest(name = "測試訂單-名稱{0},價格:{1},描述:{2},數量:{3}")
    @CsvSource({
        "漢堡, 50.0, 美味漢堡, 0",
        "薯條, 35.0, 酥炸薯條, 999"
    })
    public void testNotValidAddOrder(String name, double price, String description, int quantity) {
        MenuItem item = new MenuItem(name, price, description);
        Order order = OrderFactory.createNextOrder(item, quantity);
  
        assertNull(order);
    }
}
