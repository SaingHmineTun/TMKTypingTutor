module it.saimao.hminepos {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    exports it.saimao.tmk_typing;
    opens it.saimao.tmk_typing to javafx.fxml;
    exports it.saimao.tmk_typing.model;
    opens it.saimao.tmk_typing.model to javafx.fxml;
    exports it.saimao.tmk_typing.utils;
    opens it.saimao.tmk_typing.utils to javafx.fxml;
    exports it.saimao.tmk_typing.node;
    opens it.saimao.tmk_typing.node to javafx.fxml;

}