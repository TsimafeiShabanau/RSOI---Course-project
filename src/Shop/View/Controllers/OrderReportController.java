package Shop.View.Controllers;

import Shop.DAL.Models.Order;
import Shop.DAL.Models.Order_Product;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OrderReportController
{
    public Label Label;

    private String reportText;

    public void Save()
    {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(new JButton("Сохранить")) == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(reportText);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Back()
    {
        Stage current = (Stage) Label.getScene().getWindow();
        current.close();
    }

    public void SetOrder(Order o)
    {
        assert o != null;
        System.out.println(o.getProducts().size());
        reportText =
                "           Заказ номер " + o.getId() + "\n" +
                "Статус: " + o.getStatus() +
                "\n\nТовары:\n\n";
        for (Order_Product op : o.getProducts())
        {
            reportText += op.getProduct().getName() + " " + "x" + op.getAmount() + "    " +
                    op.getProduct().getVendorName() + " " + op.getProduct().getVendor().getCountry() +
                    "   " + op.getProduct().getPrice() + "$\n";
        }
        reportText += "\nОбщая стоимость: " + o.getTotalPrice() ;
        reportText += "\n\nПокупатель " + o.getCustomer().getName() + "    " + o.getDatetime();

        Label.setText(reportText);
    }
}
