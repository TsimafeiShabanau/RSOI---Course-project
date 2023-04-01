package Shop.DAL.Repositories;

import Shop.DAL.Interfaces.IProductRepository;
import Shop.DAL.Models.Category_Product;
import Shop.DAL.Models.Product;
import Shop.DAL.Models.Vendor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends GenericRepository<Product> implements IProductRepository
{
    public ProductRepository(String dbSet, Connection db)
    {
        super(dbSet, db);
    }

    @Override
    public ResultSet GetAll() throws SQLException
    {
        query = "SELECT ProductId, ProductName, ProductDescription, Price, vendors.VendorId, vendors.VendorName, vendors.Email, vendors.PhoneNumber, vendors.country" +
                " FROM " + dbSet + " INNER JOIN Vendors ON vendors.vendorId = products.vendorId";
        Statement st = db.createStatement();
        return st.executeQuery(query);
    }

    @Override
    public ResultSet Get(int id) throws SQLException
    {
        query = "SELECT ProductId, ProductName, ProductDescription, Price, vendors.VendorId, vendors.VendorName, vendors.Email, vendors.PhoneNumber, vendors.country" +
                " FROM " + dbSet + "INNER JOIN Vendors ON vendors.vendorId = products.vendorId";
        Statement st = db.createStatement();
        return st.executeQuery(query);
    }

    @Override
    public void Create(Product item) throws SQLException
    {
        query = "INSERT INTO " + dbSet + " (productName, productDescription, Price,  VendorId)" +
                " VALUES (?, ?, ?, ?)";
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(2, item.getDescription());
        ps.setDouble(3, item.getPrice());
        ps.setInt(4, item.getVendor().getId());
        ps.executeUpdate();

        query = "INSERT INTO category_product values (?,?)";
        PreparedStatement ps2 = db.prepareStatement(query);
        for (Category_Product cat :
                item.getCategories())
        {
            ps2.setInt(1, cat.getCategoryId());
            ps2.setInt(2, cat.getProductId());
            ps2.executeUpdate();
        }
    }

    @Override
    public void Update(Product item) throws SQLException
    {
        query = "UPDATE " + dbSet + " SET productName = ?, productDescription = ?, Price = ?, VendorId = ?";
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(2, item.getDescription());
        ps.setDouble(3, item.getPrice());
        ps.setInt(4, item.getVendor().getId());
        ps.executeUpdate();

        query = "DELETE FROM category_product WHERE ProductId = " + item.getId();
        db.createStatement().executeQuery(query);

        query = "INSERT INTO category_product VALUES (?, ?)";
        PreparedStatement ps2 = db.prepareStatement(query);
        for (Category_Product cat :
                item.getCategories())
        {
            ps2.setInt(1, cat.getCategoryId());
            ps2.setInt(2, cat.getProductId());
            ps2.executeUpdate();
        }
    }

    @Override
    public List<Product> ProcessData(ResultSet result) throws SQLException
    {
        List<Product> data = new ArrayList<>();
        while(result.next())
        {
            data.add(new Product(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4),
                        new Vendor(result.getInt(5), result.getString(6), result.getString(7), result.getString(8),
                                result.getString(9)),
                        new ArrayList<>()));
        }
        return data;
    }

    @Override
    public ResultSet FindProductByNameAndDescriptionContentsAndCategory(Product product) throws SQLException {
        query = "SELECT ProductId, ProductName, ProductDescription, Price, vendors.VendorId, vendors.VendorName, vendors.Email, vendors.PhoneNumber, vendors.country" +
                " FROM " + dbSet + " INNER JOIN Vendors ON vendors.vendorId = products.vendorId " +
                " WHERE ProductName LIKE \"%" + product.getName() + "%\"" +
                " OR ProductDescription LIKE \"%" + product.getDescription() + "%\"";
        Statement st = db.createStatement();
        return st.executeQuery(query);
    }
}
