import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ModernPDV extends Application {
    private TableView<Product> productTable;
    private TextField quantityField;
    private Label totalLabel;
    private double total = 0.0;
    private ObservableList<Product> products;

    @Override
    public void start(Stage primaryStage) {
        // Configuração da interface gráfica
        BorderPane root = new BorderPane();

        // Painel de produtos
        VBox productPanel = new VBox(10);
        productPanel.setPadding(new Insets(20));
        productPanel.setAlignment(Pos.CENTER);

        // Tabela de produtos
        productTable = new TableView<>();
        products = getProducts();
        productTable.setItems(products);
        productTable.getColumns().addAll(
                createColumn("Nome", "name"),
                createColumn("Preço", "price")
        );

        productPanel.getChildren().add(productTable);

        // Painel de controle
        VBox controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(20));

        HBox quantityBox = new HBox(10);
        quantityBox.getChildren().addAll(new Label("Quantidade:"), quantityField = new TextField());

        Button addButton = new Button("Adicionar");
        addButton.setOnAction(e -> addToCart());

        Button removeButton = new Button("Remover");
        removeButton.setOnAction(e -> removeFromCart());

        totalLabel = new Label("Total: R$ 0.00");

        controlPanel.getChildren().addAll(quantityBox, addButton, removeButton, totalLabel);

        root.setLeft(productPanel);
        root.setRight(controlPanel);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Caixa Moderno");
        primaryStage.show();
    }

    private TableColumn<Product, String> createColumn(String title, String property) {
        TableColumn<Product, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private void addToCart() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int quantity = Integer.parseInt(quantityField.getText());
            total += selectedProduct.getPrice() * quantity;
            totalLabel.setText(String.format("Total: R$ %.2f", total));
            quantityField.clear();
        }
    }

    private void removeFromCart() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int quantity = Integer.parseInt(quantityField.getText());
            total -= selectedProduct.getPrice() * quantity;
            totalLabel.setText(String.format("Total: R$ %.2f", total));
            quantityField.clear();
        }
    }

    private ObservableList<Product> getProducts() {
        return FXCollections.observableArrayList(
                new Product("Produto 1", 9.99),
                new Product("Produto 2", 19.99),
                new Product("Produto 3", 29.99),
                new Product("Produto 4", 39.99),
                new Product("Produto 5", 49.99),
                new Product("Produto 6", 59.99)
        );
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Product {
        private String name;
        private double price;

        Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}