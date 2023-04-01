package Shop.View.Controllers;

import Shop.DAL.Models.*;
import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.ArrayList;

public class CartController {

    public TableColumn<ProductAmount, String> Product;
    public TableColumn<ProductAmount, Integer> Amount;
    public TableColumn<ProductAmount, Double> Price;
    public TableView<ProductAmount> table;
    public Label totalPriceLabel;

    public TextField changeAmount;
    private ProductAmount selected;
    private Cart cart;
    private Customer customer;

    @FXML
    public void initialize()
    {
        try {
            cart = Client.getInstance().getCart();
            customer = Client.getInstance().getCustomer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Product.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        UpdateTableData();

        table.setRowFactory(tv ->
        {
            TableRow<ProductAmount> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if(event.getButton().equals(MouseButton.PRIMARY) && !row.isEmpty())
                {
                    selected = row.getItem();
                    changeAmount.setText(String.valueOf(selected.getAmount()));
                }
            });
            return row;
        });
    }

    public void back() {
        Main.OpenScene("../../View/GUI/MainMenuGUI.fxml", "Главная", null);
    }

    private void UpdateTableData() {
        assert cart != null;
        ObservableList<ProductAmount> observableList = FXCollections.observableList(cart.getProducts());
        table.setItems(observableList);
        totalPriceLabel.setText(String.valueOf(cart.getTotalPrice()));
    }

    public void accept() {
        int amount = 0;
        try
        {
            amount = Integer.parseInt(changeAmount.getText());
        }
        catch (NumberFormatException ignore)
        {
        }
        if(amount > 0 && selected != null)
        {
            selected.setAmount(amount);
            table.refresh();
            totalPriceLabel.setText(String.valueOf(cart.getTotalPrice()));
        }
    }

    public void delete()
    {
        cart.getProducts().remove(selected);
        totalPriceLabel.setText(String.valueOf(cart.getTotalPrice()));
    }

    public void AcceptOrder( )
    {
        if(cart.isEmpty())
        {
            Alert empty = new Alert(Alert.AlertType.ERROR, "Корзина пуста");
            empty.showAndWait();
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Order_Product> order_products = new ArrayList<>();
        for (ProductAmount pa :
                cart.getProducts()) {
            order_products.add(new Order_Product(0, null, pa.getProduct().getId(), pa.getProduct(), pa.getAmount()));
        }
        Order order = new Order(0, null, cart.getTotalPrice(), customer, order_products, null);
        String orderJson = null;
        try {
            orderJson = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Message<Order> msg = new Message<>("CREATE", Order.class, orderJson);
        try {
            System.out.println(msg.getObject());
            Message res = Client.getInstance().Send(msg);
            System.out.println(res.getObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cart.getProducts().clear();
    }
}
