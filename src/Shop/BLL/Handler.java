package Shop.BLL;

import Shop.BLL.Interfaces.IService;
import Shop.BLL.Services.OrderService;
import Shop.BLL.Services.ProductService;
import Shop.BLL.Services.UserService;
import Shop.DAL.Interfaces.IUnitOfWork;
import Shop.DAL.Models.Customer;
import Shop.DAL.Models.Order;
import Shop.DAL.Models.Product;
import Shop.DAL.Models.User;
import Shop.DAL.UnitOfWork;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Handler
{
    private Dictionary<Class, IService> serviceDictionary;

    public Handler(Connection db)
    {
        IUnitOfWork unitOfWork = new UnitOfWork(db);
        UserService userService = new UserService(unitOfWork.getUsers(), unitOfWork.getCustomers());

        serviceDictionary = new Hashtable<>();
        serviceDictionary.put(User.class, userService);
        serviceDictionary.put(Customer.class, userService);
        serviceDictionary.put(Product.class, new ProductService(unitOfWork.getProducts()));
        serviceDictionary.put(Order.class, new OrderService(unitOfWork.getOrders()));
    }

    public Message Handle(Message message) throws SQLException, JsonProcessingException
    {
        IService service = serviceDictionary.get(message.getType());
        if(service == null)
            return new Message<>("ERROR", String.class, "Операция недоступна");

        return service.Execute(message.getHead(), message.getObject(), message.getType());
    }
}
