//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

public interface IGXDLMSBase 
{
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    int[] getAttributeIndexToRead();
        
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
    Object getValue(int index, int selector, Object parameters);

   /*
    * Set value of given attribute.
    */
   void setValue(int index, Object value);
   
   /*
    * Invokes method.
    * 
     @param index Method index.
    */
   byte[][] invoke(Object sender, int index, Object parameters);
}
