package Shop.Infrastructure.Server;

import Shop.DAL.DB.DbContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Server
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket ss = new ServerSocket(7775);
            System.out.println("Сервер запущен");
            String connectionString = "jdbc:mysql://localhost:3306/shop?verifyServerCertificate=false" +
                    "&useSSL=false&requireSSL=false&useLegacyDateTimeCode=false&amp&serverTimezone=UTC";
            DbContext db = new DbContext(connectionString, "root", "1234");
            if(args.length > 0)
            {
                LinkedHashSet<String> argsSet = new LinkedHashSet<>(Arrays.asList(args));
                for (String arg :
                        argsSet) {
                    if(arg.equals("-init"))
                        db.Initialize();
                }
            }
            while (true)
            {
                Socket client = ss.accept();
                System.out.println("Новый клиент присоеденился к серверу");

                Thread t = new ClientHandler(client, db.getConnection());
                t.start();
            }
        }
        catch (IOException | SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

