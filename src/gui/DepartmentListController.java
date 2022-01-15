package gui;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

  private DepartmentService service;

  private ObservableList<Department> obsList;

  @FXML
  private TableView<Department> tableViewDepartments;

  @FXML
  private TableColumn<Department, Integer> tableColumnId;

  @FXML
  private TableColumn<Department, String> tableColumnName;

  @FXML
  private Button buttonNew;

  @FXML
  public void onButtonNewAction(ActionEvent event) {
    createDialogForm("/gui/DepartmentForm.fxml", Utils.getCurrentStage(event));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

    Stage stage = (Stage) Main.getMainScene().getWindow();

    tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
  }

  public void setService(DepartmentService service) {
    this.service = service;
  }

  public void updatedTableView() {
    if (service == null) {
      throw new IllegalStateException("Service was null");
    }

    List<Department> departments = service.findAll();

    obsList = FXCollections.observableArrayList(departments);

    tableViewDepartments.setItems(obsList);
  }

  private void createDialogForm(String absoluteName, Stage parentStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = loader.load();

      Stage dialogStage = new Stage();

      dialogStage.setTitle("Enter Department Data");
      dialogStage.setScene(new Scene(pane));
      dialogStage.setResizable(false);
      dialogStage.initOwner(parentStage);
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
    }
  }
}
