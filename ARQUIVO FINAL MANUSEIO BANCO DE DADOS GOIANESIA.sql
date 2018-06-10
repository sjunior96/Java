USE GOIANESIA;

INSERT INTO USUARIO (matriculaUsuario, nomeUsuario, 
	telefoneUsuario, login, senha) 
	VALUES('02097', 'SILVIO JÚNIOR', 
    '(61) 9 8609-3016', 'SILVIO', '123');
    
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52418', 'GENOVALDO SALES', '321564789', 'M');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52419', 'DIONE', '321564789', 'M');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52420', 'MARCIEL GOMES', '321564789', 'M');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52421', 'EDIVALDO', '321564789', 'M');
    
-- INSERTS DOS COBRADORES
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52417', 'DORISVAN', '321564789', 'C');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52416', 'LEUZIVAN', '321564789', 'C');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52415', 'ROBSON', '321564789', 'C');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52414', 'FRANCIEL', '321564789', 'C');
INSERT INTO FUNCIONARIO (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario)
	VALUES('52413', 'DIOGO', '321564789', 'C');

-- INSERE OS PREFIXOS/LINHAS
INSERT INTO LINHAS(prefixoLinha, itinerarioLinha) VALUES('12031600 - Convencional', 'BRASÍLIA(DF)/CERES(GO)');
INSERT INTO LINHAS(prefixoLinha, itinerarioLinha) VALUES('12031661 - Executivo', 'BRASÍLIA(DF)/CERES(GO)');
INSERT INTO LINHAS(prefixoLinha, itinerarioLinha) VALUES('12031000 - Convencional', 'BRASÍLIA(DF)/GOIANÉSIA(GO)');
INSERT INTO LINHAS(prefixoLinha, itinerarioLinha) VALUES('12031061 - Executivo', 'BRASÍLIA(DF)/GOIANÉSIA(GO)');

-- INSERE OS DADOS DOS HORÁRIOS
INSERT INTO HORARIOS(horario) VALUES('07:30');
INSERT INTO HORARIOS(horario) VALUES('09:30');
INSERT INTO HORARIOS(horario) VALUES('14:00');
INSERT INTO HORARIOS(horario) VALUES('17:30');

INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(1,1);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(1,2);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(2,3);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(2,4);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(3,1);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(3,2);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(4,3);
INSERT INTO HORARIOS_POR_LINHA (cod_horario, cod_linha) VALUES(4,4);

