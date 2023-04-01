package Shop.View.Controllers;

import Shop.DAL.Models.Order;
import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrdersController
{
    public ListView<Order> listView;
    private Order selectedItem;

    @FXML
    public void initialize() {
        try {
            ObservableList<Order> orders = FXCollections.observableList(GetData(
                    new Order(0, null, 0, Client.getInstance().getCustomer(), null, null)));
            listView.setItems(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SelectionModel<Order> selectionModel = listView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedItem = newValue);
    }

    public void Back()
    {
        Main.OpenScene("../../View/GUI/MainMenuGUI.fxml", "Главная", null);
    }

    private List<Order> GetData(Order order) {
        ObjectMapper mapper = new ObjectMapper();
        List<Order> list = null;
        try
        {
            String orderJson = mapper.writeValueAsString(order);
            Message msg = new Message<>("GETCUSTOMERSORDERS", Order.class, orderJson);
            msg = Client.getInstance().Send(msg);
            list = mapper.readValue(msg.getObject(), new TypeReference<List<Order>>() {
            });
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void Report()
    {
        if(selectedItem == null)
            return;
        try {
            OpenNewWindow("../../View/GUI/OrderReportGUI.fxml", "Отчет", selectedItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void OpenNewWindow(String path, String title, Order order) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

        Parent root = loader.load();
        OrderReportController controller = loader.getController();
        controller.SetOrder(order);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
