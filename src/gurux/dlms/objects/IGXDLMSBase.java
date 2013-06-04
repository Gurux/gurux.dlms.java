/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

import gurux.dlms.enums.DataType;

/**
 *
 * @author Gurux Ltd
 */
public interface IGXDLMSBase 
{
    /*
    * Returns amount of attributes.
    */
    int getAttributeCount();
        
    
    /*
     * Returns amount of methods.
     */    
    int getMethodCount();
            
    /*
    * Returns value of given attribute.
    */
    Object getValue(int index, DataType[] type, byte[] parameters);

   /*
    * Set value of given attribute.
    */
   void setValue(int index, Object value);
   
   /*
    * Invokes method.
    * 
     @param index Method index.
    */
   void invoke(int index, Object parameters);
}
