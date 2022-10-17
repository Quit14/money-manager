package manager.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.cglib.core.Local;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryTest {
    private Category catTest;
    private LocalDate date1 = LocalDate.of(2022, 10, 10);
    private LocalDate date2 = LocalDate.of(2022, 10, 11);
    private LocalDate date3 = LocalDate.of(2022, 10, 12);
    private LocalDate dateAnotherMoth = LocalDate.of(2022, 11, 15);
    private LocalDate dateAnotherYear = LocalDate.of(2021, 10, 9);

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
        catTest.addSale(date1, 1000L);
        catTest.addSale(date1, 2000L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(3000, currentResult);
    }

    @Test
    @DisplayName("Добавляем единственную покупку в день")
    void addSaleTest_singleShop() {
        catTest.addSale(date1, 1000L);
        catTest.addSale(date2, 4599L);
        long currentResult = catTest.getBuyingLog().get(date1);
        Assertions.assertEquals(1000, currentResult);
    }

    @Test
    @DisplayName("Подсчет общей суммы покупок в категории")
    void totalSumTest() {
        catTest.addSale(date1, 100L);
        catTest.addSale(date2, 100L);
        catTest.addSale(date3, 100L);
        catTest.addSale(dateAnotherYear, 100L);
        catTest.addSale(dateAnotherMoth, 100L);
        Assertions.assertEquals(500, catTest.totalSum());
    }

    @Test
    @DisplayName("Подсчет суммы покупок за день")
    void totalDaySumTest() {
        catTest.addSale(date1, 100L);
        catTest.addSale(date1, 100L);
        catTest.addSale(date2, 100L);
        Assertions.assertEquals(200, catTest.totalDaySum(date1));
    }

    @Test
    @DisplayName("Подсчет суммы покупок за день у пустой категории")
    void totalDaySumTest_Empty() {
        Assertions.assertEquals(0, catTest.totalDaySum(date1));
    }

    @Test
    @DisplayName("Подсчет суммы покупок за месяц")
    void totalMothSumTest() {
        catTest.addSale(date1, 150L);
        catTest.addSale(date2, 200L);
        catTest.addSale(date1, 150L);
        catTest.addSale(dateAnotherMoth, 100L);
        catTest.addSale(dateAnotherYear, 1000L);
        Assertions.assertEquals(500, catTest.totalMothSum(date2));
    }

    @Test
    @DisplayName("Подсчет суммы покупок за месяц у пустой категории")
    void totalMothSumTest_Empty() {
        Assertions.assertEquals(0, catTest.totalMothSum(date2));
    }

    @Test
    @DisplayName("Подсчет суммы покупок за год")
    void totalYearSumTest() {
        catTest.addSale(date1, 100L);
        catTest.addSale(date2, 100L);
        catTest.addSale(date1, 100L);
        catTest.addSale(LocalDate.of(2022, 1, 5), 100L);
        catTest.addSale(dateAnotherYear, 100L);
        Assertions.assertEquals(400, catTest.totalYearSum(date2));
    }

    @Test
    @DisplayName("Подсчет суммы покупок за год у пустой категории")
    void totalYearSumTest_Empty() {
        Assertions.assertEquals(0, catTest.totalYearSum(date1));
    }

}

