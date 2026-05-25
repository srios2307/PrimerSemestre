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
import model.Inmueble;
import model.TipoInmueble;
import model.Vendedor;
import service.InmoSmartService;

/**
 * Vista para publicar inmuebles.
 * Permite seleccionar un vendedor registrado y crear un nuevo inmueble.
 */
public class PublicarInmuebleTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TableView<Inmueble> tabla;
    private final ObservableList<Inmueble> datos;
    private final ComboBox<Vendedor> cbVendedor;

    public PublicarInmuebleTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Publicar Inmueble");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-border-color: #c5d2e0; -fx-border-radius: 8;");

        Label lblVendedor = new Label("Vendedor:");
        cbVendedor = new ComboBox<>();
        cbVendedor.setPrefWidth(280);

        Label lblCodigo = new Label("Codigo:");
        TextField txtCodigo = new TextField();
        txtCodigo.setPromptText("Ej: INM-005");

        Label lblTipo = new Label("Tipo:");
        ComboBox<TipoInmueble> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoInmueble.values());
        cbTipo.setValue(TipoInmueble.CASA);
        cbTipo.setPrefWidth(280);

        Label lblDir = new Label("Direccion:");
        TextField txtDir = new TextField();
        txtDir.setPromptText("Ej: Calle 10 #20-30");

        Label lblCiudad = new Label("Ciudad:");
        TextField txtCiudad = new TextField();
        txtCiudad.setPromptText("Ej: Medellin");

        Label lblArea = new Label("Area (m2):");
        TextField txtArea = new TextField();
        txtArea.setPromptText("Ej: 120");

        Label lblPrecio = new Label("Precio:");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Ej: 250000000");

        Label lblDesc = new Label("Descripcion:");
        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Detalles del inmueble...");
        txtDesc.setPrefRowCount(3);
        txtDesc.setWrapText(true);

        Button btnPublicar = new Button("Publicar Inmueble");
        btnPublicar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #888; -fx-text-fill: white; -fx-padding: 8 16;");

        form.add(lblVendedor, 0, 0); form.add(cbVendedor, 1, 0);
        form.add(lblCodigo, 0, 1);   form.add(txtCodigo, 1, 1);
        form.add(lblTipo, 0, 2);     form.add(cbTipo, 1, 2);
        form.add(lblDir, 0, 3);      form.add(txtDir, 1, 3);
        form.add(lblCiudad, 0, 4);   form.add(txtCiudad, 1, 4);
        form.add(lblArea, 0, 5);     form.add(txtArea, 1, 5);
        form.add(lblPrecio, 0, 6);   form.add(txtPrecio, 1, 6);
        form.add(lblDesc, 0, 7);     form.add(txtDesc, 1, 7);

        HBox botones = new HBox(10, btnPublicar, btnLimpiar);
        botones.setAlignment(Pos.CENTER_LEFT);
        form.add(botones, 1, 8);

        // Tabla con los inmuebles existentes
        tabla = new TableView<>();
        tabla.setPrefHeight(220);
        TableColumn<Inmueble, String> colCodigo = new TableColumn<>("Codigo");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setPrefWidth(80);
        TableColumn<Inmueble, TipoInmueble> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(110);
        TableColumn<Inmueble, String> colDir = new TableColumn<>("Direccion");
        colDir.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colDir.setPrefWidth(180);
        TableColumn<Inmueble, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colCiudad.setPrefWidth(100);
        TableColumn<Inmueble, Double> colArea = new TableColumn<>("Area");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colArea.setPrefWidth(70);
        TableColumn<Inmueble, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(120);
        TableColumn<Inmueble, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getEstado().toString()));
        colEstado.setPrefWidth(100);
        TableColumn<Inmueble, String> colVend = new TableColumn<>("Vendedor");
        colVend.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVendedor().getNombre()));
        colVend.setPrefWidth(150);

        tabla.getColumns().addAll(colCodigo, colTipo, colDir, colCiudad,
                colArea, colPrecio, colEstado, colVend);
        tabla.setItems(datos);

        Button btnRefrescar = new Button("Refrescar Lista");
        btnRefrescar.setStyle("-fx-background-color: #2a5298; -fx-text-fill: white; -fx-padding: 6 12;");
        btnRefrescar.setOnAction(e -> {
            refrescarVendedores();
            refrescarTabla();
        });

        // Acciones
        btnPublicar.setOnAction(e -> {
            try {
                Vendedor v = cbVendedor.getValue();
                if (v == null) {
                    mostrarMensaje(Alert.AlertType.WARNING, "Atencion",
                            "Debe seleccionar un vendedor.");
                    return;
                }
                String codigo = txtCodigo.getText().trim();
                TipoInmueble tipo = cbTipo.getValue();
                String dir = txtDir.getText().trim();
                String ciudad = txtCiudad.getText().trim();
                double area = Double.parseDouble(txtArea.getText().trim());
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                String descripcion = txtDesc.getText().trim();

                servicio.crearYPublicarInmueble(codigo, tipo, dir, ciudad,
                        area, precio, v, descripcion);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Publicado",
                        "Inmueble publicado correctamente.\nEl vendedor gano 10 puntos.");
                limpiar(txtCodigo, txtDir, txtCiudad, txtArea, txtPrecio);
                txtDesc.clear();
                refrescarTabla();
            } catch (NumberFormatException ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error de formato",
                        "El area y el precio deben ser numericos.");
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al publicar", ex.getMessage());
            }
        });

        btnLimpiar.setOnAction(e -> {
            limpiar(txtCodigo, txtDir, txtCiudad, txtArea, txtPrecio);
            txtDesc.clear();
        });

        refrescarVendedores();
        refrescarTabla();

        HBox controles = new HBox(10, btnRefrescar);
        contenido.getChildren().addAll(titulo, form, controles,
                new Label("Inmuebles publicados:"), tabla);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescarVendedores() {
        cbVendedor.getItems().clear();
        cbVendedor.getItems().addAll(servicio.getVendedores());
        // Mostrar nombre legible en el ComboBox
        cbVendedor.setCellFactory(lv -> new javafx.scene.control.ListCell<Vendedor>() {
            @Override
            protected void updateItem(Vendedor v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : v.getNombre() + " (" + v.getIdentificacion() + ")");
            }
        });
        cbVendedor.setButtonCell(new javafx.scene.control.ListCell<Vendedor>() {
            @Override
            protected void updateItem(Vendedor v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : v.getNombre() + " (" + v.getIdentificacion() + ")");
            }
        });
    }

    private void refrescarTabla() {
        datos.clear();
        datos.addAll(servicio.getInmuebles());
        tabla.refresh();
    }

    private void limpiar(TextField... campos) {
        for (TextField t : campos) {
            t.clear();
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
