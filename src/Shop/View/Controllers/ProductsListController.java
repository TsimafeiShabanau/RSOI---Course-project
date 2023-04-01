package Shop.View.Controllers;

import Shop.DAL.Models.Product;
import Shop.DAL.Models.ProductAmount;
import Shop.Infrastructure.Client.Client;
import Shop.Infrastructure.Client.Main;
import Shop.Infrastructure.Models.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class ProductsListController
{

    public TextField search;
    public TableView<Product> Table;

    public TableColumn<Product, String> Name;
    public TableColumn<Product, Double> Price;
    public TableColumn<Product, String> Description;
    public TableColumn<Product, String> VendorName;

    @FXML
    public void initialize()
    {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Введите количество");

        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        VendorName.setCellValueFactory(new PropertyValueFactory<>("VendorName"));
        UpdateTableData(GetData("GETALL", null));
//создает новые
        Table.setRowFactory(tv ->
        {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if(event.getClickCount() == 2 && !row.isEmpty())
                {
                    int amount = 0;
                    td.showAndWait();
                    try
                    {
                        amount = Integer.parseInt(td.getResult());
                    }
                    catch (NumberFormatException ignored)
                    {
                    }
                    if(amount > 0)
                    {
                        try {
                            Client.getInstance().getCart().getProducts().add(new ProductAmount(row.getItem(), amount));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return row;
        });
    }

    private List<Product> GetData(String operation, Product product) {
        ObjectMapper mapper = new ObjectMapper();
        String prod;
        List<Product> list = null;
        try
        {
            prod = mapper.writeValueAsString(product);
            Message msg = new Message<>(operation, Product.class, prod);
            msg = Client.getInstance().Send(msg);
            list = mapper.readValue(msg.getObject(), new TypeReference<List<Product>>() {
            });
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private void UpdateTableData(List<Product> data)
    {
            ObservableList<Product> observableList = FXCollections.observableList(data);
            Table.setItems(observableList);
    }

    public void Find()
    {
        String searchString = search.getText();
        Product prod = new Product(0, searchString, searchString, 0, null, null);
        UpdateTableData(GetData("FIND", prod));
    }

    public void back()
    {
        Main.OpenScene("../../View/GUI/MainMenuGUI.fxml", "Главная", null);
    }
}
