package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import manager.category.Category;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class Manager {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Создаем категории на основе списка tsv-файла
    public Set<Category> createCategories(File file) throws FileNotFoundException {
        HashMap<String, List<String>> categoriesFromFile = new HashMap<>();
        categoriesFromFile.put("другое", new ArrayList<>());
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (scanner.hasNextLine()) {
                String[] txt = scanner.nextLine().split("\t");
                categoriesFromFile.computeIfAbsent(txt[1], item -> new ArrayList<>()).add(txt[0]);
            }
            Set<Category> categories = new HashSet<>();
            List<String> keyList = new ArrayList<>(categoriesFromFile.keySet());
            for (String key : keyList) {
                categories.add(new Category(key, categoriesFromFile.get(key)));
            }
            return categories;
        }
    }

    private class maxValue {
        String category;
        long sum;

        public maxValue(String category, long sum) {
            this.category = category;
            this.sum = sum;
        }
    }

    //  определяем категорию с наибольшей абсолютной суммой трат:
    protected String maxCategory(Set<Category> categories) {
        List<Category> comparableCategories = categories.stream()
                .sorted(comparatorMaxSum)
                .collect(Collectors.toList());
        Category maxCategory = comparableCategories.get(comparableCategories.size() - 1);
        return gson.toJson(new maxValue(maxCategory.getType(), maxCategory.totalSum()));
    }

    // категория с наибольшими тратами за год:
    protected String maxYearCategory(Set<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalYearSum(day) > maxSum) {
                maxCategory = cat;
                maxSum = cat.totalYearSum(day);
            }
        }
        return gson.toJson(new maxValue(maxCategory.getType(), maxSum));
    }


    // категория с наибольшими тратами за месяц:
    protected String maxMothCategory(Set<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalMothSum(day) > maxSum) {
                maxCategory = cat;
                maxSum = cat.totalMothSum(day);
            }
        }
        return gson.toJson(new maxValue(maxCategory.getType(), maxSum));
    }

    // категория с наибольшими тратами за день:
    private String maxDayCategory(Set<Category> categories, LocalDate day) {
        Category maxCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalDaySum(day) > maxSum) {
                maxCategory = cat;
                maxSum = cat.totalDaySum(day);
            }
        }
        return gson.toJson(new maxValue(maxCategory.getType(), maxSum));

    }

    public String printMaxCategories(Set<Category> categories, LocalDate day) {
        Map<String, String> maxCategories = new LinkedHashMap<>();
        maxCategories.put("maxCategory", maxCategory(categories));
        maxCategories.put("maxYearCategory", maxYearCategory(categories, day));
        maxCategories.put("maxMonthCategory", maxMothCategory(categories, day));
        maxCategories.put("maxDayCategory", maxDayCategory(categories, day));

        return gson.toJson(maxCategories);
    }

    //Сериализация - создание файла
    public void saveBin(File file, Set<Category> categories) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(categories);
        }
    }

    // Десериализация - восстановление корзины
    public Set<Category> categoriesFromBinFile(File file) {
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

}

