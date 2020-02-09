package com.udacity.course3.lesson4.exercise4;


import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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

/**        // Perform all the steps listed in the exercise
        database.createCollection("members"); */

        MongoCollection<Document> members = database.getCollection("members");

        Document member = new Document()
                .append("first_name", "Carl")
                .append("last_name", "Jenkins")
                .append("gender", "male")
                .append("age", 23)
                .append("address", new Document("street", "123 Main Street")
                                        .append("city", "Oakland")
                                        .append("state", "CA"))
                .append("interests", Arrays.asList("pilates","swim","crossfit"))
                .append("balance", 125.20);


        members.insertOne(member);

        ObjectId _id = member.getObjectId("_id");

        System.out.println("First Member: " + member.toJson());

        Document[] docArray = new Document[3];

        docArray[0] = new Document()
                .append("first_name", "Juanjo")
                .append("last_name", "Guirao")
                .append("gender", "male")
                .append("age", 53)
                .append("address", new Document("street", "324 Winterthur Way")
                        .append("city", "Basingstoke")
                        .append("state", "Hants"))
                .append("interests", Arrays.asList("Java","Alma","David"))
                .append("balance", 1000.00);

        docArray[1] = new Document()
                .append("first_name", "Alma")
                .append("last_name", "Guirao Gil")
                .append("gender", "female")
                .append("age", 1)
                .append("address", new Document("street", "324 Winterthur Way")
                        .append("city", "Basingstoke")
                        .append("state", "Hants"))
                .append("interests", Arrays.asList("Macapaca","Uva","Peppa Pig"))
                .append("balance", 100000.00);

        docArray[2] = new Document()
                .append("_id", "25126789")
                .append("first_name", "Maru")
                .append("last_name", "Gil")
                .append("gender", "female")
                .append("age", 42)
                .append("address", new Document("street", "324 Winterthur Way")
                        .append("city", "Basingstoke")
                        .append("state", "Hants"))
                .append("interests", Arrays.asList("Alma","teaching","talking"))
                .append("balance", 1000.00);

        members.insertMany(Arrays.asList(docArray));

        List<String> items = new ArrayList<>();
        MongoCursor<Document> cursor = members.find().iterator();
        try {
            while (cursor.hasNext()) {
                items.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }



        System.out.println(items);


/**        members.replaceOne(new Document("_id", _id), new Document()
                .append("first_name", "Carl")
                .append("last_name", "Jenkins")
                .append("gender", "male")
                .append("age", 23)
                .append("address", new Document("street", "123 Main Street")
                        .append("city", "Oakland")
                        .append("state", "CA"))
                .append("interests", Arrays.asList("pilates","swim","crossfit"))
                .append("balance", 125.20)); */

        members.updateOne(new Document("_id", _id), new Document()
                .append("$rename", new Document("gender","sex")));

/**         members.replaceOne(new Document("fist_name", "Eduardo"), new Document("first_name", "Eduardo")
                .append("last_name", "Lopez").append("age", 23).append("sex","male"),
                new ReplaceOptions().upsert(true)); */


        Bson filter = new Document();
        Bson updateOperationDocument = new Document("$rename", new Document("gender","sex"));
        members.updateMany(filter, updateOperationDocument);

        Document docToDelete = members.find(eq("first_name", "Carl")).first();

        ObjectId myID = docToDelete.getObjectId("_id");
        System.out.println("docToDelete: " + docToDelete.toJson());
        System.out.println("myID: " + myID);
        members.deleteOne(new Document("_id", myID));

        Document maleToDelete = members.find(eq("sex", "male")).first();
        members.deleteOne(new Document("sex", "male"));
        // members.insertOne(maleToDelete);  // This re-inserts the deleted male

        mongoClient.close();
    }
}


/** Classroom code:
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

 */