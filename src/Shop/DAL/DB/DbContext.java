package Shop.DAL.DB;

import Shop.DAL.Models.*;
import Shop.DAL.UnitOfWork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

public class DbContext
{
    private Connection connection;

    public DbContext(String connectionString, String user, String password) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString, user, password);
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void Initialize() throws SQLException {
        UnitOfWork uof = new UnitOfWork(connection);
        uof.getUsers().Create(new User(0, "makss", Base64.getEncoder().encodeToString(String.format("1311").getBytes()), "maksOrso@gmail.com", new Role(2, "user")));
        uof.getUsers().Create(new User(0, "agonn", Base64.getEncoder().encodeToString(String.format("yan").getBytes()), "agoniamun@gmail.com", new Role(2, "user")));
        uof.getUsers().Create(new User(0, "sofi", Base64.getEncoder().encodeToString(String.format("claps").getBytes()), "sofi563zar@mail.ru", new Role(2, "user")));
        uof.getUsers().Create(new User(0,"pashka", Base64.getEncoder().encodeToString(String.format("qwerty").getBytes()), "pavelMur@gmail.com", new Role(1, "admin")));

        uof.getCustomers().Create(new Customer(0, "Максим Орсов", "+375294356689", new User(40, "", "", "", null)));
        uof.getCustomers().Create(new Customer(0, "Агния Мун", "+375298337857", new User(41, "", "", "", null)));
        uof.getCustomers().Create(new Customer(0, "София Зарапина", "+375335437714", new User(42, "", "", "", null)));
        uof.getCustomers().Create(new Customer(0, "Павел Мурин", "+375449648837", new User(43, "", "", "", null)));

        uof.getCategories().Create(new Category(0, "Смартфоны", "смартфоны", null));
        uof.getCategories().Create(new Category(0, "Телевизоры", "телевизоры", null));

        uof.getVendors().Create(new Vendor(10, "Apple", "appleCompany@apple.com", "94045632", "US"));
        uof.getVendors().Create(new Vendor(11, "LG", "lgTex.com", "43657573", "USA"));
        uof.getVendors().Create(new Vendor(12, "Slmsung", "phoneslmsung.com", "45463244", "USA"));

        uof.getProducts().Create(new Product(7, "iPhone 11", "Характеристики....", 2000, new Vendor(10, "", "","",""), null));
        uof.getProducts().Create(new Product(8, "Lg HD TV", "Характеристики....", 1300, new Vendor(11, "", "","",""),null ));
        uof.getProducts().Create(new Product(9, "Samsung Galaxy s8", "Характеристики....", 800, new Vendor(12, "", "","",""), null));
    }
}
