import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.*
import javafx.stage.Stage
import org.firebirdsql.jdbc.FBDataSource
import java.sql.Connection
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ModernCaixa : Application() {
    private lateinit var productTable: TableView<Product>
    private lateinit var quantityField: TextField
    private lateinit var totalLabel: Label
    private lateinit var dateTimeLabel: Label
    private lateinit var paymentMethodComboBox: ComboBox<String>
    private var total = 0.0

    override fun start(primaryStage: Stage) {
        // Configuração da interface gráfica
        val root = BorderPane()

        // Tabela de produtos
        productTable = TableView()
        productTable.items = getProducts()
        productTable.columns.addAll(
            createColumn("Nome", "name"),
            createColumn("Preço", "price")
        )

        // Painel de controle
        val controlPanel = VBox(10)
        controlPanel.padding = Insets(20)

        val quantityBox = HBox(10)
        quantityBox.children.addAll(Label("Quantidade:"), quantityField = TextField())

        val paymentBox = HBox(10)
        paymentBox.children.addAll(Label("Método de Pagamento:"), paymentMethodComboBox = ComboBox())
        paymentMethodComboBox.items = FXCollections.observableArrayList("Dinheiro", "Cartão de Crédito", "PIX")
        paymentMethodComboBox.selectionModel.selectFirst()

        val addButton = Button("Adicionar")
        addButton.setOnAction { addToCart() }

        totalLabel = Label("Total: R$ 0.00")
        totalLabel.styleClass.add("total-label")

        dateTimeLabel = Label(getCurrentDateTime())
        dateTimeLabel.styleClass.add("date-time-label")

        val checkoutButton = Button("Finalizar Compra")
        checkoutButton.styleClass.add("checkout-button")
        checkoutButton.setOnAction { processCheckout() }

        controlPanel.children.addAll(quantityBox, paymentBox, addButton, totalLabel, dateTimeLabel, checkoutButton)

        root.center = productTable
        root.right = controlPanel

        val scene = Scene(root, 900.0, 600.0)
        scene.stylesheets.add("styles.css")
        primaryStage.scene = scene
        primaryStage.title = "Caixa Moderno"
        primaryStage.show()
    }

    private fun createColumn(title: String, property: String): TableColumn<Product, String> {
        val column = TableColumn<Product, String>(title)
        column.cellValueFactory = PropertyValueFactory(property)
        return column
    }

    private fun addToCart() {
        val selectedProduct = productTable.selectionModel.selectedItem
        if (selectedProduct != null) {
            val quantity = quantityField.text.toInt()
            total += selectedProduct.price * quantity
            totalLabel.text = "Total: R$ %.2f".format(total)
            dateTimeLabel.text = getCurrentDateTime()
            quantityField.clear()
        }
    }

    private fun processCheckout() {
        // Lógica para processar o checkout
        val paymentMethod = paymentMethodComboBox.selectionModel.selectedItem
        println("Método de Pagamento: $paymentMethod")
        println("Valor Total: R$ $total")
        // Adicionar lógica para emitir cupom fiscal, atualizar estoque, etc.
        total = 0.0
        totalLabel.text = "Total: R$ 0.00"
    }

    private fun getProducts(): ObservableList<Product> {
        val products = FXCollections.observableArrayList<Product>()

        try {
            val dataSource = FBDataSource()
            dataSource.serverName = "localhost"
            dataSource.databaseName = "caixa.fdb"
            dataSource.user = "usuario"
            dataSource.password = "senha"

            dataSource.connection.use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM produtos")

                while (resultSet.next()) {
                    val id = resultSet.getInt("id")
                    val name = resultSet.getString("nome")
                    val price = resultSet.getDouble("preco")
                    products.add(Product(id, name, price))
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return products
    }

    private fun getCurrentDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(ModernCaixa::class.java, *args)
        }
    }

    data class Product(
        val id: Int,
        val name: String,
        val price: Double
    )
}