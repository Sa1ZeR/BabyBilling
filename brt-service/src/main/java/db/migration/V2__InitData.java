package db.migration;

import lombok.val;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

public class V2__InitData extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Random random = new Random();

        //номера оператора ромашки
        List<String> phones = List.of("79554014636", "79553488401", "79550398772", "79552658665",
                "79552168075", "79553669810", "79555651417", "79550471859", "79559779972",
                "79555857898", "79550410514", "79551006150", "79551975058", "79551302983",
                "79555478980", "79557581785", "79552919091", "79553513426", "79551529009",
                "79550790113", "79550289408", "79555678757", "79553208309", "79551967402",
                "79550144220", "79553648563", "79554831095");

        Connection connection = context.getConnection();
        var sql = """
                INSERT INTO tariffs (id, name) VALUES (11, 'Классика'), (12, 'Помесячный')
                """;
        try(var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        var customerSql = """
                INSERT INTO customers (balance, tariff_id, msisnd, password) VALUES (%s, %s, '%s', '%s')
                """;
        var managerSQl = """
                INSERT INTO customers_to_roles VALUES (%s, %s)
                """;
        //заполняем базу тестовыми пользователями
        var tariffArray = new int[]{11, 12};
        var bcrypt = new BCryptPasswordEncoder();
        int id = 1;
        for(String s : phones) {
            var balance = random.nextInt(900) + 350;
            var tariff = tariffArray[random.nextInt(2)];
            var password = bcrypt.encode("123456");

            try(var statement = connection.createStatement()) {
                statement.execute(String.format(customerSql, balance, tariff, s, password));
                statement.execute(String.format(managerSQl, 0, id));
            }
            id++;
        }

        //выдаем менеджера пользователю
        try(var statement = connection.createStatement()) {
            statement.execute(String.format(managerSQl, 1, 1));
        }

        connection.commit();
    }


}
