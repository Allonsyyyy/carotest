module com.mycompany.carogame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.mycompany.carogame to javafx.fxml;
    exports com.mycompany.carogame.Login;
    exports com.mycompany.carogame.javaai;
    exports com.mycompany.carogame.javaai.Difficulty;
}
