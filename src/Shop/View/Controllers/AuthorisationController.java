package Shop.View.Controllers;

import Shop.DAL.Models.Customer;
import Shop.DAL.Models.User;
import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Base64;

public class AuthorisationController
{
    public TextField password;
    public TextField login;
    public Label ErrorField;

    public void MouseLog() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        User user;
        try
        {
            String encodedPass = Base64.getEncoder().encodeToString(password.getText().getBytes());
            user = new User(0, login.getText(), encodedPass, "", null);
        } catch (Exception e)
        {
            e.printStackTrace();
            ErrorField.setText("Ошибка шифрования");
            return;
        }
        Message message = new Message<>("LOGIN", User.class, mapper.writeValueAsString(user));

        Message responseMessage = Client.getInstance().Send(message);
        if(responseMessage.getHead().equals("ERROR") || responseMessage.getHead().equals("FAILURE"))
            ErrorField.setText(responseMessage.getObject());
        else
        {
            Customer customer = mapper.readValue(responseMessage.getObject(), Customer.class);
            Client.getInstance().setCustomer(customer);
            if(customer.getRole().equals("blocked"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Пользователь заблокирован.");
                alert.showAndWait();
                return;
            }
            Main.OpenScene("../../View/GUI/MainMenuGUI.fxml", "Главная", null);
        }
    }

    public void MouseReg()
    {
        Main.OpenScene("../../View/GUI/RegistrationGUI.fxml", "Регистрация", null);
    }
}
