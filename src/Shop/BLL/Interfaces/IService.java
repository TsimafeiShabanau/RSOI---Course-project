package Shop.BLL.Interfaces;

import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public interface IService
{
    Message Execute(String command, String objectJson, Class type) throws SQLException, JsonProcessingException;
}
