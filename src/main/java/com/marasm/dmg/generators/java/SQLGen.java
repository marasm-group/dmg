package com.marasm.dmg.generators.java;

import com.marasm.dmg.DMGObject;
import com.marasm.dmg.Field;
import com.marasm.dmg.Log;

/**
 * Created by vhq473 on 29.08.2016.
 */
public class SQLGen
{
    public String ID = "_id";
    JavaGenerator gen = null;

    public static String header = "import java.sql.*;//";

    SQLGen(JavaGenerator gen)
    {
        this.gen = gen;
    }

    void generate(DMGObject o)
    {

        gen.append("public static void initDB(String driver, String url, String user, String password)\n{");
        gen.append("Class.forName(driver);");
        gen.append("Connection conn = null;\n" +
                   "Statement stmt = null;");
        gen.append("conn = DriverManager.getConnection(url, user, password);");
        gen.append("stmt = conn.createStatement();");
        gen.append("String sql = "+sqlCreateTableString(o)+";");
        gen.append("stmt.executeUpdate(sql);");
        gen.append("}//initDB");
        gen.append("public void toSQL()\n{");
        for (Field f : o.fields)
        {
            //generateField(f);
        }
        gen.append("}");
    }

    private String sqlCreateTableString(DMGObject o)
    {
        String res = "CREATE TABLE "+capitalize(o.name)+" " + "("+ID+" INTEGER not NULL";

        for (Field f:o.fields)
        {
            res += ", "+f.name+" "+type(f,f.isArray);
        }
        res += " PRIMARY KEY ( "+ID+" )";
        for (Field f:o.fields)
        {
            if(f.object != null)
            {
                res+=", FOREIGN KEY ("+f.object.name+"_id"+") REFERENCES "+capitalize(f.object.name)+"("+ID+")";
            }
        }
        res+=")";
        return res;
    }

    public String type(Field f, boolean array)
    {
        if(array)
        {
            Log.e(this,"Arrays not supported");
            System.exit(-1);
        }
        switch (f.type)
        {
            case Object:
                return f.object.name+"_id INTEGER ";
            case String:
                return "VARCHAR(MAX) ";
            case Int:
                return "INTEGER ";
            case Real:
                return "REAL ";
            case Number:
                return "REAL ";
            case Bool:
                return "BIT ";
            default:
                return "ERROR_TYPE";
        }
    }

    String capitalize(String o)
    {
        return o.toUpperCase();
    }
}
