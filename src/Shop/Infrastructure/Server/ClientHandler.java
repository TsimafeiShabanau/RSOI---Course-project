package Shop.Infrastructure.Server;

import Shop.BLL.Handler;
import Shop.DAL.Models.Customer;
import Shop.DAL.Models.Product;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class ClientHandler extends Thread
{
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Socket client;
    private final Handler requestHandler;
    private LogService logService;
    private Customer current;

    ClientHandler(Socket s, Connection db) throws IOException
    {
        client = s;
        input = new DataInputStream(s.getInputStream());
        output = new DataOutputStream(s.getOutputStream());
        requestHandler = new Handler(db);
    }

    @Override
    public void run()
    {
        String request;
        String response = "";
        ObjectMapper mapper = new ObjectMapper();
        while (true)
        {
            try
            {
                request = input.readUTF();
                Message message = mapper.readValue(request, Message.class);
                if(message.getHead().equals("EXIT"))
                {
                    if(logService != null)
                    {
                        logService.AddLog("Пользователь вышел");
                        logService.Save();
                    }
                    else
                        System.out.println("Пользователь вышел");
                    client.close();
                    return;
                }
                LogHandler(message);
                Message handledMessage = requestHandler.Handle(message);
                response = mapper.writeValueAsString(handledMessage);
                if(message.getHead().equals("LOGIN"))
                {
                    current = mapper.readValue(handledMessage.getObject(), Customer.class);
                    logService = new LogService(current, client);
                    logService.AddLog("Пользователь вошел в систему");
                }
            }
            catch (IOException | SQLException | ClassCastException e)
            {
                e.printStackTrace();
                try
                {
                    response = mapper.writeValueAsString(new Message<>("ERROR", String.class, "Ошибка на стороне сервера"));
                } catch (JsonProcessingException ex)
                {
                    ex.printStackTrace();
                }
            }
            finally
            {
                try
                {
                    if(!client.isClosed())
                        output.writeUTF(response);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void LogHandler(Message msg) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        switch (msg.getHead())
        {
            case "GETCUSTOMERORDERS":
                logService.AddLog("Пользователь просматривает заказы");
                break;
            case "GETALL":
                logService.AddLog("Пользователь просматривает список товаров");
                break;
            case "FIND":
                Product product = mapper.readValue(msg.getObject(), Product.class);
                logService.AddLog("Пользователь ищет товары. Строка: '" + product.getName() + "'");
                break;
        }
    }
}
