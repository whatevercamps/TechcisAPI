CREATE TABLE SERVICIO (
ID NUMBER NOT NULL, 
NOMBRE VARCHAR2(60 BYTE),
COSTO NUMBER NOT NULL,
CONSTRAINT PK_SERVICIO PRIMARY KEY (ID), 
CONSTRAINT CK_COSTO_SERV CHECK (COSTO >= 0)
);