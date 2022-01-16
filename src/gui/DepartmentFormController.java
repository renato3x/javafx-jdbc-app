package gui;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

  private Department entity;

  private DepartmentService service;

  @FXML
  private TextField txtId;

  @FXML
  private TextField txtName;

  @FXML
  private Label labelErrorName;

  @FXML
  private Button btnContinue;

  @FXML
  private  Button btnCancel;

  public void setEntity(Department entity) {
    this.entity = entity;
  }

  public void setService(DepartmentService service) {
    this.service = service;
  }

  @FXML
  public void onBtnContinueAction(ActionEvent event) {
    if (entity == null) {
      throw new IllegalStateException("Entity was null");
    }

    if (service == null) {
      throw new IllegalStateException("Service was null");
    }

    try {
      entity = getFormData();
      service.saveOrUpdate(entity);
      Utils.getCurrentStage(event).close();
    } catch (DbException e) {
      Alerts.showAlert("Error saving Object", null, e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void obBtnCancelAction(ActionEvent event) {
    Utils.getCurrentStage(event).close();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeNodes();
  }

  private void initializeNodes() {
    Constraints.setTextFieldInteger(txtId);
    Constraints.setTextFieldMaxLength(txtName, 30);
  }

  public void updateFormData() {
    if (entity == null) {
      throw new IllegalStateException("Entity was null");
    }

    txtId.setText(entity.getId() + "");
    txtName.setText(entity.getName());
  }

  private Department getFormData() {
    Department dep = new Department();

    dep.setId(Utils.tryParseToInt(txtId.getText()));
    dep.setName(txtName.getText());

    return dep;
  }
}
