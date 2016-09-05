package com.marasm.dmg.generators.swift;

import com.marasm.dmg.DMGObject;
import com.marasm.dmg.Field;

/**
 * Created by vhq473 on 29.08.2016.
 */
public class CouchDBGenerator
{
    SwiftGenerator g;
    public String header = "import CouchbaseLite\n";

    String tmp_obj = "TMP_OBJ";
    String tmp_prop_obj = "TMP_PROP_OBJ";
    String tmp_arr = "TMP_ARR";
    String tmp_map = "TMP_MAP";
    CouchDBGenerator(SwiftGenerator g)
    {
        this.g = g;
    }

    void generateObject(DMGObject o)
    {
        g.append("public func saveToDB() throws -> String?{");
        g.append("return try self.saveToDB(\""+ dbName(o.name)+"\")");
        g.append("}");
        g.append("public func saveToDB(db: String) throws -> String?{");
        g.append("let database = try CBLManager.sharedInstance().databaseNamed(db) \n"+
                 "let document = database.createDocument() \n" +
                 "return try self.saveToCBLDocument(document)\n"+
                 "}");
        g.append("public func saveToCBLDocument(document: CBLDocument) throws -> String?{\n"+
                 "try document.putProperties(self.jsonDictionary)\n"+
                 "return document.documentID\n"+
                 "}");
        g.append("var defaultDB : CBLDatabase?{");
        g.append("let database = try? CBLManager.sharedInstance().databaseNamed(\""+dbName(o.name)+"\")");
        g.append("return database");
        g.append("}");
        g.append("public convenience init?(documentID: String) throws {");
        g.append("try self.init(dbName:\"" + dbName(o.name) + "\", documentID: documentID)");
        g.append("}");
        g.append("public convenience init?(dbName: String, documentID: String) throws {");
        g.append("let database = try CBLManager.sharedInstance().databaseNamed(dbName) \n"+
                 "guard let document = database.documentWithID(documentID) \n" +
                 "else{\n"+
                 ""+g.capitalizeFirst(o.name)+".Log(\"ERROR: documentWithID(\\(documentID)) is nil\")\n"+
                 "return nil\n}\n"+
                 "self.init(cblDocument: document)\n");
        g.append("}");
        g.append("public convenience init?(cblDocument: CBLDocument){\n"+
                 "guard let props = cblDocument.properties\n" +
                 "else {\n" +
                 ""+g.capitalizeFirst(o.name)+".Log(\"ERROR: cblDocument.properties is nil\")\n"+
                 "return nil\n" +
                 "}\n" +
                 "self.init(jsonDictionary: props)\n");
        g.append("}");
    }

    String dbName(String s)
    {
        return s.toLowerCase();
    }

}