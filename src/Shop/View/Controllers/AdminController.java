package Shop.View.Controllers;

import Shop.DAL.Models.Customer;
import Shop.DAL.Models.Order;
import Shop.DAL.Models.Role;
import Shop.DAL.Models.User;
import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class AdminController
{

    public TableView<Customer> users;
    public TableColumn<Customer, String> userName;
    public TableColumn<Customer, String> userLogin;
    public TableColumn<Customer, String> userMail;
    public TableColumn<Customer, String> userRole;

    public TableView<Order> orders;
    public TableColumn<Order, Integer> orderNumber;
    public TableColumn<Order, Double> orderPrice;
    public TableColumn<Order, Date> orderDate;
    public TableColumn<Order, String> orderCustomerName;
    public TableColumn<Order, String> orderCustomerMail;
    public TableColumn<Order, String> orderStatus;
    public LineChart<String, Double> lineChart;
    public NumberAxis yAxis;
    public CategoryAxis xAxis;

    @FXML
    public void initialize()
    {
        userName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        userLogin.setCellValueFactory(new PropertyValueFactory<>("Login"));
        userMail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        userRole.setCellValueFactory(new PropertyValueFactory<>("Role"));

        orderNumber.setCellValueFactory(new PropertyValueFactory<>("Id"));
        orderPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        orderDate.setCellValueFactory(new PropertyValueFactory<>("Datetime"));
        orderCustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        orderCustomerMail.setCellValueFactory(new PropertyValueFactory<>("CustomerEmail"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        ObservableList<Customer> usersList = FXCollections.observableList(GetCustomers());
        ObservableList<Order> ordersList = FXCollections.observableList(GetOrders());

        users.setItems(usersList);
        orders.setItems(ordersList);

        CreateUsersCMAndHandlers();
        CreateOrdersCMAndHandlers();

        lineChart.setTitle("Статистика по заказам");
        lineChart.setLegendSide(Side.LEFT);
        xAxis.setLabel("Дата");
        yAxis.setLabel("Сумма");

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Статистка покупок");
        for (Order o : ordersList)
        {
            series.getData().add(new XYChart.Data<>(o.getDatetime().toString(), o.getTotalPrice()));
        }
        lineChart.getData().add(series);
    }

    public void Back()
    {
        Main.OpenScene("../../View/GUI/MainMenuGUI.fxml", "Главная", null);
    }

    private List<Customer> GetCustomers() {
        ObjectMapper mapper = new ObjectMapper();
        List<Customer> list = null;
        Message msg;
        try
        {
            msg = new Message<>("GETCUSTOMERS", Customer.class, null);
            msg = Client.getInstance().Send(msg);
            list = mapper.readValue(msg.getObject(), new TypeReference<List<Customer>>() {});
        }
        catch (IOException ex) {
            assert false;
            ex.printStackTrace();
        }
        return list;
    }
    private List<Order> GetOrders() {
        ObjectMapper mapper = new ObjectMapper();
        List<Order> list = null;
        Message msg;
        try
        {
            msg = new Message<>("GETALL", Order.class, null);
            msg = Client.getInstance().Send(msg);
            list = mapper.readValue(msg.getObject(), new TypeReference<List<Order>>() {});
        }
        catch (IOException ex) {
            assert false;
            ex.printStackTrace();
        }
        return list;
    }

    private void CreateOrdersCMAndHandlers()
    {
        orders.setRowFactory(param ->
        {
            final TableRow<Order> row = new TableRow<>();
            final ContextMenu cmOrders = new ContextMenu();
            final MenuItem pending = new MenuItem("Pending");
            final MenuItem accepted = new MenuItem("Accepted");
            final MenuItem rejected = new MenuItem("Rejected");
            final MenuItem custom = new MenuItem("Custom");

            pending.setOnAction(event ->
            {
                Order selected = orders.getItems().get(row.getIndex());
                selected.setStatus("Pending");
                orders.refresh();
                Send(selected);
            });
            accepted.setOnAction(event ->
            {
                Order selected = orders.getItems().get(row.getIndex());
                selected.setStatus("Accepted");
                orders.refresh();
                Send(selected);
            });
            rejected.setOnAction(event ->
            {
                Order selected = orders.getItems().get(row.getIndex());
                selected.setStatus("Rejected");
                orders.refresh();
                Send(selected);
            });
            custom.setOnAction(event ->
            {
                Order selected = orders.getItems().get(row.getIndex());
                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Введите статус");
                td.showAndWait();
                if(td.getResult().isEmpty())
                    return;
                selected.setStatus(td.getResult());
                orders.refresh();
                Send(selected);
            });
            cmOrders.getItems().addAll(pending, accepted, rejected, custom);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(cmOrders));
            return row;
        });
    }
    private void CreateUsersCMAndHandlers()
    {
        users.setRowFactory(param ->
        {
            final TableRow<Customer> row = new TableRow<>();
            final ContextMenu cmUsers = new ContextMenu();
            final MenuItem ban = new MenuItem("Заблокировать");
            final MenuItem pardon = new MenuItem("Разблокировать");
            final MenuItem giveAdminRights = new MenuItem("Сделать администратором");
            final MenuItem reduceAdminRights = new MenuItem("Сделать пользователем");
            ban.setOnAction(event ->
            {
                User selected = users.getItems().get(row.getIndex()).getUser();
                selected.setRole(new Role(3, "blocked"));
                users.refresh();
                Send(selected);
            });
            pardon.setOnAction(event ->
            {
                User selected = users.getItems().get(row.getIndex()).getUser();
                selected.setRole(new Role(2, "user"));
                users.refresh();
                Send(selected);
            });
            giveAdminRights.setOnAction(event ->
            {
                User selected = users.getItems().get(row.getIndex()).getUser();
                selected.setRole(new Role(1, "admin"));
                users.refresh();
                Send(selected);
            });
            reduceAdminRights.setOnAction(pardon.getOnAction());
            cmUsers.getItems().addAll(ban, pardon, giveAdminRights, reduceAdminRights);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(cmUsers));
            return row;
        });
    }
    private void Send(User user)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String json = mapper.writeValueAsString(user);
            Client.getInstance().Send(new Message<>("CHANGEROLE", User.class, json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void Send(Order order)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String json = mapper.writeValueAsString(order);
            Client.getInstance().Send(new Message<>("UPDATE", Order.class, json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
