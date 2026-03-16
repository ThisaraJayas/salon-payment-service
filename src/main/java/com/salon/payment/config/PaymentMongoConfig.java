package com.salon.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class PaymentMongoConfig {


    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(
                "mongodb+srv://mayanioinfo_db_user:Sms25BMt6n8qcj4d@cluster0.nhbeekm.mongodb.net/payment_service?retryWrites=true&w=majority&appName=Cluster0"
        );
    }
}