import calculation_of_the_shipping_cost.CargoDimension;
import calculation_of_the_shipping_cost.Delivery;
import calculation_of_the_shipping_cost.ServiceWorkload;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryCalculatorTest {
    @Test
    @Tag("Positive")
    @DisplayName("Расчет стоимости минимальной доставки")
    void testMinDeliveryCost() {
        Delivery delivery = new Delivery(1, CargoDimension.LARGE, false, ServiceWorkload.INCREASED);
        assertEquals(400, delivery.calculateDeliveryCost());
    }

    @Test
    @Tag("Positive")
    @DisplayName("Проверка расчета стоимости доставки в зависимости от расстояния")
    void testDistanceBasedDeliveryPrice() {
        Delivery delivery_1 = new Delivery(5, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        assertEquals(400, delivery_1.calculateDeliveryCost());
        Delivery delivery_2 = new Delivery(15, CargoDimension.SMALL, false, ServiceWorkload.HIGH);
        assertEquals(420, delivery_2.calculateDeliveryCost());
        Delivery delivery_3 = new Delivery(35, CargoDimension.SMALL, false, ServiceWorkload.VERY_HIGH);
        assertEquals(640, delivery_3.calculateDeliveryCost());
    }

    @Test
    @Tag("Positive")
    @DisplayName("Проверка расчета стоимости доставки в зависимости от размеров")
    void testSizeBasedDeliveryPrice() {
        Delivery delivery_1 = new Delivery(5, CargoDimension.LARGE, false, ServiceWorkload.NORMAL);
        assertEquals(400, delivery_1.calculateDeliveryCost());
        Delivery delivery_2 = new Delivery(15, CargoDimension.LARGE, false, ServiceWorkload.INCREASED);
        assertEquals(480, delivery_2.calculateDeliveryCost());
        Delivery delivery_3 = new Delivery(35, CargoDimension.LARGE, false, ServiceWorkload.VERY_HIGH);
        assertEquals(800, delivery_3.calculateDeliveryCost());
    }

//    @DisplayName("Проверка расчета стоимости доставки в зависимости от загруженности")
    @ParameterizedTest(name = "Проверка расчета стоимости доставки в зависимости от загруженности")
    @Tag("Positive")
    @CsvSource({"VERY_HIGH, 1120",
                "HIGH, 980",
                "INCREASED, 840",
                "NORMAL, 700"})
    void testLoadBasedDeliveryPrice(ServiceWorkload deliveryServiceWorkload, int expectedCost) {
        Delivery delivery = new Delivery(15, CargoDimension.LARGE, true, deliveryServiceWorkload);
        assertEquals(expectedCost, delivery.calculateDeliveryCost());
    }

    @Test
    @Tag("Negative")
    @DisplayName("Проверка расчета стоимости доставки в зависимости от хрупкости и на расстояние более 30 км")
    void testFragileBasedDeliveryPrice() {
        Delivery delivery = new Delivery(35, CargoDimension.SMALL, true, ServiceWorkload.NORMAL);
        Throwable exception = assertThrows(
                UnsupportedOperationException.class,
                delivery::calculateDeliveryCost
        );
        assertEquals("Fragile cargo cannot be delivered for the distance more than 30", exception.getMessage());
    }

    @DisplayName("Проверка расчета стоимости доставки на расстояние меньше нуля или 0")
    @ParameterizedTest(name = "Проверка расчета стоимости доставки на расстояние меньше нуля или 0")
    @Tag("Negative")
    @ValueSource(ints = {-1, 0})
    void testNegativeDistanceOrderCost(int distance) {
        Delivery delivery = new Delivery(-1, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                delivery::calculateDeliveryCost
        );
        assertEquals("destinationDistance should be a positive number!", exception.getMessage());
    }
}


