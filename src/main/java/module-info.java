module it.saimao.tmk_typing_tutor {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;

    exports it.saimao.tmk_typing_tutor;
    opens it.saimao.tmk_typing_tutor to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.model;
    opens it.saimao.tmk_typing_tutor.model to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.utils;
    opens it.saimao.tmk_typing_tutor.utils to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.key_map;
    opens it.saimao.tmk_typing_tutor.key_map to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.auth;
    opens it.saimao.tmk_typing_tutor.auth to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.controller;
    opens it.saimao.tmk_typing_tutor.controller to javafx.fxml;
    exports it.saimao.tmk_typing_tutor.services;
    opens it.saimao.tmk_typing_tutor.services to javafx.fxml;

}