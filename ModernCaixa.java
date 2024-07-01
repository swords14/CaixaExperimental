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

public class CaixaModerno extends Application {
    private TableView<Produto> tabelaProdutos;
    private TextField campoQuantidade;
    private Label labelTotal;
    private double total = 0.0;
    private ObservableList<Produto> produtos;

    @Override
    public void start(Stage primaryStage) {
        // Configuração da interface gráfica
        BorderPane raiz = new BorderPane();

        // Painel de produtos
        VBox painelProdutos = new VBox(10);
        painelProdutos.setPadding(new Insets(20));
        painelProdutos.setAlignment(Pos.CENTER);

        // Tabela de produtos
        tabelaProdutos = new TableView<>();
        produtos = getProdutos();
        tabelaProdutos.setItems(produtos);
        tabelaProdutos.getColumns().addAll(
                criarColuna("Nome", "nome"),
                criarColuna("Preço", "preco")
        );

        painelProdutos.getChildren().add(tabelaProdutos);

        // Painel de controle
        VBox painelControle = new VBox(10);
        painelControle.setPadding(new Insets(20));

        HBox campoQuantidadeBox = new HBox(10);
        campoQuantidadeBox.getChildren().addAll(new Label("Quantidade:"), campoQuantidade = new TextField());

        Button botaoAdicionar = new Button("Adicionar");
        botaoAdicionar.setOnAction(e -> adicionarAoCarrinho());

        Button botaoRemover = new Button("Remover");
        botaoRemover.setOnAction(e -> removerDoCarrinho());

        labelTotal = new Label("Total: R$ 0.00");

        painelControle.getChildren().addAll(campoQuantidadeBox, botaoAdicionar, botaoRemover, labelTotal);

        raiz.setLeft(painelProdutos);
        raiz.setRight(painelControle);

        Scene cena = new Scene(raiz, 900, 600);
        cena.getStylesheets().add("estilos.css");
        primaryStage.setScene(cena);
        primaryStage.setTitle("Caixa Moderno");
        primaryStage.show();
    }

    private TableColumn<Produto, String> criarColuna(String titulo, String propriedade) {
        TableColumn<Produto, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        return coluna;
    }

    private void adicionarAoCarrinho() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            int quantidade = Integer.parseInt(campoQuantidade.getText());
            total += produtoSelecionado.getPreco() * quantidade;
            labelTotal.setText(String.format("Total: R$ %.2f", total));
            campoQuantidade.clear();
        }
    }

    private void removerDoCarrinho() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            int quantidade = Integer.parseInt(campoQuantidade.getText());
            total -= produtoSelecionado.getPreco() * quantidade;
            labelTotal.setText(String.format("Total: R$ %.2f", total));
            campoQuantidade.clear();
        }
    }

    private ObservableList<Produto> getProdutos() {
        return FXCollections.observableArrayList(
                new Produto("Produto 1", 9.99),
                new Produto("Produto 2", 19.99),
                new Produto("Produto 3", 29.99),
                new Produto("Produto 4", 39.99),
                new Produto("Produto 5", 49.99),
                new Produto("Produto 6", 59.99)
        );
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Produto {
        private String nome;
        private double preco;

        Produto(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }

        public String getNome() {
            return nome;
        }

        public double getPreco() {
            return preco;
        }
    }
}