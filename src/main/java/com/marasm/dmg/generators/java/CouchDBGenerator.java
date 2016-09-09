package com.marasm.dmg.generators.java;

import com.marasm.dmg.DMGObject;
import com.marasm.dmg.Field;

/**
 * Created by vhq473 on 29.08.2016.
 */
public class CouchDBGenerator
{
    JavaGenerator g;
    public String header = "import com.couchbase.lite.*;\n" +
                           "import java.util.HashMap;\n" +
                           "import java.util.Map;";

    String tmp_obj = "TMP_OBJ";
    String tmp_prop_obj = "TMP_PROP_OBJ";
    String tmp_arr = "TMP_ARR";
    String tmp_map = "TMP_MAP";
    CouchDBGenerator(JavaGenerator  g)
    {
        this.g = g;
    }

    void generateObject(DMGObject o)
    {
        g.append("public String saveToDB(){");
        g.append("return this.saveToDB(\""+ dbName(o.name)+"\");");
        g.append("}");
        g.append("public String saveToDB(String db){");
        g.append("JavaContext context = new JavaContext();\n"+
                "Manager manager = null;\n"+
                "try {\n"+
                "manager = new Manager(context, Manager.DEFAULT_OPTIONS);\n"+
                "} catch (IOException e) {\n"+
                "e.printStackTrace();\n"+
                "return null;\n"+
                "}\n"+
                "Database database = null;\n"+
                "try {\n"+
                "database = manager.getDatabase(db);\n"+
                "} catch (Exception e) {\n"+
                "e.printStackTrace();\n"+
                "return null;\n"+
                "}");
        g.append("Map<String, Object> properties = this.properties();\n");
        g.append("// Create a new document\n"+
                "Document document = database.createDocument();\n"+
                "// Save the document to the database\n"+
                "try {\n"+
                "document.putProperties(properties);\n"+
                "} catch (CouchbaseLiteException e) {\n"+
                "e.printStackTrace();\n" +
                "}");
        g.append("return document.getId();");
        g.append("}");
        g.append("public HashMap<String,Object> properties(){");
        g.append("HashMap<String,Object> res = new HashMap<>();");
        for (Field f :o.fields)
        {
            if(f.isArray)
            {
                g.append("if(true){");
                if(f.object != null)
                {
                    g.append("ArrayList<Map<String, Object>> "+tmp_arr+" = new ArrayList<>();");
                    g.append("for("+g.type(f,true)+" "+tmp_obj+" : this."+f.name+"){");
                    g.append(tmp_arr+".add("+tmp_obj+".properties());");
                    g.append("}");
                    g.append("res.put(\""+f.name+"\","+tmp_arr+");");
                }
                else
                {
                    g.append("ArrayList<"+g.type(f,true)+"> "+tmp_arr+" = new ArrayList<>();");
                    g.append("for("+g.type(f,true)+" "+tmp_obj+" : this."+f.name+"){");
                    g.append(tmp_arr+".add("+tmp_obj+");");
                    g.append("}");
                    g.append("res.put(\""+f.name+"\","+tmp_arr+");");
                }
                g.append("}");
            }
            else
            {
                if(f.object != null)
                {
                    g.append("res.put(\""+f.name+"\","+f.name+".properties());");
                }
                else
                {
                    g.append("res.put(\""+f.name+"\","+f.name+");");
                }
            }
        }
        g.append("return res;");
        g.append("}");
        g.append("void fillWithProperties(Map<String,Object> properties)\n{");
        g.append("Object "+tmp_prop_obj+";");
        for (Field f :o.fields)
        {
            g.append(tmp_prop_obj+" = properties.get(\""+f.name+"\");");
            if(f.isArray)
            {
                g.append("if(true){");
                if(f.object != null)
                {
                    g.append("ArrayList<Map<String, Object>> "+tmp_arr+" = (ArrayList<Map<String,Object>>)"+tmp_prop_obj+";");
                    g.append("this."+f.name+" = new "+g.arrayType+"<>();");
                    g.append("for(Map<String, Object> "+tmp_map+" : "+tmp_arr+"){");
                    g.append(g.type(f,true)+" "+tmp_obj+" = new "+g.type(f,true)+"();");
                    g.append(tmp_obj+".fillWithProperties("+tmp_map+");");
                    g.append("this."+f.name+".add("+tmp_obj+");");
                    g.append("}");
                }
                else
                {
                    g.append("this."+f.name+" = new "+g.arrayType+"<>();");
                    g.append("ArrayList<Object> "+tmp_arr+" = (ArrayList<Object>)"+tmp_prop_obj+";");
                    g.append("for(Object "+tmp_map+" : "+tmp_arr+"){");
                    g.append("this."+f.name+".add(("+g.type(f,true)+")"+tmp_map+");");
                    g.append("}");
                }
                g.append("}");
            }
            else
            {
                if(f.object != null)
                {
                    g.append("this."+f.name+" = new "+g.type(f,false)+"();");
                    g.append("this."+f.name+".fillWithProperties((Map<String, Object>)"+tmp_prop_obj+");");
                }
                else
                {
                    g.append("this."+f.name+" = ("+g.type(f,false)+")"+tmp_prop_obj+";");
                }
            }
        }
        g.append("}");
        g.append("Database defaultDB()\n{");
        g.append("JavaContext context = new JavaContext();\n"+
                 "Manager manager = null;\n"+
                 "try {\n"+
                 "manager = new Manager(context, Manager.DEFAULT_OPTIONS);\n"+
                 "} catch (IOException e) {\n"+
                 "e.printStackTrace();\n"+
                 "return null;\n"+
                 "}\n"+
                 "Database database = null;\n"+
                 "try {\n"+
                 "database = manager.getDatabase(\""+ dbName(o.name)+"\");\n"+
                 "} catch (Exception e) {\n"+
                 "e.printStackTrace();\n"+
                 "return null;\n"+
                 "}");
        g.append("return database;");
        g.append("}");
        g.append("void fillFromCouchDB(String id){");
        g.append("this.fillFromCouchDB(\"" + dbName(o.name) + "\",id);");
        g.append("}");
        g.append("void fillFromCouchDB(String db, String id){");
        g.append("JavaContext context = new JavaContext();\n"+
                 "Manager manager = null;\n"+
                 "try {\n"+
                 "manager = new Manager(context, Manager.DEFAULT_OPTIONS);\n"+
                 "} catch (IOException e) {\n"+
                 "e.printStackTrace();\n"+
                 "return;\n"+
                 "}\n"+
                 "Database database = null;\n"+
                 "try {\n"+
                 "database = manager.getDatabase(db);\n"+
                 "} catch (Exception e) {\n"+
                 "e.printStackTrace();\n"+
                 "return;\n"+
                 "}");
        g.append("// Get a document\n"+
                 "Document document = database.getDocument(id);\n"+
                 "// Save the document to the database\n"+
                 "try {\n"+
                 "this.fillWithProperties(document.getProperties());\n"+
                 "} catch (Exception e) {\n"+
                 "e.printStackTrace();\n"+
                 "}");
        g.append("}");
    }

    String dbName(String s)
    {
        return s.toLowerCase();
    }

}
