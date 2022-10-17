package manager;

import manager.category.Category;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class Manager {

    //Создаем категории на основе списка tsv-файла
    public HashSet<Category> createCategories(File file) throws FileNotFoundException {
        HashMap<String, List<String>> categoriesFromFile = new HashMap<>();
        categoriesFromFile.put("другое", new ArrayList<>());
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (scanner.hasNextLine()) {
                String[] txt = scanner.nextLine().split("\t");
                categoriesFromFile.computeIfAbsent(txt[1], item -> new ArrayList<>()).add(txt[0]);
            }
            HashSet<Category> categories = new HashSet<>();
            List<String> keyList = new ArrayList<>(categoriesFromFile.keySet());
            for (String key : keyList) {
                categories.add(new Category(key, categoriesFromFile.get(key)));
            }
            return categories;
        }
    }

    //  определяем категорию с наибольшей абсолютной суммой трат:
    public String maxCategory(HashSet<Category> categories) {
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

        return "{\"maxCategory\": {\"category\":\""
                + maxCategory.getType()
                + "\",\"sum\":" + maxCategory.totalSum() + "}}";
    }

    // категория с наибольшими тратами за год:
    public String maxYearCategory(HashSet<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalYearSum(day) > maxSum) {
                maxCategory = cat;
            }
        }
        return "{\"maxYearCategory\": {\"category\":\""
                + maxCategory.getType()
                + "\",\"sum\":" + maxCategory.totalYearSum(day) + "}}";
    }

    // категория с наибольшими тратами за месяц:
    public String maxMothCategory(HashSet<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalMothSum(day) > maxSum) {
                maxCategory = cat;
            }
        }
        return "{\"maxMothCategory\": {\"category\":\""
                + maxCategory.getType()
                + "\",\"sum\":" + maxCategory.totalMothSum(day) + "}}";
    }

    // категория с наибольшими тратами за день:
    public String maxDayCategory(HashSet<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalDaySum(day) > maxSum) {
                maxCategory = cat;
            }
        }

        return "{\"maxDayCategory\": {\"category\":\""
                + maxCategory.getType()
                + "\",\"sum\":" + maxCategory.totalDaySum(day) + "}}";

    }

    //Сериализация - создание файла
    public void saveBin(File file, HashSet<Category> categories) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(categories);
        }
    }

    // Десериализация - восстановление корзины
    public HashSet<Category> categoriesFromBinFile(File file) {
        HashSet<Category> categories = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            categories = (HashSet<Category>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }


//  компаратор для сортировки категорий по максимальной абсолютной сумме трат
    Comparator<Category> comparatorMaxSum = Comparator.comparing(Category::totalSum);
//
//
//    //компаратор для сортировки категорий по сумме трат в год
//    Comparator<Category> comparatorMaxYearSum = Comparator.comparing(Category::totalYearSum);
//
//    //компаратор для сортировки категорй по сумме трат в месяц
//    Comparator<Category> comparatorMaxMothSum = Comparator.comparing(Category::totalMothSum);



}

