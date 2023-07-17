module it.saimao.hminepos {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    exports it.saimao.tmk_typing_tutor;
    opens it.saimao.tmk_typing_tutor to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.model;
    opens it.saimao.tmk_typing_tutor.model to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.utils;
    opens it.saimao.tmk_typing_tutor.utils to javafx.fxml;

}