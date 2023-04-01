package Shop.Infrastructure.Client;

import Shop.DAL.Models.Cart;
import Shop.DAL.Models.Customer;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    private static Client instance;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private Customer customer = null;
    private Cart cart = new Cart();

    public static Client getInstance() throws IOException
    {
        if(instance == null)
        {
            instance = new Client();
            instance.socket = new Socket(InetAddress.getByName("localhost"), 7775);
            instance.input = new DataInputStream(instance.socket.getInputStream());
            instance.output = new DataOutputStream(instance.socket.getOutputStream());
        }
        return instance;
    }

    public Message Send(Message message) throws IOException
    {
        String messageStr = message.toString();
        output.writeUTF(messageStr);
        if(message.getHead().equals("EXIT"))
            return null;
        String response = input.readUTF();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, Message.class);
    }

    void Close() throws IOException
    {
        Send(new Message<>("EXIT", String.class, "EXIT"));
        socket.close();
        input.close();
        output.close();
    }

    public Customer getCustomer()
    {
        return customer;
    }
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
