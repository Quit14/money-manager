import manager.Manager;
import manager.category.Category;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Main {

    static final File TSVFILE = new File("categories.tsv");
    static File binFile = new File("data.bin");



    public static void main(String[] args) {
        try {
            // создаем класс для анализа суммы покупок
            Manager manager = new Manager();

            // считываем tsv-файл и создаем категории товаров
            Set<Category> categories = manager.createCategories(TSVFILE);
            // считываем предыдущие записи
            if (binFile.exists()) {
                categories = manager.categoriesFromBinFile(binFile);
            }

            try (ServerSocket serverSocket = new ServerSocket(8989)) { // стартуем сервер один(!) раз
                while (true) { // в цикле(!) принимаем подключения
                    try (Socket socket = serverSocket.accept();
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                        String input = in.readLine();

                        //записываем входящие данные в одну из категорий
                        getReguest(input, categories);

                        //выдаем максимальную сумму трат
                        System.out.println(manager.printMaxCategories(categories, incomeDate(input)));
                        out.println(manager.printMaxCategories(categories, incomeDate(input)));

                        // записываем данные
                        manager.saveBin(binFile, categories);
                    }
                }

            } catch (IOException e) {
                System.out.println("Не могу стартовать сервер");
                e.printStackTrace();
            } catch (java.text.ParseException | ParseException e) {
                System.out.println("Неправильный формат запроса");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Не найден файл categories.tsv");
            e.printStackTrace();
        }
    }


    //метод чтения и добавления данных в каждую категорию
    static void getReguest(String input, Set<Category> categories) throws ParseException, java.text.ParseException {
        String incomeProduct;
        LocalDate incomeDate;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        Long incomeSum;
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(input);
        incomeProduct = (String) json.get("title");
        String date = (String) json.get("date");
        incomeDate = LocalDate.parse(date, dtf);
        incomeSum = (Long) json.get("sum");
        if (categories.stream().noneMatch(category -> category.getItems().contains(incomeProduct))) {
            categories.stream()
                    .filter(category -> category.getType().contains("другое"))
                    .forEach(category -> category.addSale(incomeDate, incomeSum));
        }
        categories.stream()
                .filter(category -> category.getItems().contains(incomeProduct))
                .forEach(category -> category.addSale(incomeDate, incomeSum));

    }
    
    static LocalDate incomeDate (String input) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(input);
        String date = (String) json.get("date");
        return  LocalDate.parse(date, dtf);
    }
}

