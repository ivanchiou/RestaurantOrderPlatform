import javax.swing.SwingUtilities;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.ordersystem.view.OrderSystemUI;
import com.ordersystem.controller.Producer;
import com.ordersystem.controller.Consumer;
import com.ordersystem.model.AbstractOrder;

public class App {
    public static void main(String[] args) throws Exception {
        BlockingQueue<AbstractOrder> queue = new LinkedBlockingQueue<AbstractOrder>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderSystemUI orderSystemUI = new OrderSystemUI(producer, consumer);
                orderSystemUI.setVisible(true);
            }
        });

        System.out.println("Restaurant Order System Start!");
    }
}
