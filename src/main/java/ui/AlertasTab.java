package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.InmoSmartService;

/**
 * Vista que muestra las alertas automaticas generadas por el sistema.
 * Cada accion importante (registro, publicacion, oferta, transaccion)
 * genera una alerta que se guarda y se puede consultar aqui.
 */
public class AlertasTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final ListView<String> listaAlertas;
    private final ObservableList<String> datos;

    public AlertasTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Alertas Automaticas");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");
        Label descripcion = new Label("Estas alertas se generan automaticamente cuando "
                + "se registran usuarios, se publican inmuebles, se hacen ofertas o se realizan transacciones.");
        descripcion.setWrapText(true);
        descripcion.setStyle("-fx-text-fill: #555;");

        listaAlertas = new ListView<>(datos);
        listaAlertas.setPrefHeight(420);
        listaAlertas.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        Button btnRefrescar = new Button("Refrescar Alertas");
        btnRefrescar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");
        Button btnLimpiar = new Button("Limpiar Alertas");
        btnLimpiar.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; -fx-padding: 8 16;");

        btnRefrescar.setOnAction(e -> refrescar());
        btnLimpiar.setOnAction(e -> {
            servicio.limpiarAlertas();
            refrescar();
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Alertas");
            info.setHeaderText(null);
            info.setContentText("Alertas limpiadas correctamente.");
            info.showAndWait();
        });

        HBox botones = new HBox(10, btnRefrescar, btnLimpiar);
        botones.setAlignment(Pos.CENTER_LEFT);

        refrescar();

        contenido.getChildren().addAll(titulo, descripcion, botones, listaAlertas);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescar() {
        datos.clear();
        datos.addAll(servicio.getAlertas());
    }
}
