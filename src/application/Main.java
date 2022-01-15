package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

  private static Scene mainScene;

  public static Scene getMainScene() {
    return mainScene;
  }

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

    mainScene = new Scene(root);

    stage.setTitle("Sample JavaFX Application");
    stage.setScene(mainScene);

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
