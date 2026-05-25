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
import model.TipoUsuario;
import model.Usuario;
import model.Vendedor;
import service.InmoSmartService;

/**
 * Vista de registro de usuarios (compradores y vendedores).
 * Permite agregar nuevos usuarios y muestra los registrados en una tabla.
 */
public class RegistroUsuarioTab {

    private final InmoSmartService servicio;
    private final VBox contenido;
    private final TableView<Usuario> tabla;
    private final ObservableList<Usuario> datos;

    public RegistroUsuarioTab(InmoSmartService servicio) {
        this.servicio = servicio;
        this.datos = FXCollections.observableArrayList();

        contenido = new VBox(15);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #f0f4f8;");

        Label titulo = new Label("Registro de Usuarios");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e3c72;");

        // Formulario
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-border-color: #c5d2e0; -fx-border-radius: 8;");

        Label lblTipo = new Label("Tipo:");
        ComboBox<TipoUsuario> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoUsuario.values());
        cbTipo.setValue(TipoUsuario.COMPRADOR);
        cbTipo.setPrefWidth(220);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Juan Perez");

        Label lblIdent = new Label("Identificacion:");
        TextField txtIdent = new TextField();
        txtIdent.setPromptText("Numero unico");

        Label lblTel = new Label("Telefono:");
        TextField txtTel = new TextField();
        txtTel.setPromptText("Ej: 3001234567");

        Label lblCorreo = new Label("Correo:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("correo@dominio.com");

        Button btnRegistrar = new Button("Registrar Usuario");
        btnRegistrar.setStyle("-fx-background-color: #1e3c72; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 16;");

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #888; -fx-text-fill: white; -fx-padding: 8 16;");

        form.add(lblTipo, 0, 0);    form.add(cbTipo, 1, 0);
        form.add(lblNombre, 0, 1);  form.add(txtNombre, 1, 1);
        form.add(lblIdent, 0, 2);   form.add(txtIdent, 1, 2);
        form.add(lblTel, 0, 3);     form.add(txtTel, 1, 3);
        form.add(lblCorreo, 0, 4);  form.add(txtCorreo, 1, 4);

        HBox botones = new HBox(10, btnRegistrar, btnLimpiar);
        botones.setAlignment(Pos.CENTER_LEFT);
        form.add(botones, 1, 5);

        // Tabla con los usuarios registrados
        tabla = new TableView<>();
        tabla.setPrefHeight(280);
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(180);
        TableColumn<Usuario, String> colIdent = new TableColumn<>("Identificacion");
        colIdent.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colIdent.setPrefWidth(130);
        TableColumn<Usuario, String> colTel = new TableColumn<>("Telefono");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTel.setPrefWidth(130);
        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCorreo.setPrefWidth(180);
        TableColumn<Usuario, Integer> colPuntos = new TableColumn<>("Puntos");
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colPuntos.setPrefWidth(70);
        TableColumn<Usuario, String> colRango = new TableColumn<>("Rango");
        colRango.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().obtenerRango().toString()));
        colRango.setPrefWidth(170);

        tabla.getColumns().addAll(colId, colNombre, colIdent, colTel, colCorreo, colPuntos, colRango);
        tabla.setItems(datos);

        // Acciones de botones
        btnRegistrar.setOnAction(e -> {
            try {
                TipoUsuario tipo = cbTipo.getValue();
                String nombre = txtNombre.getText().trim();
                String ident = txtIdent.getText().trim();
                String tel = txtTel.getText().trim();
                String correo = txtCorreo.getText().trim();

                if (tipo == TipoUsuario.COMPRADOR) {
                    servicio.registrarComprador(nombre, ident, tel, correo);
                } else {
                    servicio.registrarVendedor(nombre, ident, tel, correo);
                }

                mostrarMensaje(Alert.AlertType.INFORMATION, "Exito",
                        "Usuario registrado correctamente.");
                limpiarFormulario(txtNombre, txtIdent, txtTel, txtCorreo);
                refrescarTabla();
            } catch (Exception ex) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al registrar", ex.getMessage());
            }
        });

        btnLimpiar.setOnAction(e -> limpiarFormulario(txtNombre, txtIdent, txtTel, txtCorreo));

        refrescarTabla();

        contenido.getChildren().addAll(titulo, form, new Label("Usuarios Registrados:"), tabla);
    }

    public Node getContent() {
        return contenido;
    }

    private void refrescarTabla() {
        datos.clear();
        datos.addAll(servicio.getCompradores());
        datos.addAll(servicio.getVendedores());
        tabla.refresh();
    }

    private void limpiarFormulario(TextField... campos) {
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
