package Shop.BLL.Services;

import Shop.BLL.Interfaces.IService;
import Shop.DAL.Interfaces.IProductRepository;
import Shop.DAL.Models.Product;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductService implements IService
{
    private IProductRepository productRepository;
    private ObjectMapper objectMapper;

    public ProductService(IProductRepository productRepository)
    {
        this.productRepository = productRepository;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Message Execute(String command, String objectJson, Class type) throws SQLException, JsonProcessingException
    {
        Product product = objectMapper.readValue(objectJson, Product.class);
        switch(command)
        {
            case "GETALL":
                return GetAllProducts();
            case "FIND":
                return FindProducts(product);
            default:
                return new Message<>("ERROR", String.class, "Операция не найдена");
        }
    }

    private Message FindProducts(Product product) throws SQLException, JsonProcessingException {
        ResultSet res = productRepository.FindProductByNameAndDescriptionContentsAndCategory(product);
        List<Product> products = productRepository.ProcessData(res);
        String productsJson = objectMapper.writeValueAsString(products);
        return new Message("SUCCESS", List.class, productsJson);
    }

    private Message GetAllProducts() throws SQLException, JsonProcessingException
    {
        ResultSet res = productRepository.GetAll();
        List<Product> products = productRepository.ProcessData(res);
        String productsJson = objectMapper.writeValueAsString(products);
        return new Message<>("SUCCESS", List.class, productsJson);
    }
}
