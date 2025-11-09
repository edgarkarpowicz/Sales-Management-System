package dto;

/**
 * Data Transfer Object (DTO) para la tabla Producto.
 * Contiene exactamente los mismos campos (columnas) que la tabla en la BD.
 */
public class ProductoDto {
    private int    idProducto;   // Clave primaria
    private String nombre;
    private int    stock;
    private double precio;
    private String marca;
    private String codigoBarras;

    /** Constructor vacío (necesario para ciertos frameworks o para crear un DTO “vacío” y luego setear). */
    public ProductoDto() {}

    /**
     * Constructor completo.
     * Úsalo cuando ya tengas todos los datos (por ejemplo, al leer de la BD).
     */
    public ProductoDto(int idProducto, String nombre, int stock, double precio, String marca, String codigoBarras) {
        this.idProducto   = idProducto;
        this.nombre       = nombre;
        this.stock        = stock;
        this.precio       = precio;
        this.marca        = marca;
        this.codigoBarras = codigoBarras;
    }

    /** Constructor parcial solo con id (útil para buscar por id). */
    public ProductoDto(int idProducto) {
        this.idProducto = idProducto;
    }

    /** Constructor parcial solo con código de barras (útil para buscar por código). */
    public ProductoDto(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    // ───────────────────────────────────────────────────────────────────────
    // Getters y setters para cada campo
    // ───────────────────────────────────────────────────────────────────────

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    // ───────────────────────────────────────────────────────────────────────
    // toString() útil para depuración
    // ───────────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "ProductoDto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                ", marca='" + marca + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                '}';
    }
}