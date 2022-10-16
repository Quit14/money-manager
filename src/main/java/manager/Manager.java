package manager;

import manager.category.Category;
import org.json.simple.JSONObject;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class Manager {

    public String maxCategory(HashSet<Category> categories) {

        // определяем категорию с наибольшей абсолютной суммой трат
        List<Category> comparableCategories = categories.stream()
                .sorted(comparatorMaxSum)
                .collect(Collectors.toList());
        Category maxCategory = comparableCategories.get(comparableCategories.size() - 1);

        //имеет ли принципиальное значение порядок ключ-значение в ответе сервера?

//        // создаем json-объект
//        JSONObject obj = new JSONObject();
//        obj.put("category", maxCategory.getType());
//        obj.put("sum", maxCategory.totalSum());
//        JSONObject message = new JSONObject();
//        message.put("maxCategory", obj);
//        return message.toJSONString();

        String message = "{\"maxCategory\": {\"category\":\""
                + maxCategory.getType()
                + "\",\"sum\":" + maxCategory.totalSum() + "}}";
        return message;
    }


    //компаратор для сортировки категорий по максимальной абсолютной сумме трат
    Comparator<Category> comparatorMaxSum = (c1, c2) -> {
        long sum1 = c1.totalSum();
        long sum2 = c2.totalSum();
        return Long.compare(sum1, sum2);
    };
}

