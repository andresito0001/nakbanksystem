package main.test.java;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import main.java.dao.AdminDAO;
import main.java.util.DatabaseUtils;
import main.java.util.CheckTypes.*;
import main.java.util.bancos.PlataformasOnline;
import main.java.util.monedas.MoneyType;
import main.java.dao.BanksDAO;
import main.java.dao.InventoryDAO;
import main.java.dao.CicleDAO;
import main.java.services.CicleService;
import main.java.util.TimeZone;
import main.java.util.TransTypes;
import main.java.util.ULID;

public class transactionsAppCLI {
    public transactionsAppCLI(final Connection conn) {
        this.conn = conn;
        admin = new AdminDAO(conn);
        dbUtils = new DatabaseUtils(conn);
        banksDAO = new BanksDAO(conn);
        inventoryDAO = new InventoryDAO(conn);
    }

    public void executeApp() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String username = "", password = "";
        
        Character op;
        Integer menuOp = 0;
        
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

        do {
            System.out.println("Ingrese su nombre de usuario: ");
            username = sc.nextLine();
            System.out.println("Ingrese su clave: ");
            password = sc.nextLine();

            if(!admin.authenticateUser(username, password)) {
                System.out.println("[ERROR] Usuario o clave invalidas");
            }

        } while(!admin.authenticateUser(username, password));

        while (exec) {
            transType = "";
            typeMoneyReceived = "";
            metodoRecibido = "";
            typeMoneySent = "";
            metodoEnviado = "";

            tasa = 0.0;
            gananciaPerdida = 0.0;
            cantRecibida = 0.0;

            System.out.println("Menu Principal\n\t[1] Resgistrar Transaccion\n\t[2] Iniciar Ciclo\n\t[3] Cerrar ciclo actual\n\t[4] Ver inventario\n\t[5] Transferencia a cuentas propias (SOLO CUENTAS EN BOLIVARES)\n\t[6] Ver ganancia de un ciclo\n\t[7] Consultar ciclos por fecha\n\t[8] Ver ganacia total de ciclos entre un rango de fechas...\n\t[0] Salir del programa");
            menuOp = sc.nextInt(); sc.nextLine();

            switch (menuOp) {
                case 1: {
                    if (dbUtils.getInfoByLastReferenceOf("cicles", "status", null, null).equals("INACTIVE") 
                        || dbUtils.getInfoByLastReferenceOf("cicles", "status",null, null).equals("VOID")) {
                        System.err.println("[WARNING]: No hay ningun ciclo activo para realizar una transaccion de compra o venta");
                        break;
                    }

                    do {
                        System.out.println("Seleccione el Tipo de transaccion:\n\t[C] Compra\n\t[V] Venta (SIN USO)\n\t[W] Swap\nTu seleccion: ");
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
                    List<String> nombreBanco = banksDAO.getInfoOf("nombre_banco", "moneda", typeMoneyReceived);
                    List<String> codigos = banksDAO.getInfoOf("codigo", "moneda", typeMoneyReceived);
                    
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
                    nombreBanco = banksDAO.getInfoOf("nombre_banco", "moneda", typeMoneySent);
                    codigos = banksDAO.getInfoOf("codigo", "moneda", typeMoneySent);
                    
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
                    tasaMadre = Double.parseDouble(dbUtils.getInfoByLastReferenceOf("cicles", "tasa", null, null));

                    System.out.println(tasaMadre);
                    switch (transType) {
                        case "COMPRA": {
                            gananciaPerdida = cantRecibida - (cantEnviada / tasaMadre);                            
                        } break;
                        case "VENTA": {
                            gananciaPerdida = cantRecibida - (cantEnviada / tasaMadre);
                        } break;
                        case "SWAP": {
                            gananciaPerdida = cantRecibida - cantEnviada; //como la sacariamos aqui?
                        } break;
                        default:
                            gananciaPerdida = 0.0;
                            break;
                    }

                    gananciaPerdida = gananciaPerdida < 0 ? gananciaPerdida * -1 : gananciaPerdida; 
                    
                    String cicle_id = dbUtils.getInfoByLastReferenceOf("cicles", "id", null, null);

                    byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                    final String id = ULID.generate(System.currentTimeMillis(), entropy);
        
                    final String query = "insert into trans(id, cicle_id, cedula_cliente, admin, fecha, tipo, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ganancia, ref_bancaria) " + 
                    "values (" + "'" + id + "', "  + "'" + cicle_id + "', " + "'V-11222599', " + "'" + username + "', " + "'" + TimeZone.getDateZoneCaracas() + "', " + "'" + transType + "', "
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

                    final Double totalBalanceEnviado =  banksDAO.getTotalBalanceOf(metodoEnviado);
                    final Double totalBalanceRecibido = banksDAO.getTotalBalanceOf(metodoRecibido);

                    if (totalBalanceEnviado <= 0.0 || totalBalanceEnviado < cantEnviada) {
                        System.out.println("[ERROR] No hay saldo suficiente en " + typeMoneySent + "\nSe poseen: " + totalBalanceEnviado + typeMoneySent);
                        break;
                    } else {
                        String condition = "codigo = " + "'" + metodoEnviado + "'";
                        dbUtils.updateRegister("bancos", "saldo_actual", (totalBalanceEnviado - cantEnviada), condition);
                        condition = "codigo = " + "'" + metodoRecibido + "'";
                        dbUtils.updateRegister("bancos", "saldo_actual", (totalBalanceRecibido + cantRecibida), condition);
                    }

                    switch (ch) {
                        case 'Y': {
                            try (final PreparedStatement st = conn.prepareStatement(query)) {
                                st.executeUpdate();
                                final String dateTimeQuery = "select fecha, hora from trans order by id desc limit 1";

                                try (final PreparedStatement stm = conn.prepareStatement(dateTimeQuery)) {
                                    ResultSet rs = stm.executeQuery();
                                    if (rs.next()) {
                                        inventoryDAO.newRegister(rs.getDate("fecha"), rs.getTimestamp("hora"), "INGRESO", cantRecibida, typeMoneySent, metodoRecibido, transType);
                                        inventoryDAO.newRegister(rs.getDate("fecha"), rs.getTimestamp("hora"), "EGRESO", cantEnviada, typeMoneyReceived, metodoEnviado, transType);
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
                    if (dbUtils.getInfoByLastReferenceOf("cicles", "status", null, null).equals("ACTIVE")) {
                        System.out.println("[WARNING] Ya hay un ciclo activo en este momento");
                        break;
                    }

                    System.out.println("Digite la cantidad Enviada en USDT: ");
                    Double cantEnviada = sc.nextDouble();   sc.nextLine();

                    System.err.println("Digite la tasa USDT/Bs Ref: ");
                    tasa = sc.nextDouble(); sc.nextLine();

                    final List<String> nombreBanco = banksDAO.getInfoOf("nombre_banco", "moneda", "VES");
                    final List<String> codigos = banksDAO.getInfoOf("codigo", "moneda", "VES");

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
                    
                    String  nCuenta = codigos.get(opint - 1).trim();

                    System.out.println("Introduzca la referencia bancaria: ");
                    String ref_bancaria = sc.nextLine();

                    // gananciaPerdida = cantEnviada * -1;
                    cantRecibida = cantEnviada * tasa;

                    // gen ulid
                    byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                    final String id = ULID.generate(System.currentTimeMillis(), entropy);
        
                    final String query = "insert into cicles(id, cedula_cliente, admin, fecha, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria) " + 
                                        "values (" + "'" + id + "', " + "'V-11222599', " + "'" + username + "', " +  "'" + TimeZone.getDateZoneCaracas() + "', " +
                                        "'" + cantRecibida + "', " + "'" + MoneyType.BOLIVARES.getNombre() + "', " + "'" + nCuenta + "', " + "'" + cantEnviada + "', " + "'" + MoneyType.BINANCE_USDT.getNombre() + "', " + "'" + "BE-WN-0006" + "', " + "'ACTIVE', " + "'" + tasa + "', " + "'" + ref_bancaria + "');";
                    
                    System.out.println("Estos son los datos del registro:\n" +
                    "Fecha: " + TimeZone.getDateZoneCaracas() + "\n" +
                    "Tipo de transaccion: " + TransTypes.INVERSION.getTransType() + "\n" + 
                    "Cantidad recibida: " + cantRecibida + " " + MoneyType.BOLIVARES.getNombre() + "\n" +
                    "Recibido en: " + nCuenta + "\n" + 
                    "Cantidad enviada: " + cantEnviada + " " + MoneyType.BINANCE_USDT.getNombre() + "\n" +
                    "Enviado desde: BE-WN-0006" + "\n" + 
                    "Tasa: " + tasa + " " + MoneyType.BINANCE_USDT.getNombre() + "\n" + 
                    "\nEsta seguro de que desea registrar esta transaccion? y/n: ");

                    Character ch = sc.next().toUpperCase().charAt(0); sc.nextLine();

                    final Double totalBalanceUsdt =  banksDAO.getTotalBalanceOf("BE-WN-0006");
                    
                    if (totalBalanceUsdt <= 0.0 || totalBalanceUsdt < cantEnviada) {
                        System.out.println("[ERROR] No hay saldo suficiente en USDT\nSe poseen: " + totalBalanceUsdt + " USDT");
                        break;
                    }

                    switch (ch) {
                        case 'Y': {
                            try (final PreparedStatement st = conn.prepareStatement(query)) {
                                st.executeUpdate();
                                final String dateTimeQuery = "select fecha, hora from cicles order by id desc limit 1";

                                try (final PreparedStatement stm = conn.prepareStatement(dateTimeQuery)) {
                                    ResultSet rs = stm.executeQuery();
                                    if (rs.next()) {
                                        inventoryDAO.newRegister(rs.getDate("fecha"), rs.getTimestamp("hora"), "INGRESO", cantRecibida, MoneyType.BOLIVARES.getNombre(), metodoRecibido, "COMPRA");
                                        inventoryDAO.newRegister(rs.getDate("fecha"), rs.getTimestamp("hora"), "EGRESO", cantEnviada, MoneyType.BINANCE_USDT.getNombre(), PlataformasOnline.BINANCE.getNombre(), "VENTA");
                                        
                                        dbUtils.updateRegister("bancos", "saldo_actual", (totalBalanceUsdt - cantEnviada), "codigo = " + "'" + "BE-WN-0006" + "'");
                                        dbUtils.updateRegister("bancos", "saldo_actual", cantRecibida, "codigo = " + "'" + nCuenta + "'");
                                    } else {
                                        System.err.println("No data found in cicles table.");
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
                    if (dbUtils.getInfoByLastReferenceOf("cicles", "status", null, null).equals("INACTIVE") ||
                        dbUtils.getInfoByLastReferenceOf("cicles", "status", null, null).equals("VOID")) {
                        System.err.println("[WARNING]: No hay ningun ciclo activo para cerrar");
                        break;
                    }

                    final String condition = "id = (select id from cicles order by id desc limit 1)";
                    dbUtils.updateRegister("cicles", "status", "INACTIVE", condition);

                    System.out.println("[INFO]: Se ha cerrado el ciclo correctamente");
                } break;
                
                case 4: {
                    final String query = "select * from bancos";
                    try (PreparedStatement st = conn.prepareStatement(query)) {
                        final ResultSet rs = st.executeQuery();
                        StringBuilder metaData = new StringBuilder();
                        
                        if (rs.next()) {
                            while (rs.next()) {
                                // metaData.append(", fecha: ").append(String.valueOf(rs.getString("fecha")))
                                // .append("hora: ").append(String.valueOf(rs.getString("hora")))
                                // .append(", tipo: ").append(rs.getString("tipo_movimiento"))
                                // .append(", moneda: ").append(rs.getString("moneda"))
                                // .append(", cantidad: ").append(rs.getString("cantidad"))
                                // .append(", metodo: ").append(rs.getString("metodo"));

                                metaData.append("Banco: ").append(rs.getString("nombre_banco"))
                                .append(", Codigo: ").append(rs.getString("codigo"))
                                .append(", saldo: ").append(rs.getDouble("saldo_actual"))
                                .append(" ").append(rs.getString("moneda")).append('\n');
                            }

                            System.out.println(metaData);
                        } else {
                            System.out.println("[WARNING] No hay registros disponibles");
                        }
                        rs.close();
                        st.close();
                    }
                } break;

                case 5: { 
                    Integer opint = 0;

                    final List<String> nombreBanco = banksDAO.getInfoOf("nombre_banco", "moneda", "VES");
                    final List<String> codigos = banksDAO.getInfoOf("codigo", "moneda", "VES");

                    do {
                        System.out.println("Seleccione la cuenta a debitar\n");
                        
                        System.out.printf("%-30s%-30s%n", "Banco", "Codigo");
                        
                        for (int index = 0; index < Math.min(nombreBanco.size(), codigos.size()); index++) {
                            System.out.printf("%-30s%-30s%n", (index + 1) + ". " + nombreBanco.get(index), codigos.get(index));
                        }
                        
                        opint = sc.nextInt(); sc.nextLine();

                        if (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size()))) {
                            System.out.println("[WARNING]: debe elegir una opcion en el rango establecido");
                        }

                    } while (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size())));
                    
                    String cuentaDebitar = codigos.get(opint - 1);
                    opint = 0;

                    do {
                        System.out.println("Seleccione la cuenta a depositar\n");
                        
                        System.out.printf("%-30s%-30s%n", "Banco", "Codigo");
                        
                        for (int index = 0; index < Math.min(nombreBanco.size(), codigos.size()); index++) {
                            System.out.printf("%-30s%-30s%n", (index + 1) + ". " + nombreBanco.get(index), codigos.get(index));
                        }
                        
                        opint = sc.nextInt(); sc.nextLine();

                        if (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size()))) {
                            System.out.println("[WARNING]: debe elegir una opcion en el rango establecido");
                        }

                    } while (!(opint >= 1 && opint <= Math.min(nombreBanco.size(), codigos.size())));

                    String cuentaDepositar = codigos.get(opint - 1);

                    System.out.println("Monto: ");
                    Double monto = sc.nextDouble(); sc.nextLine();
                    
                    final Double saldoCuentaDebitar = (Double)dbUtils.getValueOf("bancos", "saldo_actual", "codigo = " + "'" + cuentaDebitar + "'");
                    final Double saldoCuentaDepositar = (Double)dbUtils.getValueOf("bancos", "saldo_actual", "codigo = " + "'" + cuentaDepositar + "'");
                    
                    if (saldoCuentaDebitar < monto) {
                        System.out.println("[ERROR]: No hay saldo disponible en: " + cuentaDebitar);
                        break;
                    } else if (monto <= 0) {
                        System.out.println("[ERROR]: Solo estan permitidos los numeros positivos sin incluir el 0");
                    }

                    dbUtils.updateRegister("bancos", "saldo_actual", saldoCuentaDebitar - monto, "codigo = " + "'" + cuentaDebitar + "'");
                    dbUtils.updateRegister("bancos", "saldo_actual", saldoCuentaDepositar + monto , "codigo = " + "'" + cuentaDepositar + "'");
                } break;

                case 6: {
                    String id;
                    DatabaseUtils dbutils = new DatabaseUtils(conn);
                    CicleService cicleService = new CicleService(conn);

                    System.out.println("Ingrese el ID del ciclo: ");
                    id = sc.nextLine();

                    System.out.println("ID Ciclo: " + id.trim() + ", Inversion Inicial: " + dbutils.getValueOf("cicles", "cantidad_enviada", "id = " + "'" + id.trim() + "'") + " USDT, " + " Ganancia: " + cicleService.getProfit(id) + " USD ");
                } break;

                case 7: {
                    CicleDAO cicle = new CicleDAO(conn);
                    String beginDate, endDate;
                    
                    System.out.println("fecha de inicio\n> ");
                    beginDate = sc.nextLine();
                    System.out.println("fecha fin\n> ");
                    endDate = sc.nextLine();
                            
                    StringBuilder sb = cicle.fillByDate(Date.valueOf(beginDate), Date.valueOf(endDate));
                    System.out.println(sb);
                } break;

                case 8: {
                    CicleDAO cicle = new CicleDAO(conn);
                    CicleService cicleService = new CicleService(conn);

                    String beginDate, endDate;
                    
                    System.out.println("fecha de inicio\n> ");
                    beginDate = sc.nextLine();
                    System.out.println("fecha fin\n> ");
                    endDate = sc.nextLine();
                    
                    final List<String> ids = cicle.getIdsByDate(Date.valueOf(beginDate), Date.valueOf(endDate));
                    final Double profit = cicleService.getProfitByIds(ids);
                    final Integer numTrans = cicleService.getTotalOfTransByCiclesIds(ids);

                    System.out.println("ganacia de los ciclos entre " + beginDate + " | " + endDate + "\n" 
                    + "Total USD obtenidos: " + profit + '\n' 
                    + "Numero de transacciones: " + numTrans + "\n"
                    + "Tasa de compra promedio de USD a VES: " + cicleService.averagePurchaseRatebYIds(ids) + '\n'
                    + "");
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
    
    private final Connection conn;
    private final AdminDAO admin;
    private final DatabaseUtils dbUtils;
    private final BanksDAO banksDAO;
    private final InventoryDAO inventoryDAO;
}