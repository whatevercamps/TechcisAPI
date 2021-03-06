SELECT ID1, SUM1, SUM2 
FROM 
    (SELECT ID_OPERADOR ID1, SUM(PAGADO) SUM1
    FROM OPERADORES, FACTURAS 
    WHERE ID_OPERADOR = OPERADORES.ID 
    AND EXTRACT (YEAR FROM FECHA_ULTIMO_PAGO) = 1997
    GROUP BY ID_OPERADOR), 
    (SELECT ID_OPERADOR ID2, SUM(PAGADO) SUM2 
    FROM OPERADORES, FACTURAS 
    WHERE ID_OPERADOR = OPERADORES.ID 
    AND EXTRACT (YEAR FROM FECHA_ULTIMO_PAGO) = 1996
    GROUP BY ID_OPERADOR) 
WHERE ID1 = ID2;