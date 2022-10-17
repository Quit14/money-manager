package manager.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
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
    @DisplayName("Добавляем несколько покупок в один день")
    void addSaleTest() {
        LocalDate date1 = LocalDate.of(2022, 4, 20);
        catTest.addSale(date1, 1000L);
        catTest.addSale(date1, 2000L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(3000, currentResult);
    }

    @Test
    @DisplayName("Добавляем единственную покупку в день")
    void addSaleTest_singleShop() {
        LocalDate date1 = LocalDate.of(2020,10,11);
        LocalDate date2 = LocalDate.of(2022,10,10);
        catTest.addSale(date1, 1000L);
        catTest.addSale(date2, 4599L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(1000, currentResult);
    }

    @Test
    @DisplayName("Подсчет общей суммы покупок в категории")
    void totalSumTest() {
        LocalDate date1 = LocalDate.of(2022,10,15);
        LocalDate date2 = LocalDate.of(2022,10,16);
        LocalDate date3 = LocalDate.of(2022,10,17);
        catTest.addSale(date1, 100L);
        catTest.addSale(date2, 100L);
        catTest.addSale(date3, 100L);
        Assertions.assertEquals(300, catTest.totalSum());
    }
}
