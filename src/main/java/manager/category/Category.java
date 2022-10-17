package manager.category;

import java.io.*;
import java.util.*;
import java.util.function.ToLongFunction;

public class Category implements Serializable {

    private String type;
    private List<String> items; // товары, входящие в категорию
    private HashMap<Date, Long> buyingLog; // связка "дата - сумма покупки"

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

    public HashMap<Date, Long> getBuyingLog() {
        return buyingLog;
    }

    //метод добавления даты и суммы покупки
    public void addSale(Date date, Long sum) {
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
        return buyingLog.values().stream().mapToLong(l -> l).sum();
    }

    // TODO: 12.10.2022 метод для подсчета самой большой покупки
    // TODO: 12.10.2022 метод для подсчета суммы за год
    // TODO: 12.10.2022 расчет суммы за месяц
    // TODO: 12.10.2022 расчет суммы за день

}


