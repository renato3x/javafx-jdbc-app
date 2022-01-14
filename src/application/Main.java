package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
    ScrollPane root = loader.load();

    root.setFitToWidth(true);
    root.setFitToHeight(true);

    /*
     * Os comandos setFitTo... fazem com que o elemento possua o tamanho total da
     * tela
     */

    Scene scene = new Scene(root);

    stage.setTitle("Sample JavaFX Application");
    stage.setScene(scene);

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
