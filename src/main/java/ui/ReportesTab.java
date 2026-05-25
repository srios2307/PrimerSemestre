package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Usuario;
import service.InmoSmartService;

import java.util.ArrayList;

/**
 * Vista de reportes generales del sistema.
 * Muestra el reporte de texto y el ranking de usuarios por puntos (ordenado con burbuja).
 */
public class ReportesTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TextArea txtReporte;
    private final TableView<Usuario> tabla;
    private final ObservableList<Usuario> datos;

    public ReportesTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Reportes del Sistema");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        Button btnGenerar = new Button("Generar Reporte");
        btnGenerar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");
        Button btnActualizarRanking = new Button("Actualizar Ranking (Burbuja)");
        btnActualizarRanking.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; "
                + "-fx-padding: 8 16;");

        HBox botones = new HBox(10, btnGenerar, btnActualizarRanking);
        botones.setAlignment(Pos.CENTER_LEFT);

        txtReporte = new TextArea();
        txtReporte.setEditable(false);
        txtReporte.setPrefRowCount(15);
        txtReporte.setWrapText(false);
        txtReporte.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        // Tabla de ranking
        tabla = new TableView<>();
        tabla.setPrefHeight(220);
        TableColumn<Usuario, String> colPos = new TableColumn<>("Posicion");
        colPos.setCellValueFactory(c -> {
            int pos = datos.indexOf(c.getValue()) + 1;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(pos));
        });
        colPos.setPrefWidth(70);
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(180);
        TableColumn<Usuario, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> {
            String tipo = c.getValue().getClass().getSimpleName();
            return new javafx.beans.property.SimpleStringProperty(tipo);
        });
        colTipo.setPrefWidth(110);
        TableColumn<Usuario, Integer> colPuntos = new TableColumn<>("Puntos");
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colPuntos.setPrefWidth(80);
        TableColumn<Usuario, String> colRango = new TableColumn<>("Rango");
        colRango.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().obtenerRango().toString()));
        colRango.setPrefWidth(180);
        TableColumn<Usuario, String> colBenef = new TableColumn<>("Beneficio");
        colBenef.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                "$" + c.getValue().calcularBeneficio()));
        colBenef.setPrefWidth(120);

        tabla.getColumns().addAll(colPos, colNombre, colTipo, colPuntos, colRango, colBenef);
        tabla.setItems(datos);

        btnGenerar.setOnAction(e -> {
            String reporte = servicio.generarReporte();
            txtReporte.setText(reporte);
        });

        btnActualizarRanking.setOnAction(e -> {
            // Ordenamos los usuarios usando metodo burbuja desde el servicio
            ArrayList<Usuario> ranking = servicio.ordenarUsuariosPorPuntos();
            datos.clear();
            datos.addAll(ranking);
            tabla.refresh();
        });

        // Al cargar la vista mostrar reporte inicial y ranking
        txtReporte.setText(servicio.generarReporte());
        datos.addAll(servicio.ordenarUsuariosPorPuntos());

        contenido.getChildren().addAll(titulo, botones, txtReporte,
                new Label("Ranking de usuarios (ordenado por puntos):"), tabla);
    }

    public Node getContent() {
        return contenido;
    }
}
