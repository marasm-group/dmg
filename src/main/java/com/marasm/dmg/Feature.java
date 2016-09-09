package com.marasm.dmg;

/**
 * Created by vhq473 on 24.08.2016.
 */
public enum Feature
{
    from_json, // json string -> object conversion
    to_json,   // object -> json string conversion
    couchdb,   // Couchbase lite
    sql,       // SQL database read/write

    ERROR_FEATURE;

    public static Feature fromString(String str)
    {
        switch (str)
        {
            case "from_json":
                return from_json;
            case "to_json":
                return to_json;
            case "couchdb":
                return couchdb;
            case "sql":
                return sql;
            default:
                return ERROR_FEATURE;
        }
    }

    public String description()
    {
        switch (this)
        {
            case from_json:
                return "\"from_json\" -- add json deserialization";
            case to_json:
                return "\"to_json\" -- add json serialization";
            case couchdb:
                return "\"couchdb\" -- couchbase lite support";
            case sql:
                return "\"sql\" -- add sql create/save/load methods";
            default:
                    return "";
        }
    }
}
