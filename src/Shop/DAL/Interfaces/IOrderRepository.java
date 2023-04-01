package Shop.DAL.Interfaces;

import Shop.DAL.Models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IOrderRepository extends IGenericRepository<Order>
{
    ResultSet GetOrdersByCustomerId(int customerId) throws SQLException;
}
