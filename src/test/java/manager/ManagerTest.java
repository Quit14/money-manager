package manager;

import manager.Manager;
import manager.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class ManagerTest {
    private Manager manager = new Manager();


    @Test
    @DisplayName("Тест: определяем категорию с наибольшей суммой трат (абсол.)")
    void maxCategoryTest() {
        //Создаем одну категорию
        List<String> studies = new ArrayList<>();
        studies.add("книги");
        Category cat1 = new Category("учеба", studies);
        LocalDate date1 = LocalDate.of(2022,05,10);
        cat1.addSale(date1, 500L);

        //Создаем вторую категорию
        List<String> home = new ArrayList<>();
        studies.add("посуда");
        Category cat2 = new Category("дом", home);
        LocalDate date2 = LocalDate.of(2020,10,10);
        cat2.addSale(date2, 3999L);

        HashSet<Category> testCat = new HashSet<>();
        testCat.add(cat1);
        testCat.add(cat2);
        String result = "{\"maxCategory\": {\"category\":\"дом\",\"sum\":3999}}";

        // Проверяем
        Assertions.assertEquals(result, manager.maxCategory(testCat));
    }
}


