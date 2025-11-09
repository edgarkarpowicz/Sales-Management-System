package dto;

/**
 * Data Transfer Object (DTO) para la tabla Venta.
 * Campos en la base de datos (SQLite):
 *   - idVenta      (INTEGER PRIMARY KEY AUTOINCREMENT)
 *   - idCliente    (INTEGER NOT NULL)
 *   - idCajero     (INTEGER NOT NULL)
 *   - idDescuento  (INTEGER)           // puede ser NULL
 *   - idFactura    (INTEGER NOT NULL)
 *   - noTarjeta    (TEXT)              // opcional, puede ser NULL
 *   - tipo         (TEXT NOT NULL)
 *   - estado       (TEXT NOT NULL)
 */
public class VentaDto {
    private int     idVenta;      // 0 si todavía no se ha insertado (autoincrement)
    private int     idCliente;
    private int     idCajero;
    private Integer idDescuento;  // opcional (puede ser null)
    private int     idFactura;
    private String  noTarjeta;    // opcional (puede ser null)
    private String  tipo;
    private String  estado;

    /** Constructor vacío (necesario para frameworks o para mapear resultados). */
    public VentaDto() {
        this.idVenta = 0;
    }

    /** Constructor para crear una nueva venta (sin idVenta). */
    public VentaDto(int idCliente, int idCajero, Integer idDescuento,
                    int idFactura, String noTarjeta, String tipo, String estado) {
        this.idVenta     = 0;
        this.idCliente   = idCliente;
        this.idCajero    = idCajero;
        this.idDescuento = idDescuento;
        this.idFactura   = idFactura;
        this.noTarjeta   = noTarjeta;
        this.tipo        = tipo;
        this.estado      = estado;
    }

    /** Constructor completo (útil para mapear filas existentes con idVenta). */
    public VentaDto(int idVenta, int idCliente, int idCajero, Integer idDescuento,
                    int idFactura, String noTarjeta, String tipo, String estado) {
        this.idVenta     = idVenta;
        this.idCliente   = idCliente;
        this.idCajero    = idCajero;
        this.idDescuento = idDescuento;
        this.idFactura   = idFactura;
        this.noTarjeta   = noTarjeta;
        this.tipo        = tipo;
        this.estado      = estado;
    }

    /** Getters y setters **/

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public Integer getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(Integer idDescuento) {
        this.idDescuento = idDescuento;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getNoTarjeta() {
        return noTarjeta;
    }

    public void setNoTarjeta(String noTarjeta) {
        this.noTarjeta = noTarjeta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "VentaDto{" +
               "idVenta=" + idVenta +
               ", idCliente=" + idCliente +
               ", idCajero=" + idCajero +
               ", idDescuento=" + idDescuento +
               ", idFactura=" + idFactura +
               ", noTarjeta='" + noTarjeta + '\'' +
               ", tipo='" + tipo + '\'' +
               ", estado='" + estado + '\'' +
               '}';
    }
}
