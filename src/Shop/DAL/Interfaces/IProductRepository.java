package Shop.DAL.Interfaces;

import Shop.DAL.Models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IProductRepository extends IGenericRepository<Product>
{
    ResultSet FindProductByNameAndDescriptionContentsAndCategory(Product product) throws SQLException;
}
