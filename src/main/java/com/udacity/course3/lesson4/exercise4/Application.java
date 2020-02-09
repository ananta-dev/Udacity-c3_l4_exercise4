package com.udacity.course3.lesson4.exercise4;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Application {

    public static void main(String[] args) {
        // STEP 1: Craft the URI to connect to your local MongoDB server

        // Host: localhost
        // Port: 27017 (default)
        // Username: course3
        // Password: course3
        // DB: jdnd-c3

        // String uri = "mongodb://course3:course3@localhost:27017/jdnd-c3";
        String uri = "mongodb://localhost:27017/jdnd-c3";

        // STEP 2: Create a MongoClient
        MongoClient mongoClient = MongoClients.create(uri);

        // STEP 3: Select the jdnd-c3 database to work with
        MongoDatabase database = mongoClient.getDatabase("jdnd-c3");

        // Perform all the steps listed in the exercise
        database.createCollection("patients");

        MongoCollection<Document> patients = database.getCollection("patients");

        Document patient = new Document()
                .append("first_name", "Sana")
                .append("last_name", "Khan")
                .append("age", 45);

        patients.insertOne(patient);

        ObjectId _id = patient.getObjectId("_id");

        patients.replaceOne(new Document("_id", _id), new Document()
                .append("first_name", "Sana")
                .append("last_name", "Khan")
                .append("age", 45)
                .append("gender", "female"));

        patients.updateOne(new Document("_id", _id), new Document()
                .append("$rename", new Document("gender","sex")));


        patients.replaceOne(new Document("fist_name", "Eduardo"), new Document("first_name", "Eduardo")
                .append("last_name", "Lopez").append("age", 23).append("gender","male"),
                new ReplaceOptions().upsert(true));

        mongoClient.close();
    }
}