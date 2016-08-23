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

}