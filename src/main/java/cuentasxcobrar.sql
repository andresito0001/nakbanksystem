 SELECT inventario.id_trans,
    concat(clientes.nombre, ' ', clientes.apellido, ' (', clientes.alias, ')') AS cliente,
    transacciones.cantidad_recibida AS monto_transaccion,
    transacciones.moneda_recibida AS moneda_trans,
    sum(inventario.cantidad) AS abonado,
    inventario.moneda,
    transacciones.tipo,
    transacciones.cantidad_recibida - sum(inventario.cantidad) AS pendiente,
    transacciones.moneda_recibida
   FROM transacciones
     JOIN clientes ON transacciones.cedula_cliente = clientes.cedula
     LEFT JOIN inventario ON inventario.id_trans = transacciones.id
  WHERE inventario.id_trans = transacciones.id AND (inventario.tipo_movimiento = 'INGRESO'::text OR inventario.tipo_movimiento = 'ABONO'::text) AND transacciones.status = 'ENVIADO'::text
  GROUP BY inventario.id_trans, inventario.moneda, inventario.tipo_movimiento, transacciones.tipo, transacciones.cantidad_recibida, clientes.nombre, clientes.apellido, clientes.alias, transacciones.moneda_recibida;