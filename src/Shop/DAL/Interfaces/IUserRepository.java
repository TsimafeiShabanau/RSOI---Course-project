package Shop.DAL.Interfaces;

import Shop.DAL.Models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IUserRepository extends IGenericRepository<User>
{
    ResultSet FindUserByLoginAndPassword(User user) throws SQLException;
}
