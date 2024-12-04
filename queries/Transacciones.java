package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import utility.TimeZone;
import utility.TransTypes;
import utility.CheckTypes.*;
import utility.bancos.PlataformasOnline;
import utility.monedas.MoneyType;

public class Transacciones {
    /***
     * Muestra el historial de transacciones de un cliente dado su numero de cedula.
     * 
     * Tome en cuenta que este es el historico total de transacciones de este cliente
     * a lo largo del tiempo.
     * @param conn
     * @param cedula
     * @return void
     */
    public static void fillByClient(Connection conn, final String cedula, Date beginDate, Date endDate) throws SQLException {
        final String query = "select * from transaccion where cedula_cliente = ? and fecha between ? and ?";
        try (PreparedStatement st = conn.prepareStatement(query);) {
            st.setString(1, cedula);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);
            
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("Fecha: ").append(String.valueOf(rs.getDate("fecha")))
                .append(", tipo: ").append(rs.getString("tipo"))
                .append(", cantidad_recibida: ").append(rs.getString("cantidad_recibida"))
                .append(", moneda_recibida: ").append(rs.getString("moneda_recibida"))
                .append(", metodo_recibido: ").append(rs.getString("metodo_recibido"))
                .append(", cantidad_enviada: ").append(rs.getString("cantidad_enviada"))
                .append(", moneda_enviada: " ).append(rs.getString("moneda_enviada"))
                .append(", metodo_enviado: ").append(rs.getString("metodo_enviado"))
                .append(", status: ").append(rs.getString("status")).append("\n");
            }
            System.out.println(metaData);
            rs.close();
            st.close();
        }
    }

    public static void fillByDate(final Connection conn, final Date beginDate, final Date endDate) throws SQLException {
        String query = "select c.nombre, c.apellido, c.cedula, t.fecha, t.tipo, t.cantidad_recibida, t.moneda_recibida, " +
        "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status " +
        "from transaccion t " +
        "inner join cliente c on t.cedula_cliente = c.cedula " +
        "where t.fecha between ? and ?";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();
            
            while (rs.next()) {
                metaData.append("Nombre: ").append(rs.getString("nombre"))
                .append(", Apellido: ").append(rs.getString("apellido"))
                .append(", Cedula: ").append(rs.getString("cedula"))
                .append(", Fecha: ").append(String.valueOf(rs.getDate("fecha"))).append("\n");
            }

            System.out.println(metaData);
            rs.close();
            st.close();
        }
    }

    public static Integer getNumOftTransByTypeAndDate(final Connection conn, final String type, final Date beginDate, 
    final Date endDate) throws SQLException {
        final String query = "select tipo, count(*) as total_transacciones " +
                        "from transaccion where tipo = ? and fecha between ? and ? group by tipo;";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, type);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);

            final ResultSet rs = st.executeQuery();
            rs.next();

            Integer count = rs.getInt("total_transacciones");
            
            rs.close();
            st.close();
            return count > 0 ? count : 0;
        }
    }

    public static Float getAllAmountReceivedBy(Connection conn, final String transType,
                                                final String currencyReceived,
                                                final String receivedMethod) throws SQLException {
        final String query = "select sum(cantidad_recibida) as total "
                              + "from transaccion " 
                              + "where tipo = ? and moneda_recibida = ? and metodo_recibido = ? ";
        
        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, transType);
            st.setString(2, currencyReceived);
            st.setString(3, receivedMethod);

            final ResultSet rs = st.executeQuery();
            rs.next();

            Float total = rs.getFloat("total");
            rs.close();
            st.close();

            return total;
        }
    }

    public static void updateRegister(final Connection conn, final String table, final String colum,
                                      final Object value, final String condition) throws SQLException {
        String sql = "update " + table + " set " + colum + " = ? where " + condition;
        
        try (final PreparedStatement st = conn.prepareStatement(sql)) {
            switch (value.getClass().getSimpleName()) {
                case "String": st.setString(1, (String) value);
                    break;
                case "Double": st.setDouble(1, (Double) value);
                    break;
                case "Float": st.setFloat(1, (Float) value);
                    break;
                case "Integer": st.setInt(1, (Integer) value);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de dato no soportado: " + value.getClass().getSimpleName());
            }
            st.executeUpdate();
        };
    }

    public static void createTable(final Connection conn, final String name, final List<String> colums,
                                    final List<String> types) throws SQLException {
        StringBuilder sql = new StringBuilder("create table " + name + " (");

        for (int i = 0; i < colums.size(); i++) {
            sql.append(colums.get(i)).append(" ").append(types.get(i));
                if (i < colums.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(");");

        try (final PreparedStatement st = conn.prepareStatement(sql.toString())) {
            st.executeUpdate();
            st.close();
        }
    }

    public static boolean isEmptyTable(final Connection conn, final String tableName) throws SQLException {
            final String query = "select exists (select 1 from " + tableName + ")";
           
            try (final PreparedStatement st = conn.prepareStatement(query)) {
                
                ResultSet rs = st.executeQuery();
                st.close();
    
                return !rs.getBoolean(1);
            }
        }
        
        public static void contabilidadBancariaRegister(final Connection conn, final String nombreBanco, final String numeroCuenta,
                                                        final String tipoCuenta, final String moneda, 
                                                        final Double saldo) throws SQLException {
            final String query = "insert into contabilidad_bancaria(nombre_banco, numero_cuenta, tipo_cuenta, moneda, saldo_actual) " +
                                  "values (?, ?, ?, ?, ?)";
            
            try (final PreparedStatement st = conn.prepareStatement(query)) {
                st.setString(1, nombreBanco);
                st.setString(2, numeroCuenta);
                st.setString(3, tipoCuenta);
                st.setString(4, moneda);
                st.setDouble(5, saldo);

                st.executeUpdate();
                st.close();
            }
        }

        public static void simuinvRegister(final Connection conn, final Date fecha, final java.sql.Timestamp hora, final String tipo_mov,
                                            final Double cantidad, final String moneda, final String metodo,
                                            final String tipo) throws SQLException {
            final String query = "insert into simuinv(fecha, hora, tipo_movimiento, cantidad, moneda, metodo, tipo)" +
                                "values (?, ?, ?, ?, ?, ?, ?);";

            try (final PreparedStatement st = conn.prepareStatement(query)) {
                st.setDate(1, fecha);
                st.setTimestamp(2, hora);
                st.setString(3, tipo_mov);
                st.setDouble(4, cantidad);
                st.setString(5, moneda);
                st.setString(6, metodo);
                st.setString(7, tipo);

                st.executeUpdate();
                st.close();
            }
        }

    public static void newRegister(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String username, password;

        do {
            System.out.println("Ingrese su nombre de usuario: ");
            username = sc.nextLine();
            System.out.println("Ingrese su clave: ");
            password = sc.nextLine();

            if(!Administradores.authenticateUser(conn, username, password)) {
                System.out.println("[ERROR] Usuario o clave invalidas");
            }

        } while(!Administradores.authenticateUser(conn, username, password));
        

        Character op;

        String transType;
        String typeMoneyReceived;
        String metodoRecibido;
        String typeMoneySent;
        String metodoEnviado;

        Double tasa;
        Double tasaMadre;
        Double gananciaPerdida;
        Boolean exec = true;
        Double cantRecibida;

        Integer menuOp = 0;
        
        while (exec) {
            transType = "";
            typeMoneyReceived = "";
            metodoRecibido = "";
            typeMoneySent = "";
            metodoEnviado = "";

            tasa = 0.0;
            gananciaPerdida = 0.0;
            cantRecibida = 0.0;

            System.out.println("Menu Principal\n\t[1] Resgistrar Transaccion\n\t[2] Iniciar Ciclo\n\t[3] Cerrar ciclo actual\n\t[4] Ver inventario\n\t[0] Salir del programa");
            menuOp = sc.nextInt(); sc.nextLine();
            
            switch (menuOp) {
                case 1: {
                    if (TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "status", "tipo", "INVERSION").equals("INACTIVE") 
                        || TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "status", "tipo", "INVERSION").equals("VOID")) {
                        System.err.println("[WARNING]: No hay ningun ciclo activo para realizar una transaccion de compra o venta");
                        break;
                    }

                    do {
                        System.out.println("Seleccione el Tipo de transaccion:\n\t[C] Compra\n\t[V] Venta\n\t[W] Swap\nTu seleccion: ");
                        op = sc.next().toUpperCase().charAt(0);

                        if (!(!op.equals('C') && !op.equals('V') && !op.equals('W')))
                            transType = checkTransType.check(op);

                    } while (!op.equals('C') && !op.equals('V') && !op.equals('W'));
                    
                    do {
                        System.err.println("Que tipo de moneda se ha recibido?\n\t[B] Bolivares\n\t[D] Dolares\n\t[T] USDT\n\t[Z] Zelle\n\t Tu seleccion: ");
                        op = sc.next().toUpperCase().charAt(0);
                        
                        if (!(!op.equals('B') && !op.equals('D') && !op.equals('T') && !op.equals('Z')))
                        typeMoneyReceived = checkMoneyType.check(op);
                        
                    } while (!op.equals('B') && !op.equals('D') && !op.equals('T') && !op.equals('Z'));

                    Integer opint = 0;
                    List<String> nombreBanco = BanksCompany.getInfoOf(conn, "nombre_banco", "moneda", typeMoneyReceived);
                    List<String> codigos = BanksCompany.getInfoOf(conn, "codigo", "moneda", typeMoneyReceived);
                    
                    do {
                        System.out.println("Indique cual fue el metodo recibido?\n");
                                
                        System.out.printf("%-30s%-30s%n", "Banco", "Codigo");
                                
                        for (int index = 0; index < Math.min(nombreBanco.size(), codigos.size()); index++) {
                            System.out.printf("%-30s%-30s%n", (index + 1) + ". " + nombreBanco.get(index), codigos.get(index));
                        }
                                
                        opint = sc.nextInt(); sc.nextLine();

                        if (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size()))) {
                                System.out.println("[WARNING]: debe elegir una opcion en el rango establecido");
                        }

                    } while (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size())));
                    
                    metodoRecibido = codigos.get(opint - 1);
                    
                    System.out.println("Introduzca la cantidad recibida: ");
                    cantRecibida = sc.nextDouble(); sc.nextLine();

                    do {
                        System.err.println("Que tipo de moneda se ha enviado?\n\t[B] Bolivares\n\t[D] Dolares\n\t[T] USDT\n\t[Z] Zelle\n\t Tu seleccion: ");
                        op = sc.next().toUpperCase().charAt(0);
                        
                        if (!(!op.equals('B') && !op.equals('D') && !op.equals('T') && !op.equals('Z')))
                        typeMoneySent = checkMoneyType.check(op);
                        
                    } while (!op.equals('B') && !op.equals('D') && !op.equals('T') && !op.equals('Z'));
                    
                    opint = 0;
                    nombreBanco = BanksCompany.getInfoOf(conn, "nombre_banco", "moneda", typeMoneySent);
                    codigos = BanksCompany.getInfoOf(conn, "codigo", "moneda", typeMoneySent);
                    
                    do {
                        System.out.println("Indique cual fue el metodo enviado?\n");
                                
                        System.out.printf("%-30s%-30s%n", "Banco", "Codigo");
                                
                        for (int index = 0; index < Math.min(nombreBanco.size(), codigos.size()); index++) {
                            System.out.printf("%-30s%-30s%n", (index + 1) + ". " + nombreBanco.get(index), codigos.get(index));
                        }
                                
                        opint = sc.nextInt(); sc.nextLine();

                        if (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size()))) {
                                System.out.println("[WARNING]: debe elegir una opcion en el rango establecido");
                        }

                    } while (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size())));
                    
                    metodoEnviado = codigos.get(opint - 1);

                    System.out.println("Digite la cantidad Enviada: ");
                    Double cantEnviada = sc.nextDouble();   sc.nextLine();

                    System.err.println("Digite la tasa: ");
                    tasa = sc.nextDouble(); sc.nextLine();
                    tasaMadre = Double.parseDouble(TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "tasa", "tipo", "INVERSION"));

                    switch (transType) {
                        case "COMPRA": {
                            gananciaPerdida = cantRecibida - (cantEnviada / tasaMadre);
                        } break;
                        case "VENTA": {
                            gananciaPerdida = cantRecibida - (cantEnviada / tasaMadre);
                        } break;
                        case "SWAP": {
                            gananciaPerdida = cantRecibida - (cantEnviada / tasaMadre);
                        } break;
                        default:
                            gananciaPerdida = 0.0;
                            break;
                    }

                    gananciaPerdida = gananciaPerdida < 0 ? gananciaPerdida * -1 : gananciaPerdida; 

                    final String query = "insert into simutrans(cedula_cliente, admin, fecha, tipo, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ganancia, ref_bancaria) " + 
                    "values ('V-11222599', " + "'" + username + "', " + "'" + TimeZone.getDateZoneCaracas() + "', " + "'" + transType + "', "
                    + "'" + cantRecibida + "', " + "'" + typeMoneyReceived + "', " + "'" + metodoRecibido + "', " + "'" + cantEnviada + "', " + "'" + typeMoneySent + "', " + "'" + metodoEnviado + "', " + "'OK', " + "'" + tasa + "', " + "'" + gananciaPerdida + "', " + "'123456789555'" + ");"; 

                    System.out.println("Estos son los datos del registro:\n" +
                    "Cedula Cliente: " + "V-11222599" + "\n" +
                    "Fecha: " + TimeZone.getDateZoneCaracas() + "\n" +
                    "Tipo de transaccion: " + transType + "\n" + 
                    "Cantidad recibida: " + cantRecibida + " " + typeMoneyReceived + "\n" +
                    "Recibido en: " + metodoRecibido + "\n" + 
                    "Cantidad enviada: " + cantEnviada + " " + typeMoneySent + "\n" +
                    "Enviado desde: " + metodoEnviado + "\n" + 
                    "Tasa: " + tasa + " " + typeMoneySent + "\n" + 
                    "G/P: " + gananciaPerdida + "\nEsta seguro de que desea registrar esta transaccion? y/n: ");

                    Character ch = sc.next().toUpperCase().charAt(0); sc.nextLine();

                    final Double totalBalanceEnviado =  BanksCompany.getTotalBalanceOf(conn, metodoEnviado);
                    final Double totalBalanceRecibido = BanksCompany.getTotalBalanceOf(conn, metodoRecibido);

                    if (totalBalanceEnviado <= 0.0 || totalBalanceEnviado < cantEnviada) {
                        System.out.println("[ERROR] No hay saldo suficiente en " + typeMoneySent + "\nSe poseen: " + totalBalanceEnviado + typeMoneySent);
                        break;
                    } else {
                        String condition = "codigo = " + "'" + metodoEnviado + "'";
                        updateRegister(conn, "bancos", "saldo_actual", (totalBalanceEnviado - cantEnviada), condition);
                        condition = "codigo = " + "'" + metodoRecibido + "'";
                        updateRegister(conn, "bancos", "saldo_actual", (totalBalanceRecibido + cantRecibida), condition);

                    }

                    switch (ch) {
                        case 'Y': {
                            try (final PreparedStatement st = conn.prepareStatement(query)) {
                                st.executeUpdate();
                                final String dateTimeQuery = "select fecha, hora from simutrans order by referencia desc limit 1";

                                try (final PreparedStatement stm = conn.prepareStatement(dateTimeQuery)) {
                                    ResultSet rs = stm.executeQuery();
                                    if (rs.next()) {
                                        simuinvRegister(conn, rs.getDate("fecha"), rs.getTimestamp("hora"), "INGRESO", cantRecibida, typeMoneySent, metodoRecibido, transType);
                                        simuinvRegister(conn, rs.getDate("fecha"), rs.getTimestamp("hora"), "EGRESO", cantEnviada, typeMoneyReceived, metodoEnviado, transType);
                                    } else {
                                        System.err.println("No data found in simutrans table.");
                                    }
                                    stm.close();
                                }
                                st.close();
                            }
                        } break;
                        case 'N': {
                            System.out.println("[WARNING]: No se completo el registro!");
                        } break;
                        default: {
                            System.out.println("[WARNING] Opcion no valida");
                        } break;
                    }
                } break;

                case 2: {
                    if (TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "status", "tipo", "INVERSION").equals("ACTIVE")) {
                        System.out.println("[WARNING] Ya hay un ciclo activo en este momento");
                        break;
                    }

                    System.out.println("Digite la cantidad Enviada en USDT: ");
                    Double cantEnviada = sc.nextDouble();   sc.nextLine();

                    System.err.println("Digite la tasa USDT/Bs Ref: ");
                    tasa = sc.nextDouble(); sc.nextLine();

                    final List<String> nombreBanco = BanksCompany.getInfoOf(conn, "nombre_banco", "moneda", "VES");
                    final List<String> codigos = BanksCompany.getInfoOf(conn, "codigo", "moneda", "VES");

                    Integer opint = 0;

                    do {
                        System.out.println("Indique cual fue el metodo recibido?\n");
                        
                        System.out.printf("%-30s%-30s%n", "Banco", "Codigo");
                        
                        for (int index = 0; index < Math.min(nombreBanco.size(), codigos.size()); index++) {
                            System.out.printf("%-30s%-30s%n", (index + 1) + ". " + nombreBanco.get(index), codigos.get(index));
                        }
                        
                        opint = sc.nextInt(); sc.nextLine();

                        if (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size()))) {
                            System.out.println("[WARNING]: debe elegir una opcion en el rango establecido");
                        }

                    } while (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size())));
                    
                    // metodoRecibido = nombreBanco.get(opint - 1);
                    String  nCuenta = codigos.get(opint - 1).trim();

                    System.out.println("Introduzca la referencia bancaria: ");
                    String ref_bancaria = sc.nextLine();

                    gananciaPerdida = cantEnviada * -1;
                    cantRecibida = cantEnviada * tasa;

                    final String query = "insert into simutrans(cedula_cliente, admin, tipo, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ganancia, ref_bancaria) " + 
                                        "values ('V-11222599', " + "'" + username + "', " + "'" + TransTypes.INVERSION.getTransType() + "', "
                                        + "'" + cantRecibida + "', " + "'" + MoneyType.BOLIVARES.getNombre() + "', " + "'" + nCuenta + "', " + "'" + cantEnviada + "', " + "'" + MoneyType.BINANCE_USDT.getNombre() + "', " + "'" + "BE-WN-0006" + "', " + "'ACTIVE', " + "'" + tasa + "', " + "'" + gananciaPerdida + "', " + "'" + ref_bancaria + "');";
                    
                    System.out.println("Estos son los datos del registro:\n" +
                    "Fecha: " + TimeZone.getDateZoneCaracas() + "\n" +
                    "Tipo de transaccion: " + TransTypes.INVERSION.getTransType() + "\n" + 
                    "Cantidad recibida: " + cantRecibida + " " + MoneyType.BOLIVARES.getNombre() + "\n" +
                    "Recibido en: " + nCuenta + "\n" + 
                    "Cantidad enviada: " + cantEnviada + " " + MoneyType.BINANCE_USDT.getNombre() + "\n" +
                    "Enviado desde: BE-WN-0006" + "\n" + 
                    "Tasa: " + tasa + " " + MoneyType.BINANCE_USDT.getNombre() + "\n" + 
                    "G/P: " + gananciaPerdida + "\nEsta seguro de que desea registrar esta transaccion? y/n: ");

                    Character ch = sc.next().toUpperCase().charAt(0); sc.nextLine();

                   // final Double totalBalanceVes = BanksCompany.getTotalBalanceOf(conn, "VES");
                    final Double totalBalanceUsdt =  BanksCompany.getTotalBalanceOf(conn, "USDT");
                    
                    if (totalBalanceUsdt <= 0.0 || totalBalanceUsdt < cantEnviada) {
                        System.out.println("[ERROR] No hay saldo suficiente en USDT\nSe poseen: " + totalBalanceUsdt + " USDT");
                        break;
                    } else {
                        final String condition = "codigo = " + "'" + "BE-WN-0006" + "'";
                        updateRegister(conn, "bancos", "saldo_actual", (totalBalanceUsdt - cantEnviada), condition);
                    }

                    switch (ch) {
                        case 'Y': {
                            try (final PreparedStatement st = conn.prepareStatement(query)) {
                                st.executeUpdate();
                                final String dateTimeQuery = "select fecha, hora from simutrans order by referencia desc limit 1";

                                try (final PreparedStatement stm = conn.prepareStatement(dateTimeQuery)) {
                                    ResultSet rs = stm.executeQuery();
                                    if (rs.next()) {
                                        simuinvRegister(conn, rs.getDate("fecha"), rs.getTimestamp("hora"), "INGRESO", cantRecibida, MoneyType.BOLIVARES.getNombre(), metodoRecibido, "COMPRA");
                                        simuinvRegister(conn, rs.getDate("fecha"), rs.getTimestamp("hora"), "EGRESO", cantEnviada, MoneyType.BINANCE_USDT.getNombre(), PlataformasOnline.BINANCE.getNombre(), "VENTA");
                                    } else {
                                        System.err.println("No data found in simutrans table.");
                                    }
                                    stm.close();
                                }
                                st.close();
                            }
                            } break;
                        case 'N': {
                            System.out.println("[WARNING]: No se completo el registro");
                        } break;
                        default: {
                            System.out.println("[WARNING] Opcion no valida");
                        } break;
                    }
                } break;

                case 3: {
                    if (TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "status", "tipo", "INVERSION").equals("INACTIVE") ||
                        TableInfo.getInfoByLastReferenceOf(conn, "simutrans", "status", "tipo", "INVERSION").equals("VOID")) {
                        System.err.println("[WARNING]: No hay ningun ciclo activo para cerrar");
                        break;
                    }

                    final String condition = "referencia = (SELECT referencia FROM simutrans WHERE tipo = 'INVERSION' order by referencia desc limit 1)";
                    updateRegister(conn, "simutrans", "status", "INACTIVE", condition);

                    System.out.println("[INFO]: Se ha cerrado el ciclo correctamente");
                } break;
                
                case 4: {
                    final String query = "select * from simuinv";
                    try (PreparedStatement st = conn.prepareStatement(query)) {
                        
                        final ResultSet rs = st.executeQuery();
                        StringBuilder metaData = new StringBuilder();
                        
                        if (rs.next()) {
                            while (rs.next()) {
                                metaData.append(", fecha: ").append(String.valueOf(rs.getString("fecha")))
                                .append("hora: ").append(String.valueOf(rs.getString("hora")))
                                .append(", tipo: ").append(rs.getString("tipo_movimiento"))
                                .append(", moneda: ").append(rs.getString("moneda"))
                                .append(", cantidad: ").append(rs.getString("cantidad"))
                                .append(", metodo: ").append(rs.getString("metodo"));
                            }
                        } else {
                            System.out.println("[WARNING] No hay registros disponibles");
                        }
                        rs.close();
                        st.close();
                    }
  
                } break;
                case 0: {
                    menuOp = 0;
                    exec = false;
                } break;

                default: {
                    System.out.println("[ERROR] Opcion no valida");
                } break;
            }
        }
        sc.close();
    }
}