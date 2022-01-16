package gui;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

  private DepartmentService service;

  private ObservableList<Department> obsList;

  @FXML
  private TableView<Department> tableViewDepartments;

  @FXML
  private TableColumn<Department, Integer> tableColumnId;

  @FXML
  private TableColumn<Department, String> tableColumnName;

  @FXML
  private TableColumn<Department, Department> tableColumnEDIT;

  @FXML
  TableColumn<Department, Department> tableColumnREMOVE;

  @FXML
  private Button buttonNew;

  @FXML
  public void onButtonNewAction(ActionEvent event) {
    Department dep = new Department();
    createDialogForm(dep, "/gui/DepartmentForm.fxml", Utils.getCurrentStage(event));
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

    initEditButtons();
    initRemoveButtons();
  }

  private void createDialogForm(Department dep, String absoluteName, Stage parentStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = loader.load();

      DepartmentFormController controller = loader.getController();
      controller.setEntity(dep);
      controller.setService(new DepartmentService());
      controller.subscribeDataChangeListener(this);
      controller.updateFormData();

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

  @Override
  public void onDataChanged() {
    updatedTableView();
  }

  private void initEditButtons() {
    tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnEDIT.setCellFactory(param -> new TableCell<>() {
      private final Button button = new Button("edit");
      @Override
      protected void updateItem(Department obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.getCurrentStage(event)));
      }
    });
  }

  private void initRemoveButtons() {
    tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
      private final Button button = new Button("remove");
      @Override
      protected void updateItem(Department obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        button.setOnAction(event -> removeEntity(obj));
      }
    });
  }

  private void removeEntity(Department dep) {
    Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

    if (result.get() == ButtonType.OK) {
      if (service == null) {
        throw new IllegalStateException("Service was null");
      }

      try {
        service.remove(dep);
        updatedTableView();
      } catch (DbIntegrityException e) {
        Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
      }
    }
  }
}
