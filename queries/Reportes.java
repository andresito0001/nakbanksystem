package queries;
import java.sql.*;
import java.util.Scanner;

public class Reportes {
    public void reporteFlujo(int dia, Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println ("Ingrese la fecha de consulta: ");
            String fecha = " '"+sc.nextLine()+"'";
            System.out.println("Cuantas semanas atrás de esa fecha quieres ver? ");
            int cantidad = sc.nextInt();

            String subConsulta1 = "select sum(cantidad_recibida) from transaccion where tipo = 'compra' and moneda_recibida = 'usdt' "+
            "and extract(dow from fecha) = " + dia + " and fecha >= " + fecha + "::DATE - interval '" + (cantidad) + " weeks'";
            String subConsulta2 = "select sum(cantidad_enviada) from transaccion where tipo = 'venta' and moneda_enviada = 'usdt' "+
            "and extract(dow from fecha) = " + dia + " and fecha >= " + fecha + "::DATE - interval '" + (cantidad) + " weeks'";
            String subConsulta3 = "select sum(cantidad_recibida) from transaccion where tipo = 'compra' and (moneda_recibida = 'usd' or moneda_recibida = 'usdt') "+
            "and extract(dow from fecha) = " + dia + " and fecha >= " + fecha + "::DATE - interval '" + (cantidad) + " weeks'";
        
            String consultaFinal = "select (" + subConsulta1 + ") as monto_posicion_usdt, (" + subConsulta2 + ") as total_ventas_usdt, (" + subConsulta3 + ") as total_compras_usd";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaFinal);

            while(rs.next()) {
                System.out.println(rs.getMetaData().getColumnLabel(1)+ ": " + rs.getString(1) + " | " + 
                rs.getMetaData().getColumnLabel(2)+ ": " + rs.getString(2) + " | "+ 
                rs.getMetaData().getColumnLabel(3)+ ": " + rs.getString(3));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Eror de conexion");
            System.out.println(e.getCause());
        }
    }

    public void reporteTopCliente(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Ingrese el mes para ver el top clientes (1 es enero, 12 es diciembre)");
            String mes = sc.nextLine();

            String calcularPromedio = "select avg (cantidad_recibida/cantidad_enviada) as promedio from transaccion where moneda_recibida = 'bs' and tipo = 'venta' " + 
            " and extract (month from fecha) = " + mes + " ;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(calcularPromedio);
            double promedioTasa = 0;

            while (rs.next()) {
                promedioTasa = rs.getDouble("promedio");                
            }

            String consultaTopCliente = "select concat (nombre, ' ', apellido) as cliente, count(*) as cantidad_trx, " +
            "(sum(cantidad_recibida) * avg(cantidad_enviada/cantidad_recibida))/" + promedioTasa + " as profit " +
            " from transaccion inner join cliente on transaccion.cedula_cliente = cliente.cedula where moneda_enviada = 'bs' and tipo = 'compra' " + 
            " and extract (month from fecha) = " + mes + " group by cliente.cedula order by profit desc";

            System.out.println("Promedio de Tasa Bs en las transacciones: " + promedioTasa + "\nTOP CLIENTES: ");

            rs = st.executeQuery(consultaTopCliente);
            System.out.println(" | Cliente" + " | Cantidad Transacciones | Profit |");
            while (rs.next()) {
                System.out.println (" | " + rs.getString("cliente")+ " | "+rs.getString("cantidad_trx") + " | " + rs.getDouble("profit") + " | ");
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error de conexion. " + e.getCause());
        }
    }

    public void reporteAverage (Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            
            System.out.println("Ingrese el periodo de tiempo a ver desde la fecha actual \n 1-Un mes \t2- Una semana \t 3-Otro");
            String temp = sc.nextLine();
            String periodo = "";
            String consulta = "select avg(cantidad_enviada/cantidad_recibida) as promedio, " +
            "count(*) as n_trx from transaccion where moneda_recibida = 'usd' and moneda_enviada = 'bs' " +
            "and fecha between " ;
            switch (temp) {
                case "1":
                        periodo = "(current_date - interval '1 month') and current_date";
                    break;
                case "2":
                    periodo = "(current_date - interval '1 week') and current_date";
                    break;
                case "3":
                    System.out.println ("Desde la fecha: ");
                    String fecha1 = sc.nextLine();
                    System.out.println("Hasta la fecha: ");
                    String fecha2 = sc.nextLine();
                    periodo = " '"+fecha1+"' and '"+fecha2+"'";
                    break;
                default:
                    break;
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta+periodo);

            while (rs.next()) {
                System.out.println("|Tasa de compra promedio Bs: "+rs.getString(1) + "\t | Cantidad de Transacciones: " + rs.getString(2));            
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error de conexion. " + e.getCause());
        }
    }
    public void reporteCantidad (Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println ("Ingrese el metodo a visualizar (zelle, binance, banesco, venezuela, cash)");
            String metodo = sc.nextLine();
            System.out.println("Fecha inicio: ");
            String fecha = "'" + sc.nextLine() + "' ";
            System.out.println("Fecha fin: ");
            fecha += "and '" + sc.nextLine() + "' ";
            String consulta = "select ( select count (*) from transaccion where fecha between " +
            fecha + ") as numero_trx_periodo, (select count(*) from transaccion where " + 
            " metodo_enviado = '" + metodo + "' and fecha between " + fecha + " ) as cantidad_trx_metodo";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            System.out.println ("Periodo de Tiempo \t| #Transacciones Periodo | #Transacciones"+metodo);
            while (rs.next())
            {
                System.out.println(fecha + "|" + rs.getString(1) + "\t\t|" + rs.getString(2));
            }

            rs.close();
            st.close();
            conn.close();
        }
        catch (SQLException e) {
            System.out.println("Error de conexion "+ e.getCause());
        }
    }
    public void reporteDiferenciaTasas (Connection conn) {
        try {

            Scanner sc = new Scanner(System.in);

            System.out.println("\nFecha inicio: ");
            String fecha = "'" + sc.nextLine() + "' ";
            System.out.println("Fecha fin: ");
            fecha += "and '" + sc.nextLine() + "' ";

            String consulta = "select avg(V.cantidad_recibida/V.cantidad_enviada) as Tasa_Ventas_Promedio, " + 
            " avg(C.cantidad_enviada/C.cantidad_recibida) as Tasa_Compra_Promedio, " + 
            " ((avg(V.cantidad_recibida/V.cantidad_enviada)-avg(C.cantidad_enviada/C.cantidad_recibida))/avg(C.cantidad_enviada/C.cantidad_recibida))*100 " + 
            " as Relacion from transaccion V, transaccion C " +
            " where V.referencia <> C.referencia and " + 
            " V.tipo ='venta' and V.moneda_recibida = 'bs' and V.moneda_enviada = 'usd' " + 
            " and C.tipo = 'compra' and C.moneda_recibida = 'usd' and C.moneda_enviada = 'bs' " + 
            " and C.fecha between " + fecha;
   

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            System.out.println("Tasa Ventas Promedio \t|Tasa Compra Promedio \t|Relacion");
            while (rs.next())
            {
                System.out.println(rs.getString(1)+ "\t\t\t|" + rs.getString(2) + "\t\t\t|" + rs.getString(3));
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }
}
