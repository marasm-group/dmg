package com.marasm.dmg.generators.java;

import com.marasm.dmg.DMGObject;
import com.marasm.dmg.Field;

/**
 * Created by vhq473 on 29.08.2016.
 */
public class CouchDBGenerator
{
    JavaGenerator g;
    String tmp_obj = "TMP_OBJ";
    String tmp_arr = "TMP_ARR";
    String tmp_map = "TMP_MAP";
    CouchDBGenerator(JavaGenerator  g)
    {
        this.g = g;
    }

    void generateObject(DMGObject o)
    {
        g.append("public void loadFromDB(){");
        g.append("loadFromDB(\""+o.name+"\");");
        g.append("}");
        g.append("public void loadFromDB(String db){");
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
                 "database = manager.getDatabase(\"app\");\n"+
                 "} catch (Exception e) {\n"+
                 "e.printStackTrace();\n"+
                 "return\n;"+
                 "}");
        //TODO
        g.append("}");

        g.append("public void saveToDB(){");
        g.append("loadFromDB(\""+o.name+"\");");
        g.append("}");
        g.append("public void saveToDB(String db){");
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
                "database = manager.getDatabase(\"app\");\n"+
                "} catch (Exception e) {\n"+
                "e.printStackTrace();\n"+
                "return\n;"+
                "}");
        g.append("Map<String, Object> properties = this.properties;\n");
        ;
        g.append("// Create a new document\n"+
                "Document document = database.createDocument();\n"+
                "// Save the document to the database\n"+
                "try {\n"+
                "document.putProperties(properties);\n"+
                "} catch (CouchbaseLiteException e) {\n"+
                "e.printStackTrace();\n"+
                "}");
        g.append("}");

        g.append("public HashMap<String,Object> properties(){");
        g.append("HashMap<String,Object> res = new HashMap<>();");
        for (Field f :o.fields)
        {
            if(f.isArray)
            {
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
                    g.append("ArrayList<Map<String, Object>> "+tmp_arr+" = new ArrayList<>();");
                    g.append("for("+g.type(f,true)+" "+tmp_obj+" : this."+f.name+"){");
                    g.append(tmp_arr+".add("+tmp_obj+");");
                    g.append("}");
                    g.append("res.put(\""+f.name+"\","+tmp_arr+");");
                }
            }
            else
            {
                if(f.object != null)
                {
                    g.append("res.put(\""+f.name+"\","+f.name+".properties())");
                }
                else
                {
                    g.append("res.put(\""+f.name+"\","+f.name+")");
                }
            }
        }
        g.append("}");
    }

}
