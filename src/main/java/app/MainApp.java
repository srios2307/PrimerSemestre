package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.InmoSmartService;
import ui.AlertasTab;
import ui.BuscarInmuebleTab;
import ui.OfertasTab;
import ui.PublicarInmuebleTab;
import ui.RegistroUsuarioTab;
import ui.ReportesTab;
import ui.TransaccionesTab;

/**
 * Clase principal de la aplicacion JavaFX.
 * Monta el menu principal con TabPane y todas las secciones del sistema.
 */
public class MainApp extends Application {

    private InmoSmartService servicio;

    @Override
    public void start(Stage primaryStage) {
        // El servicio se comparte entre todas las pestañas
        servicio = new InmoSmartService();

        // Cargar datos de ejemplo para que la app no inicie vacia
        cargarDatosDeEjemplo();

        // BorderPane principal
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f4f8;");

        // Encabezado con titulo
        VBox header = new VBox();
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: linear-gradient(to right, #1e3c72, #2a5298);");

        Label titulo = new Label("InmoSmart");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");
        Label subtitulo = new Label("Plataforma Inmobiliaria Digital");
        subtitulo.setStyle("-fx-text-fill: #d6e4f0; -fx-font-size: 14px;");
        header.getChildren().addAll(titulo, subtitulo);

        // TabPane con las secciones
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabRegistro = new Tab("Registro");
        tabRegistro.setContent(new RegistroUsuarioTab(servicio).getContent());

        Tab tabPublicar = new Tab("Publicar Inmueble");
        tabPublicar.setContent(new PublicarInmuebleTab(servicio).getContent());

        Tab tabBuscar = new Tab("Buscar Inmuebles");
        tabBuscar.setContent(new BuscarInmuebleTab(servicio).getContent());

        Tab tabOfertas = new Tab("Ofertas");
        tabOfertas.setContent(new OfertasTab(servicio).getContent());

        Tab tabTransacciones = new Tab("Transacciones");
        tabTransacciones.setContent(new TransaccionesTab(servicio).getContent());

        Tab tabReportes = new Tab("Reportes");
        tabReportes.setContent(new ReportesTab(servicio).getContent());

        Tab tabAlertas = new Tab("Alertas");
        tabAlertas.setContent(new AlertasTab(servicio).getContent());

        tabPane.getTabs().addAll(tabRegistro, tabPublicar, tabBuscar,
                tabOfertas, tabTransacciones, tabReportes, tabAlertas);

        // Pie de pagina
        Label pie = new Label("Proyecto Final - Programacion I - InmoSmart (c) 2026");
        pie.setStyle("-fx-text-fill: #555; -fx-font-size: 11px;");
        VBox footer = new VBox(pie);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #e1e8ed;");

        root.setTop(header);
        root.setCenter(tabPane);
        root.setBottom(footer);

        Scene scene = new Scene(root, 1100, 720);
        primaryStage.setTitle("InmoSmart - Plataforma Inmobiliaria");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Carga algunos datos iniciales para que la aplicacion no inicie vacia
    private void cargarDatosDeEjemplo() {
        try {
            // Vendedores
            servicio.registrarVendedor("Ana Lopez", "V001", "3110001111", "ana@inmosmart.co");
            servicio.registrarVendedor("Carlos Ruiz", "V002", "3110002222", "carlos@inmosmart.co");

            // Compradores
            servicio.registrarComprador("Juan Perez", "C001", "3120001111", "juan@gmail.com");
            servicio.registrarComprador("Maria Gomez", "C002", "3120002222", "maria@gmail.com");

            // Inmuebles publicados
            servicio.crearYPublicarInmueble("INM-001", model.TipoInmueble.CASA, "Calle 10 #20-30",
                    "Medellin", 120, 250000000, servicio.getVendedores().get(0),
                    "Casa de 2 pisos con jardin");
            servicio.crearYPublicarInmueble("INM-002", model.TipoInmueble.APARTAMENTO,
                    "Carrera 50 #45-22", "Bogota", 65, 180000000,
                    servicio.getVendedores().get(0), "Apartamento moderno cerca al metro");
            servicio.crearYPublicarInmueble("INM-003", model.TipoInmueble.LOCAL,
                    "Av Siempre Viva 742", "Cali", 80, 320000000,
                    servicio.getVendedores().get(1), "Local comercial en zona rosa");
            servicio.crearYPublicarInmueble("INM-004", model.TipoInmueble.TERRENO,
                    "Vereda La Esperanza", "Pereira", 500, 95000000,
                    servicio.getVendedores().get(1), "Terreno plano apto para construir");
        } catch (Exception ex) {
            System.out.println("Error cargando datos de ejemplo: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
