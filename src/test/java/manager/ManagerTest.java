package manager;

import manager.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.*;


public class ManagerTest {
    private Manager manager = new Manager();
    private Category catTest1;
    private Category catTest2;
    private Set<Category> testCat = new HashSet<>();

    //Даты
    private LocalDate date1 = LocalDate.of(2022, 10, 10);
    private LocalDate date2 = LocalDate.of(2022, 10, 11);
    private LocalDate date3 = LocalDate.of(2022, 10, 12);
    private LocalDate mothAfter = LocalDate.of(2022, 11, 11);
    private LocalDate mothBefore = LocalDate.of(2022, 8, 15);
    private LocalDate yearBefore = LocalDate.of(2021, 10, 9);
    private LocalDate yearAfter = LocalDate.of(2023, 12,10);

    @BeforeEach
    void setUp(){
        //Создаем одну категорию
        List<String> studies = new ArrayList<>();
        studies.add("книги");
        catTest1 = new Category("учеба", studies);
        catTest1.addSale(date1, 100L);
        catTest1.addSale(date2, 150L);
        catTest1.addSale(date3, 250L);
        catTest1.addSale(mothBefore, 500L);
        catTest1.addSale(mothAfter, 300L);
        catTest1.addSale(yearBefore, 1000L);
        catTest1.addSale(yearAfter, 1500L);

        //Создаем вторую категорию
        List<String> home = new ArrayList<>();
        home.add("посуда");
        catTest2 = new Category("дом", home);
        catTest2.addSale(date1, 90L);
        catTest2.addSale(date2, 160L);
        catTest2.addSale(date3, 200L);
        catTest2.addSale(mothBefore, 1000L);//1450
        catTest2.addSale(mothAfter, 300L); //500
        catTest2.addSale(yearBefore, 1000L);
        catTest2.addSale(yearAfter, 5000L);

        //Заполняем набор категорий
        testCat.add(catTest1);
        testCat.add(catTest2);

    }



    @Test
    @DisplayName("Тест: определяем категорию с наибольшей суммой трат (абсол.)")
    void maxCategoryTest() {
        String result = "{\n  \"category\": \"дом\",\n  \"sum\": 7750\n}";
        Assertions.assertEquals(result, manager.maxCategory(testCat));
    }

    @Test
    @DisplayName("Наибольшая сумма трат за год")
    void maxYearCategoryTest() {
        String result = "{\n  \"category\": \"дом\",\n  \"sum\": 1450\n}";
        Assertions.assertEquals(result, manager.maxYearCategory(testCat,date3));
    }


    @Test
    @DisplayName("Наибольшая сумма трат за месяц")
    void maxMothCategoryTest() {
        String result = "{\n  \"category\": \"учеба\",\n  \"sum\": 700\n}";
        Assertions.assertEquals(result, manager.maxMothCategory(testCat,mothAfter));
    }
}


