package Shop.DAL.Repositories;

import Shop.DAL.Interfaces.ICategoryRepository;
import Shop.DAL.Models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository extends GenericRepository<Category> implements ICategoryRepository
{
    public CategoryRepository(String dbSet, Connection db)
    {
        super(dbSet, db);
    }

    @Override
    public void Create(Category item) throws SQLException
    {
        query = "INSERT INTO " + dbSet + " (CategoryName, CategoryDescription) VALUES (?, ?)";
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(2, item.getDescription());
        ps.executeUpdate();
    }

    @Override
    public void Update(Category item) throws SQLException
    {
        query = "UPDATE " + dbSet + " SET CategoryName = ?, CategoryDescription = ? WERE CategoryId =" + item.getId();
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(2, item.getDescription());
        ps.executeUpdate();
    }

    @Override
    public List<Category> ProcessData(ResultSet result) throws SQLException
    {
        List<Category> data = new ArrayList<>();
        while(result.next())
        {
            data.add(new Category(result.getInt(1), result.getString(2), result.getString(3),
                    new ArrayList<>()));
        }
        return data;
    }
}
