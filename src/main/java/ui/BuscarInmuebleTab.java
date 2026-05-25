package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comprador;
import model.Inmueble;
import model.TipoInmueble;
import service.InmoSmartService;

import java.util.ArrayList;

/**
 * Vista para buscar inmuebles con filtros y ver recomendaciones.
 * Tambien permite ordenar por precio usando el metodo burbuja.
 */
public class BuscarInmuebleTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TableView<Inmueble> tabla;
    private final ObservableList<Inmueble> datos;
    private final ComboBox<Comprador> cbComprador;
    private final TextArea txtRecomendaciones;

    public BuscarInmuebleTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Buscar Inmuebles");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        // Panel de filtros
        GridPane filtros = new GridPane();
        filtros.setHgap(10);
        filtros.setVgap(10);
        filtros.setPadding(new Insets(15));
        filtros.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-border-color: #c5d2e0; -fx-border-radius: 8;");

        Label lblCiudad = new Label("Ciudad:");
        TextField txtCiudad = new TextField();
        txtCiudad.setPromptText("(opcional)");

        Label lblTipo = new Label("Tipo:");
        ComboBox<TipoInmueble> cbTipo = new ComboBox<>();
        cbTipo.getItems().add(null); // Permitir opcion vacia
        cbTipo.getItems().addAll(TipoInmueble.values());

        Label lblPrecio = new Label("Precio max:");
        TextField txtPrecioMax = new TextField();
        txtPrecioMax.setPromptText("0 = sin limite");

        Label lblArea = new Label("Area minima:");
        TextField txtAreaMin = new TextField();
        txtAreaMin.setPromptText("0 = sin limite");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");
        Button btnOrdenar = new Button("Ordenar por precio (Burbuja)");
        btnOrdenar.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; -fx-padding: 8 16;");
        Button btnLimpiar = new Button("Limpiar Filtros");
        btnLimpiar.setStyle("-fx-background-color: #888; -fx-text-fill: white; -fx-padding: 8 16;");

        filtros.add(lblCiudad, 0, 0); filtros.add(txtCiudad, 1, 0);
        filtros.add(lblTipo, 2, 0);   filtros.add(cbTipo, 3, 0);
        filtros.add(lblPrecio, 0, 1); filtros.add(txtPrecioMax, 1, 1);
        filtros.add(lblArea, 2, 1);   filtros.add(txtAreaMin, 3, 1);

        HBox botonesFiltro = new HBox(10, btnBuscar, btnOrdenar, btnLimpiar);
        botonesFiltro.setAlignment(Pos.CENTER_LEFT);
        filtros.add(botonesFiltro, 0, 2, 4, 1);

        // Tabla con resultados
        tabla = new TableView<>();
        tabla.setPrefHeight(220);
        TableColumn<Inmueble, String> colCodigo = new TableColumn<>("Codigo");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Inmueble, TipoInmueble> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Inmueble, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        TableColumn<Inmueble, Double> colArea = new TableColumn<>("Area");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        TableColumn<Inmueble, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        TableColumn<Inmueble, String> colDir = new TableColumn<>("Direccion");
        colDir.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        colCodigo.setPrefWidth(80);
        colTipo.setPrefWidth(110);
        colCiudad.setPrefWidth(110);
        colArea.setPrefWidth(80);
        colPrecio.setPrefWidth(130);
        colDir.setPrefWidth(200);

        tabla.getColumns().addAll(colCodigo, colTipo, colCiudad,
                colArea, colPrecio, colDir);
        tabla.setItems(datos);

        // Seccion de recomendaciones
        Label lblRecLbl = new Label("Recomendaciones para comprador:");
        cbComprador = new ComboBox<>();
        cbComprador.setPrefWidth(280);
        Button btnRecomendar = new Button("Generar Recomendaciones");
        btnRecomendar.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; -fx-padding: 6 12;");

        txtRecomendaciones = new TextArea();
        txtRecomendaciones.setEditable(false);
        txtRecomendaciones.setPrefRowCount(6);
        txtRecomendaciones.setWrapText(true);
        txtRecomendaciones.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        HBox recHBox = new HBox(10, lblRecLbl, cbComprador, btnRecomendar);
        recHBox.setAlignment(Pos.CENTER_LEFT);

        // Acciones
        btnBuscar.setOnAction(e -> {
            try {
                String ciudad = txtCiudad.getText().trim();
                TipoInmueble tipo = cbTipo.getValue();
                double pMax = txtPrecioMax.getText().trim().isEmpty()
                        ? 0 : Double.parseDouble(txtPrecioMax.getText().trim());
                double aMin = txtAreaMin.getText().trim().isEmpty()
                        ? 0 : Double.parseDouble(txtAreaMin.getText().trim());
                ArrayList<Inmueble> resultado = servicio.buscarConFiltros(ciudad, tipo, pMax, aMin);
                datos.clear();
                datos.addAll(resultado);
                if (resultado.isEmpty()) {
                    mostrarMensaje(Alert.AlertType.INFORMATION, "Sin resultados",
                            "No se encontraron inmuebles con esos filtros.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error",
                        "Precio y area deben ser numericos.");
            }
        });

        btnOrdenar.setOnAction(e -> {
            // Ordenar los resultados actuales con metodo burbuja
            ArrayList<Inmueble> actual = new ArrayList<>(datos);
            ArrayList<Inmueble> ordenado = servicio.ordenarInmueblesPorPrecio(actual);
            datos.clear();
            datos.addAll(ordenado);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Ordenado",
                    "Inmuebles ordenados por precio (menor a mayor) usando metodo BURBUJA.");
        });

        btnLimpiar.setOnAction(e -> {
            txtCiudad.clear();
            txtPrecioMax.clear();
            txtAreaMin.clear();
            cbTipo.setValue(null);
            datos.clear();
            datos.addAll(servicio.getInmueblesDisponibles());
        });

        btnRecomendar.setOnAction(e -> {
            Comprador c = cbComprador.getValue();
            if (c == null) {
                mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                        "Debe seleccionar un comprador.");
                return;
            }
            ArrayList<Inmueble> recomendados = servicio.recomendarInmuebles(c);
            StringBuilder sb = new StringBuilder();
            sb.append("Recomendaciones para ").append(c.getNombre()).append(":\n\n");
            if (recomendados.isEmpty()) {
                sb.append("No hay inmuebles disponibles para recomendar.");
            } else {
                int n = 1;
                for (Inmueble i : recomendados) {
                    sb.append(n).append(". ").append(i.toString()).append("\n");
                    n++;
                }
            }
            txtRecomendaciones.setText(sb.toString());
        });

        refrescarCompradores();
        datos.addAll(servicio.getInmueblesDisponibles());

        contenido.getChildren().addAll(titulo, filtros,
                new Label("Resultados:"), tabla,
                recHBox, txtRecomendaciones);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescarCompradores() {
        cbComprador.getItems().clear();
        cbComprador.getItems().addAll(servicio.getCompradores());
        cbComprador.setCellFactory(lv -> new javafx.scene.control.ListCell<Comprador>() {
            @Override
            protected void updateItem(Comprador c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre() + " (" + c.getIdentificacion() + ")");
            }
        });
        cbComprador.setButtonCell(new javafx.scene.control.ListCell<Comprador>() {
            @Override
            protected void updateItem(Comprador c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre() + " (" + c.getIdentificacion() + ")");
            }
        });
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
