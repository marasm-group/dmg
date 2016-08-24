package com.marasm.dmg;

/**
 * Created by Sergey Rump on 19.08.2016.
 */
public enum Type
{
    Int,   // A Integer value
    Real,  // A double value
    Number,// A numeric value (used if auto recognition is disabled)
    String,// A String value
    Object,// A complex object value
    Bool,  // A boolean value

    ERROR_TYPE //something went wrong
    ;

    public static Type fromString(String value)
    {
        if (value == null)
        {
            Log.e(Type.ERROR_TYPE,"node.getNodeValue() == null");
            return Type.ERROR_TYPE;
        }
        if (Utils.isNumeric(value))
        {
            if(Utils.isInteger(value))
            {
                return Utils.numericType(Type.Int);
            }
            else
            {
                return Utils.numericType(Type.Real);
            }
        }
        if(Utils.isBoolean(value))
        {
            return Type.Bool;
        }
        return Type.String;
    }
}