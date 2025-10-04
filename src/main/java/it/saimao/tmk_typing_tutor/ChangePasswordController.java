package it.saimao.tmk_typing_tutor;

import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.utils.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private DialogPane changePasswordDialog;
    
    @FXML
    private PasswordField pfOldPassword;
    
    @FXML
    private PasswordField pfNewPassword;
    
    @FXML
    private PasswordField pfConfirmPassword;
    
    @FXML
    private Label lblPasswordStatus;
    
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the OK button action
        changePasswordDialog.lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!changePassword()) {
                event.consume(); // Prevent dialog from closing if password change failed
            }
        });
    }
    
    public void initData(User user) {
        this.user = user;
    }
    
    private boolean changePassword() {
        String oldPassword = pfOldPassword.getText();
        String newPassword = pfNewPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (!user.getPassword().equals(oldPassword)) {
            lblPasswordStatus.setText("Incorrect old password!");
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            lblPasswordStatus.setText("New passwords do not match!");
            return false;
        }
        if (newPassword.isEmpty()) {
            lblPasswordStatus.setText("Password cannot be empty!");
            return false;
        }

        user.setPassword(newPassword);
        UserService.updateUser(user);
        lblPasswordStatus.setText("Password changed successfully!");
        return true;
    }
}