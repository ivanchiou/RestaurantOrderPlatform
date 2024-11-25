package test;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ordersystem.controller.OrderValidator;

@DisplayName("訂單驗證器測試")
public class OrderValidatorTest {
    
    @DisplayName("測試有效的訂單ID")
    @ParameterizedTest(name = "訂單ID {0} 應該是有效的")
    @ValueSource(strings = {
        "00000000000000000",
        "99999999999999999",
        "11111111111111111"
    })
    public void shouldValidOrderIds(String orderID) {
        assertTrue(OrderValidator.isValidOrderID(orderID), "訂單ID應該是17位數");
    }

    @DisplayName("測試無效的訂單ID")
    @ParameterizedTest(name = "訂單ID {0} 應該是無效的")
    @ValueSource(strings = {
        "123",
        "999999999999999999",
        "1",
        "12.991111111111111"
    })
    public void shouldNotValidOrderIds(String orderID) {
        assertFalse(OrderValidator.isValidOrderID(orderID), "訂單ID非17位數應該是無效的");
    } 
}
