package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

  private Department entity;

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

  @FXML
  public void onBtnContinueAction() {
    System.out.println("onBtnContinueAction");
  }

  @FXML
  public void obBtnCancelAction() {
    System.out.println("obBtnCancelAction");
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
}
