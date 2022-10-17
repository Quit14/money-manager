package manager.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryTest {
    private Category catTest;

// Создаем тест-категорию
    @BeforeEach
        void setUp() {
            List<String> studies = new ArrayList<>();
            studies.add("книги");
            catTest = new Category("учеба", studies);
    }


    @Test
    @DisplayName("Несколько покупок в один день")
    void addSaleTest() {
        Date date1 = new Date(20221016);
        catTest.addSale(date1, 1000L);
        catTest.addSale(date1, 2000L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(3000, currentResult);
    }

    @Test
    @DisplayName("Единственная покупка в день")
    void addSaleTest_singleShop() {
        Date date1 = new Date(20221016);
        Date date2 = new Date(20201016);
        catTest.addSale(date1, 1000L);
        catTest.addSale(date2, 4599L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(1000, currentResult);
    }

    @Test
    @DisplayName("Подсчет общей суммы покупок в категории")
    void totalSumTest() {
        Date date1 = new Date(20221015);
        Date date2 = new Date(20221016);
        Date date3 = new Date(20221017);
        catTest.addSale(date1, 100L);
        catTest.addSale(date2, 100L);
        catTest.addSale(date3, 100L);
        Assertions.assertEquals(300, catTest.totalSum());
    }
}
