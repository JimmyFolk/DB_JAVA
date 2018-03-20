package com.henry;

import java.sql.*;
import java.util.Random;

public class Main {


    public static void main(String[] args) {

        

        //<---- variaveis para popular tabela com alguns dados aleatorios (caso necessario) ---->

        int numRegistros = 3000; //quantidade de registros que serao criados (entre 1 e 89999)


        int idAc = 1; // id_customer da primeira entrada
        String idCpfString = "456123"; // primeiros 6 digitos do cpf são fixos
        int idCpfRand = 10000; // ultimos 5 digitos do cpf sao gerados aleatoriamente
        String name = "nome"; // todas as entradas no campo nm_customer sao preenchidos pela string "nome"


        Random rn = new Random();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/HenriqueNovaes/OneDrive - Centro Paula Souza - FATEC/udemy/JAVA/testDB/customer.db");
             Statement statement = conn.createStatement()) {


            //<---- cria a tabela (caso inexistente)---->
            statement.execute("CREATE TABLE IF NOT EXISTS tb_customer_account " +
                    "(id_customer INTEGER, cpf_cnpj TEXT, nm_customer TEXT, is_active BLOB, vl_total NUMERIC)");

            //<---- preenche a tabela com dados aleatorios ---->
//            while(idAc <= numRegistros) {
//
//                int isActive = rn.nextInt(2); //gera is_active aleatorio para os clientes
//                double total = rn.nextInt(10000) + 1; //gera vl_total aleatorio para cada cliente
//
//                statement.execute("INSERT INTO tb_customer_account (id_customer, cpf_cnpj, nm_customer, is_active, vl_total)" +
//                        "VALUES( '" + idAc + "', '" + idCpfString+idCpfRand + "', '" + name + "', '" + isActive + "', '" + total + "')");
//                idAc++;
//                idCpfRand++;
//            }

            calc(statement); //chama funcao que calcula media e exibe resultado na tela
            cliente(statement); //chama funcao que lista os clientes envolvidos no calculo

        } catch (SQLException e) {
            System.out.println("Erro!! " + e.getMessage());
        }

    }



    //<---- calcula a media ---->

    public static void calc(Statement statement) {

        Double avg = 0.0; //var onde sera armazenado o resultado do SQL query
        try {
            ResultSet media = statement.executeQuery("SELECT AVG(vl_total) AS media FROM tb_customer_account " +
                    "WHERE vl_total > 560 " +
                    "AND id_customer BETWEEN 1500 AND 2700;"); //media calculada diretamente no query

            media.next();
            avg = media.getDouble("media");

            System.out.println("Média: "+avg);

            media.close();

        } catch (SQLException e) {
            System.out.println("Erro!! " + e.getMessage());
        }


    }


    //<---- lista clientes ---->
    public static void cliente(Statement statement){

        try{
            ResultSet results = statement.executeQuery("SELECT * FROM tb_customer_account WHERE vl_total > 560 AND id_customer BETWEEN 1500 AND 2700 ORDER BY vl_total;");// +

            System.out.println(" ");

            System.out.println("Clientes envolvidos no cálculo ordenados pelo valor total: ");
            System.out.println(" ");
            while (results.next()){
                System.out.println(results.getInt("id_customer") + " " + results.getString("cpf_cnpj") + " "
                        + results.getString("nm_customer") + " " + results.getInt("is_active") + " "
                        + results.getDouble("vl_total"));

            }

            results.close();

        } catch (SQLException e) {
            System.out.println("cliente erro: " + e.getMessage());
        }

    }



}
