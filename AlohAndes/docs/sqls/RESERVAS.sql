CREATE TABLE RESERVAS (
ID NUMBER  GENERATED by default on null as IDENTITY,
ID_CLIENTE NUMBER NOT NULL, 
ID_PROPUESTA NUMBER NOT NULL,
FECHA_INIC DATE,
FECHA_FINA DATE,
CONSTRAINT PK_RESERVA PRIMARY KEY (ID),
CONSTRAINT FK_ID_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTES (ID),
CONSTRAINT FK_ID_PROPUESTA FOREIGN KEY (ID_PROPUESTA) REFERENCES PROPUESTAS (ID)
);

