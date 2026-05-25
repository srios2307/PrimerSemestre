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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Oferta;
import model.TipoOperacion;
import model.Transaccion;
import service.InmoSmartService;

/**
 * Vista para cerrar transacciones (venta o arriendo) a partir de ofertas aceptadas.
 */
public class TransaccionesTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TableView<Transaccion> tabla;
    private final ObservableList<Transaccion> datos;
    private final ComboBox<Oferta> cbOferta;
    private final ComboBox<TipoOperacion> cbTipo;

    public TransaccionesTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Transacciones (Compra / Arriendo)");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-border-color: #c5d2e0; -fx-border-radius: 8;");

        Label lblOferta = new Label("Oferta aceptada:");
        cbOferta = new ComboBox<>();
        cbOferta.setPrefWidth(400);

        Label lblTipo = new Label("Tipo operacion:");
        cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoOperacion.values());
        cbTipo.setValue(TipoOperacion.VENTA);

        Button btnConcretar = new Button("Concretar Operacion");
        btnConcretar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");

        Button btnRefrescar = new Button("Refrescar");
        btnRefrescar.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; -fx-padding: 8 16;");

        form.add(lblOferta, 0, 0); form.add(cbOferta, 1, 0);
        form.add(lblTipo, 0, 1);   form.add(cbTipo, 1, 1);

        HBox botones = new HBox(10, btnConcretar, btnRefrescar);
        botones.setAlignment(Pos.CENTER_LEFT);
        form.add(botones, 1, 2);

        // Tabla con todas las transacciones realizadas
        tabla = new TableView<>();
        tabla.setPrefHeight(280);
        TableColumn<Transaccion, String> colCodigo = new TableColumn<>("Codigo");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoTransaccion"));
        colCodigo.setPrefWidth(80);
        TableColumn<Transaccion, String> colComp = new TableColumn<>("Comprador");
        colComp.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getComprador().getNombre()));
        colComp.setPrefWidth(150);
        TableColumn<Transaccion, String> colVend = new TableColumn<>("Vendedor");
        colVend.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVendedor().getNombre()));
        colVend.setPrefWidth(150);
        TableColumn<Transaccion, String> colInm = new TableColumn<>("Inmueble");
        colInm.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getInmueble().getCodigo()));
        colInm.setPrefWidth(100);
        TableColumn<Transaccion, Double> colValor = new TableColumn<>("Valor Final");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        colValor.setPrefWidth(130);
        TableColumn<Transaccion, String> colTipoOp = new TableColumn<>("Tipo");
        colTipoOp.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getTipoOperacion().toString()));
        colTipoOp.setPrefWidth(100);
        TableColumn<Transaccion, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getFecha().toString()));
        colFecha.setPrefWidth(110);

        tabla.getColumns().addAll(colCodigo, colComp, colVend, colInm,
                colValor, colTipoOp, colFecha);
        tabla.setItems(datos);

        btnConcretar.setOnAction(e -> {
            Oferta o = cbOferta.getValue();
            TipoOperacion t = cbTipo.getValue();
            if (o == null) {
                mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                        "Debe seleccionar una oferta aceptada.");
                return;
            }
            try {
                Transaccion tx = servicio.comprarInmueble(o, t);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Transaccion realizada",
                        "Operacion " + t + " concretada con exito.\n"
                                + "Codigo: " + tx.getCodigoTransaccion()
                                + "\nValor: $" + tx.getValorFinal()
                                + "\nComprador +150 puntos | Vendedor +100 puntos");
                refrescarCombo();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        btnRefrescar.setOnAction(e -> {
            refrescarCombo();
            refrescarTabla();
        });

        refrescarCombo();
        refrescarTabla();

        contenido.getChildren().addAll(titulo, form,
                new Label("Transacciones completadas:"), tabla);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescarCombo() {
        cbOferta.getItems().clear();
        cbOferta.getItems().addAll(servicio.getOfertasAceptadas());
        cbOferta.setCellFactory(lv -> new javafx.scene.control.ListCell<Oferta>() {
            @Override
            protected void updateItem(Oferta o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null
                        : o.getCodigoOferta() + " | " + o.getComprador().getNombre()
                                + " | " + o.getInmueble().getCodigo()
                                + " | $" + o.getValorOferta());
            }
        });
        cbOferta.setButtonCell(new javafx.scene.control.ListCell<Oferta>() {
            @Override
            protected void updateItem(Oferta o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null
                        : o.getCodigoOferta() + " | " + o.getComprador().getNombre()
                                + " | " + o.getInmueble().getCodigo()
                                + " | $" + o.getValorOferta());
            }
        });
    }

    private void refrescarTabla() {
        datos.clear();
        datos.addAll(servicio.getTransacciones());
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
