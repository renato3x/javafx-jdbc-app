package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

  private Department entity;

  private DepartmentService service;

  private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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

  public void subscribeDataChangeListener(DataChangeListener listener) {
    dataChangeListeners.add(listener);
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
      notifyDataChangeListener();
      Utils.getCurrentStage(event).close();
    } catch (DbException e) {
      Alerts.showAlert("Error saving Object", null, e.getMessage(), Alert.AlertType.ERROR);
    } catch (ValidationException e) {
      setErrorMessage(e.getErrors());
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

    ValidationException exception = new ValidationException("Validation error");

    dep.setId(Utils.tryParseToInt(txtId.getText()));

    if (txtName.getText() == null || txtName.getText().trim().equals("")) {
      exception.addError("name", "Field can't be empty");
    }

    dep.setName(txtName.getText());

    if (exception.getErrors().size() > 0) {
      throw exception;
    }

    return dep;
  }

  private void notifyDataChangeListener() {
    for (DataChangeListener listener : dataChangeListeners) {
      listener.onDataChanged();
    }
  }

  private void setErrorMessage(Map<String, String> errors) {
    Set<String> fields = errors.keySet();

    if (fields.contains("name")) {
      labelErrorName.setText(errors.get("name"));
    }
  }
}
