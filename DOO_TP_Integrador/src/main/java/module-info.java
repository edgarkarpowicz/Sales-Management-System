module ubp.doo.doo_tp_integrador {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens ubp.doo.doo_tp_integrador to javafx.fxml;
    exports ubp.doo.doo_tp_integrador;
    
    opens controladores to javafx.fxml;
    exports controladores;
}
