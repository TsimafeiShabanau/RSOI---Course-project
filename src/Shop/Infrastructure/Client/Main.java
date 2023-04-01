package Shop.Infrastructure.Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application
{
    private static Stage stage;
    @Override
    public void start(Stage stage)
    {
        Main.stage = stage;
        OpenScene("../../View/GUI/AuthorisationGUI.fxml", "Авторизация", null);
    }
    @Override
    public void stop()
    {
        try
        {
            Client.getInstance().Close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
    public static void OpenScene(String path, String title, EventHandler<WindowEvent> onShowing)
    {
        try
        {
            Parent root = FXMLLoader.load(Main.class.getResource(path));
            stage.setTitle(title);
            assert root != null;
            stage.setScene(new Scene(root, 600, 500));
            stage.show();
            if(onShowing != null)
                stage.setOnShowing(onShowing);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
