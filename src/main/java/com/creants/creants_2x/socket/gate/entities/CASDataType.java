// 
// Decompiled by Procyon v0.5.30
// 

package com.creants.creants_2x.socket.gate.entities;

/**
 * @author Lamhm
 *
 */
public enum CASDataType
{
    NULL("NULL", 0, 0), 
    BOOL("BOOL", 1, 1), 
    BYTE("BYTE", 2, 2), 
    SHORT("SHORT", 3, 3), 
    INT("INT", 4, 4), 
    LONG("LONG", 5, 5), 
    FLOAT("FLOAT", 6, 6), 
    DOUBLE("DOUBLE", 7, 7), 
    UTF_STRING("UTF_STRING", 8, 8), 
    BOOL_ARRAY("BOOL_ARRAY", 9, 9), 
    BYTE_ARRAY("BYTE_ARRAY", 10, 10), 
    SHORT_ARRAY("SHORT_ARRAY", 11, 11), 
    INT_ARRAY("INT_ARRAY", 12, 12), 
    LONG_ARRAY("LONG_ARRAY", 13, 13), 
    FLOAT_ARRAY("FLOAT_ARRAY", 14, 14), 
    DOUBLE_ARRAY("DOUBLE_ARRAY", 15, 15), 
    UTF_STRING_ARRAY("UTF_STRING_ARRAY", 16, 16), 
    CAS_ARRAY("CAS_ARRAY", 17, 17), 
    CAS_OBJECT("CAS_OBJECT", 18, 18), 
    TEXT("TEXT", 20, 20);
    
    private int typeID;
    
    private CASDataType(final String s, final int n, final int typeID) {
        this.typeID = typeID;
    }
    
    public static CASDataType fromTypeId(final int typeId) {
        CASDataType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final CASDataType item = values[i];
            if (item.getTypeID() == typeId) {
                return item;
            }
        }
        
        throw new IllegalArgumentException("Unknown typeId for CASDataType");
    }
    
    public int getTypeID() {
        return this.typeID;
    }
}
