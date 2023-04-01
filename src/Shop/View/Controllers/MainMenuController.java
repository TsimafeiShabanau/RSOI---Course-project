package Shop.View.Controllers;

import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController
{
    public Button Management;

    @FXML
    public void initialize() {
        try {
            if (Client.getInstance().getCustomer().getUser().getRole().getName().toUpperCase().equals("ADMIN"))
                Management.setVisible(true);
            else
                Management.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cart()
    {
        Main.OpenScene("../../View/GUI/CartGUI.fxml", "Корзина", null);
    }

    public void orders()
    {
        Main.OpenScene("../../View/GUI/OrdersGUI.fxml", "Заказы", null);
    }

    public void adminMenu()
    {
        Main.OpenScene("../../View/GUI/AdminGUI.fxml", "Управление", null);
    }

    public void products()
    {
        Main.OpenScene("../../View/GUI/ProductsListGUI.fxml", "Продукты", null);
    }
}
