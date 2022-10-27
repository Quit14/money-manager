package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    protected static class MaxCategory {
        String category;
        long sum;

        public MaxCategory(String category, long sum) {
            this.category = category;
            this.sum = sum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MaxCategory that = (MaxCategory) o;
            return sum == that.sum && Objects.equals(category, that.category);
        }

        @Override
        public int hashCode() {
            return Objects.hash(category, sum);
        }
    }

    //  определяем категорию с наибольшей абсолютной суммой трат:
    protected MaxCategory maxCategory (Set<Category> categories) {
        List<Category> comparableCategories = categories.stream()
                .sorted(comparatorMaxSum)
                .collect(Collectors.toList());
        Category maxCategoryValue = comparableCategories.get(comparableCategories.size() - 1);
        return new MaxCategory(maxCategoryValue.getType(), maxCategoryValue.totalSum());
    }

    // категория с наибольшими тратами за год:
    protected MaxCategory maxYearCategory(Set<Category> categories, LocalDate day) {
        Category maxYearCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalYearSum(day) > maxSum) {
                maxYearCategory = cat;
                maxSum = cat.totalYearSum(day);
            }
        }
        return new MaxCategory(maxYearCategory.getType(), maxSum);
    }


    // категория с наибольшими тратами за месяц:
    protected MaxCategory maxMothCategory(Set<Category> categories, LocalDate day) {
        Category maxMothCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalMothSum(day) > maxSum) {
                maxMothCategory = cat;
                maxSum = cat.totalMothSum(day);
            }
        }
        return new MaxCategory(maxMothCategory.getType(), maxSum);
    }

    // категория с наибольшими тратами за день:
    protected MaxCategory maxDayCategory(Set<Category> categories, LocalDate day) {
        Category maxDayCategory = null;
        long maxSum = Integer.MIN_VALUE;
        for (Category cat : categories) {
            if (cat.totalDaySum(day) > maxSum) {
                maxDayCategory = cat;
                maxSum = cat.totalDaySum(day);
            }
        }
        return new MaxCategory (maxDayCategory.getType(), maxSum);

    }

    public String printMaxCategories(Set<Category> categories, LocalDate day) {
        Map<String, MaxCategory> maxCategories = new LinkedHashMap<>();
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

