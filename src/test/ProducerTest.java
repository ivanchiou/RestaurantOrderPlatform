package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.ordersystem.model.AbstractOrder;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order; 
import com.ordersystem.model.OrderStatus;
import com.ordersystem.controller.Producer;
import com.ordersystem.controller.OrderFactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("產生者產生訂單測試")
class ProducerTest {
    private BlockingQueue<AbstractOrder> queue;
    private Producer producer;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>();
        producer = new Producer(queue);
    }

    @DisplayName("測試有效的訂單內容MenuItem")
    @ParameterizedTest(name = "測試訂單 - 名稱:{0}, 價格:{1}, 描述:{2}")
    @CsvSource({
        "漢堡, 50, 美味漢堡",
        "薯條, 20.11, 酥脆薯條",
        "可樂, 10.0, 冰涼可樂",
        "咖啡, 12.0, 香濃咖啡",
        "披薩, 39.99, 超值披薩"
    })
    public void testAddOrder(String name, double price, String description) throws InterruptedException {
        MenuItem item = new MenuItem(name, price, description);
        Order order = OrderFactory.createNextOrder(item, 1);
        producer.addOrder(order);
        
        AbstractOrder queuedOrder = queue.poll();
        assertNotNull(queuedOrder);
        assertEquals(order.getId(), queuedOrder.getId());
        assertEquals(OrderStatus.WAITING, queuedOrder.getStatus());
    }
}
