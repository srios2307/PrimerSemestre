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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comprador;
import model.Inmueble;
import model.Oferta;
import service.InmoSmartService;

/**
 * Vista para realizar ofertas y para que el vendedor las acepte o rechace.
 */
public class OfertasTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TableView<Oferta> tabla;
    private final ObservableList<Oferta> datos;
    private final ComboBox<Comprador> cbComprador;
    private final ComboBox<Inmueble> cbInmueble;

    public OfertasTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Gestion de Ofertas");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        // Formulario de nueva oferta
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-border-color: #c5d2e0; -fx-border-radius: 8;");

        Label lblComprador = new Label("Comprador:");
        cbComprador = new ComboBox<>();
        cbComprador.setPrefWidth(280);

        Label lblInmueble = new Label("Inmueble:");
        cbInmueble = new ComboBox<>();
        cbInmueble.setPrefWidth(380);

        Label lblValor = new Label("Valor oferta:");
        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: 200000000");

        Button btnOfertar = new Button("Realizar Oferta");
        btnOfertar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");

        Button btnRefrescar = new Button("Refrescar Datos");
        btnRefrescar.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; -fx-padding: 8 16;");

        form.add(lblComprador, 0, 0); form.add(cbComprador, 1, 0);
        form.add(lblInmueble, 0, 1);  form.add(cbInmueble, 1, 1);
        form.add(lblValor, 0, 2);     form.add(txtValor, 1, 2);

        HBox botones = new HBox(10, btnOfertar, btnRefrescar);
        botones.setAlignment(Pos.CENTER_LEFT);
        form.add(botones, 1, 3);

        // Tabla con las ofertas
        tabla = new TableView<>();
        tabla.setPrefHeight(260);
        TableColumn<Oferta, String> colCodigo = new TableColumn<>("Codigo");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoOferta"));
        colCodigo.setPrefWidth(80);
        TableColumn<Oferta, String> colComp = new TableColumn<>("Comprador");
        colComp.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getComprador().getNombre()));
        colComp.setPrefWidth(150);
        TableColumn<Oferta, String> colInm = new TableColumn<>("Inmueble");
        colInm.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getInmueble().getCodigo()));
        colInm.setPrefWidth(100);
        TableColumn<Oferta, Double> colVal = new TableColumn<>("Valor");
        colVal.setCellValueFactory(new PropertyValueFactory<>("valorOferta"));
        colVal.setPrefWidth(130);
        TableColumn<Oferta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getFechaOferta().toString()));
        colFecha.setPrefWidth(110);
        TableColumn<Oferta, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getEstado().toString()));
        colEstado.setPrefWidth(110);

        tabla.getColumns().addAll(colCodigo, colComp, colInm, colVal, colFecha, colEstado);
        tabla.setItems(datos);

        Button btnAceptar = new Button("Aceptar Oferta Seleccionada");
        btnAceptar.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");
        Button btnRechazar = new Button("Rechazar Oferta Seleccionada");
        btnRechazar.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");

        HBox accionesTabla = new HBox(10, btnAceptar, btnRechazar);
        accionesTabla.setAlignment(Pos.CENTER_LEFT);

        // Eventos
        btnOfertar.setOnAction(e -> {
            try {
                Comprador c = cbComprador.getValue();
                Inmueble i = cbInmueble.getValue();
                if (c == null || i == null) {
                    mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                            "Debe seleccionar comprador e inmueble.");
                    return;
                }
                double valor = Double.parseDouble(txtValor.getText().trim());
                servicio.realizarOferta(c, i, valor);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Oferta creada",
                        "Oferta registrada. El comprador gano 5 puntos.");
                txtValor.clear();
                refrescarTabla();
            } catch (NumberFormatException ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error", "El valor debe ser numerico.");
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al ofertar", ex.getMessage());
            }
        });

        btnAceptar.setOnAction(e -> {
            Oferta seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                        "Seleccione una oferta de la tabla.");
                return;
            }
            try {
                servicio.aceptarOferta(seleccionada);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Aceptada",
                        "Oferta aceptada. El inmueble queda RESERVADO.\n"
                                + "Vaya a la pestaña Transacciones para concretar.");
                refrescarTabla();
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        btnRechazar.setOnAction(e -> {
            Oferta seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                        "Seleccione una oferta de la tabla.");
                return;
            }
            try {
                servicio.rechazarOferta(seleccionada);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Rechazada",
                        "Oferta rechazada correctamente.");
                refrescarTabla();
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        btnRefrescar.setOnAction(e -> {
            refrescarCombos();
            refrescarTabla();
        });

        refrescarCombos();
        refrescarTabla();

        contenido.getChildren().addAll(titulo, form,
                new Label("Ofertas registradas:"), tabla, accionesTabla);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescarCombos() {
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

        cbInmueble.getItems().clear();
        cbInmueble.getItems().addAll(servicio.getInmueblesDisponibles());
        cbInmueble.setCellFactory(lv -> new javafx.scene.control.ListCell<Inmueble>() {
            @Override
            protected void updateItem(Inmueble i, boolean empty) {
                super.updateItem(i, empty);
                setText(empty || i == null ? null
                        : i.getCodigo() + " - " + i.getTipo() + " - " + i.getCiudad()
                                + " - $" + i.getPrecio());
            }
        });
        cbInmueble.setButtonCell(new javafx.scene.control.ListCell<Inmueble>() {
            @Override
            protected void updateItem(Inmueble i, boolean empty) {
                super.updateItem(i, empty);
                setText(empty || i == null ? null
                        : i.getCodigo() + " - " + i.getTipo() + " - " + i.getCiudad()
                                + " - $" + i.getPrecio());
            }
        });
    }

    private void refrescarTabla() {
        datos.clear();
        datos.addAll(servicio.getOfertas());
        tabla.refresh();
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
