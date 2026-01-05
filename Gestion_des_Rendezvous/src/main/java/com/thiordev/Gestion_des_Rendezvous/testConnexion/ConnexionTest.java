package com.thiordev.Gestion_des_Rendezvous.testConnexion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

//@Component
public class ConnexionTest implements CommandLineRunner{

    private final DataSource datasource;

    ConnexionTest(DataSource datasource){
        this.datasource = datasource;
    }

    @Override
    public void run(String... args) throws Exception {
        try(Connection connection = datasource.getConnection()){
            System.out.println("Connexion established");
            System.out.println("url : " + connection.getMetaData().getURL());
            System.out.println("username : " + connection.getMetaData().getUserName());
        }
    }
}
