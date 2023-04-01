package Shop.DAL.Interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IGenericRepository<T>
{

    ResultSet GetAll() throws SQLException;
    ResultSet Get(int id) throws SQLException;
    void Create(T item) throws SQLException;
    void Update(T item) throws SQLException;
    void Delete(int id) throws SQLException;
    List<T> ProcessData(ResultSet result) throws SQLException;
}
