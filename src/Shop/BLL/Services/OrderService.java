package Shop.BLL.Services;

import Shop.BLL.Interfaces.IService;
import Shop.DAL.Interfaces.IOrderRepository;
import Shop.DAL.Models.Order;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderService implements IService
{
    IOrderRepository orderRepository;
    ObjectMapper mapper = new ObjectMapper();

    public OrderService(IOrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public Message Execute(String command, String objectJson, Class type) throws SQLException, JsonProcessingException {
        Order order = null;
        if(objectJson != null)
            order = mapper.readValue(objectJson, Order.class);
        switch (command)
        {
            case "GETCUSTOMERSORDERS":
                return GetOrdersByCustomer(order);
            case "GETALL":
                return GetAllOrders();
            case "CREATE":
                return CreateOrder(order);
            case "UPDATE":
                    return UpdateOrder(order);
            default:
                return new Message<>("ERROR", String.class, "Операция не найдена");
        }
    }

    private Message UpdateOrder(Order order) throws SQLException
    {
        orderRepository.Update(order);
        return new Message<>("SUCCESS", String.class, "Заказ успешно обновлен");
    }

    private Message<String> CreateOrder(Order order) throws SQLException
    {
        orderRepository.Create(order);
        String result = "Заказ успешно создан";
        return new Message<>("SUCCESS", String.class, result);
    }

    private Message<List> GetAllOrders() throws SQLException, JsonProcessingException {
        ResultSet res = orderRepository.GetAll();
        List<Order> orders = orderRepository.ProcessData(res);
        String ordersJson = mapper.writeValueAsString(orders);
        return new Message<>("SUCCESS", List.class, ordersJson);
    }

    private Message<List> GetOrdersByCustomer(Order order) throws SQLException, JsonProcessingException {
        ResultSet res = orderRepository.GetOrdersByCustomerId(order.getCustomer().getId());
        List<Order> orders = orderRepository.ProcessData(res);
        String ordersJson = mapper.writeValueAsString(orders);
        return new Message<>("SUCCESS", List.class, ordersJson);
    }
}
