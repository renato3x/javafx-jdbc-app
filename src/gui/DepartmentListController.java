package gui;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

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
  public void onButtonNewAction() {
    System.out.println("onButtonNewAction()");
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
}
