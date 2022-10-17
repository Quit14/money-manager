package manager.category;

import java.io.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

public class Category implements Serializable {

    private String type;
    private List<String> items; // товары, входящие в категорию
    private HashMap<LocalDate, Long> buyingLog; // связка "дата - сумма покупки"

    public Category(String type, List<String> items) {
        this.type = type;
        this.items = items;
        this.buyingLog = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public List<String> getItems() {
        return items;
    }

    public HashMap<LocalDate, Long> getBuyingLog() {
        return buyingLog;
    }

    //метод добавления даты и суммы покупки
    public void addSale(LocalDate date, Long sum) {
        //если в один день покупок несколько
        if (buyingLog.containsKey(date)) {
            Long currentSum = buyingLog.get(date) + sum;
            buyingLog.put(date, currentSum);
        }
        // если покупка одна
        if (!buyingLog.containsKey(date)) {
            buyingLog.put(date, sum);
        }
    }

    // подсчет общей суммы покупок в категории
    public long totalSum() {
        if (!buyingLog.isEmpty()) {
            return buyingLog.values().stream().mapToLong(l -> l).sum();
        }
        return 0;
    }

    //подсчет суммы за год
    public long totalYearSum(LocalDate day) {
        if (!buyingLog.isEmpty()) {
            LocalDate yearAgo = day.minusYears(1);
            return buyingLog.entrySet().stream()
                    .filter(date -> date.getKey().isAfter(yearAgo))
                    .mapToLong(value -> value.getValue()).sum();
        }
        return 0;
    }

    // подсчет суммы за месяц
    public long totalMothSum(LocalDate day) {
        if (!buyingLog.isEmpty()) {
            LocalDate mothAgo = day.minusMonths(1);
            return buyingLog.entrySet().stream()
                    .filter(date -> date.getKey().isAfter(mothAgo))
                    .mapToLong(value -> value.getValue()).sum();
        }
        return 0;
    }

    // подсчет суммы за день
    public long totalDaySum(LocalDate day) {
        if (!buyingLog.isEmpty()) {
            return buyingLog.get(day);
        }
        return 0;
    }

}


