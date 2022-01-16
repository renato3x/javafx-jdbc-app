package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

  private Seller entity;

  private SellerService service;

  private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

  @FXML
  private TextField txtId;

  @FXML
  private TextField txtName;

  @FXML
  private TextField txtEmail;

  @FXML
  private DatePicker dpBirthDate;

  @FXML
  private TextField txtBaseSalary;

  @FXML
  private Label labelErrorName;

  @FXML
  private Label labelErrorEmail;

  @FXML
  private Label labelErrorBirthDate;

  @FXML
  private Label labelErrorBaseSalary;

  @FXML
  private Button btnContinue;

  @FXML
  private  Button btnCancel;

  public void setEntity(Seller entity) {
    this.entity = entity;
  }

  public void setService(SellerService service) {
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
    Constraints.setTextFieldMaxLength(txtName, 70);
    Constraints.setTextFieldDouble(txtBaseSalary);
    Constraints.setTextFieldMaxLength(txtEmail, 60);
    Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
  }

  public void updateFormData() {
    if (entity == null) {
      throw new IllegalStateException("Entity was null");
    }

    txtId.setText(entity.getId() + "");
    txtName.setText(entity.getName());
    txtEmail.setText(entity.getEmail());
    Locale.setDefault(Locale.US);
    txtBaseSalary.setText(entity.getBaseSalary() + "");

    if (entity.getBirthDate() != null) {
      dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
    }
  }

  private Seller getFormData() {
    Seller dep = new Seller();

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
