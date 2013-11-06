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

import gurux.dlms.GXDLMSClient;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ImageTransferStatus;
import gurux.dlms.enums.ImageTransferredBlocksStatus;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GXDLMSImageTransfer extends GXDLMSObject implements IGXDLMSBase
{
    long ImageBlockSize;
    ImageTransferredBlocksStatus m_ImageTransferredBlocksStatus;
    long ImageFirstNotTransferredBlockNumber;
    boolean ImageTransferEnabled;
    ImageTransferStatus m_ImageTransferStatus;
    List<GXDLMSImageActivateInfo> ImageActivateInfo = new ArrayList<GXDLMSImageActivateInfo>();

    /**  
     Constructor.
     @param ln Logican Name of the object.
    */
    public GXDLMSImageTransfer()
    {
        super(ObjectType.IMAGE_TRANSFER);        
    }
    
    /**  
     Constructor.
     @param ln Logican Name of the object.
    */
    public GXDLMSImageTransfer(String ln)
    {
        super(ObjectType.IMAGE_TRANSFER, ln, 0);        
    }

    /**  
     Constructor.
     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSImageTransfer(String ln, int sn)
    {
        super(ObjectType.IMAGE_TRANSFER, ln, sn);        
    }       

    /** 
     Holds the ImageBlockSize, expressed in octets, 
     * which can be handled by the server
    */
    public final long getImageBlockSize()
    {
        return ImageBlockSize;
    }
    public final void setImageBlockSize(long value)
    {
        ImageBlockSize = value;
    }

    /** 
     * Provides information about the transfer status of each
     * ImageBlock. Each bit in the bit-string provides information about
     * one individual ImageBlock.
    */
    public final ImageTransferredBlocksStatus getImageTransferredBlocksStatus()
    {
        return m_ImageTransferredBlocksStatus;
    }
    public final void setImageTransferredBlocksStatus(ImageTransferredBlocksStatus value)
    {
    	m_ImageTransferredBlocksStatus = value;
    }

    /** 
     Provides the ImageBlockNumber of the first ImageBlock not transferred.
     * NOTE If the Image is complete, the value returned should be above the
     * number of blocks calculated from the Image size and the ImageBlockSize
    */
    public final long getImageFirstNotTransferredBlockNumber()
    {
        return ImageFirstNotTransferredBlockNumber;
    }
    public final void setImageFirstNotTransferredBlockNumber(long value)
    {
        ImageFirstNotTransferredBlockNumber = value;
    }

    /**      
     * Controls enabling the Image transfer process. The method can
     * be invoked successfully only if the value of this attribute is true.
     */
    public final boolean getImageTransferEnabled()
    {
        return ImageTransferEnabled;
    }
    public final void setImageTransferEnabled(boolean value)
    {
        ImageTransferEnabled = value;
    }

    /**
     * Holds the status of the Image transfer process.     
     */
    public final ImageTransferStatus getImageTransferStatus()
    {
        return m_ImageTransferStatus;
    }
    public final void setImageTransferStatus(ImageTransferStatus value)
    {
    	m_ImageTransferStatus = value;
    }

    public final List<GXDLMSImageActivateInfo> getImageActivateInfo()
    {
        return ImageActivateInfo;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getImageBlockSize(), 
            getImageTransferredBlocksStatus(), 
            getImageFirstNotTransferredBlockNumber(), 
            getImageTransferEnabled(), getImageTransferStatus(), 
            getImageActivateInfo().toArray()};
    }    
    
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] GetAttributeIndexToRead()
    {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        //LN is static and read only once.
        if (LogicalName == null || LogicalName.compareTo("") == 0)
        {
            attributes.add(1);
        }   
        //Mikko
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 7;
    }
    
     /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 4;
    }     
    
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, DataType[] type, byte[] parameters, boolean raw)
    {
        if (index == 1)
        {
            type[0] = DataType.OCTET_STRING;
            return getLogicalName();
        }
        if (index == 2)
        {   
            return getImageBlockSize();
        }
        if (index == 3)
        {
            return getImageTransferredBlocksStatus().ordinal();
        }
        if (index == 4)
        {
            return getImageFirstNotTransferredBlockNumber();
        }
        if (index == 5)
        {
            return getImageTransferEnabled();

        }
        if (index == 6)
        {
            return getImageTransferStatus().ordinal();            
        }
        if (index == 7)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());
            data.write((byte) ImageActivateInfo.size()); //Count  
            try
            {
                for(GXDLMSImageActivateInfo it : ImageActivateInfo)
                {
                    data.write((byte)DataType.STRUCTURE.getValue());                    
                    data.write((byte)3);//Item count.
                    GXCommon.setData(data, DataType.UINT32, it.getSize());
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getIdentification().getBytes("ASCII"));
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getSignature().getBytes("ASCII"));
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }       
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
        
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value, boolean raw)
    {
        if (index == 1)
        {
            setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
        }        
        else if (index == 2)
        {
            if (value == null)
            {
                setImageBlockSize(0);
            }
            else
            {
                setImageBlockSize(((Number) value).intValue());
            }
        }
        else if (index == 3)
        {
            if (value == null)
            {
                setImageTransferredBlocksStatus(ImageTransferredBlocksStatus.NOT_TRANSFERRED);
            }
            else
            {
                setImageTransferredBlocksStatus(ImageTransferredBlocksStatus.values()[((Number) value).intValue()]);
            }            
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setImageFirstNotTransferredBlockNumber(0);
            }
            else
            {
                setImageFirstNotTransferredBlockNumber(((Number) value).intValue());
            }            
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setImageTransferEnabled(false);
            }
            else
            {
                setImageTransferEnabled((Boolean) value);
            }                  
        }
        else if (index == 6)
        {
            if (value == null)
            {
                setImageTransferStatus(ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED);
            }
            else
            {
                setImageTransferStatus(ImageTransferStatus.values()[((Number) value).intValue()]);
            }              
        }
        else if (index == 7)
        {
            ImageActivateInfo.clear();
            if (value != null)
            {        
                for(Object it : (Object[]) value)
                {                    
                    GXDLMSImageActivateInfo item = new GXDLMSImageActivateInfo();
                    item.setSize(((Number)Array.get(it, 0)).longValue());
                    item.setIdentification(GXDLMSClient.changeType((byte[]) Array.get(it, 1), DataType.STRING).toString());
                    item.setSignature(GXDLMSClient.changeType((byte[]) Array.get(it, 2), DataType.STRING).toString());
                    ImageActivateInfo.add(item);
                }
            }
        }         
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
    
     /**
     * Initializes the Image transfer process.
     */
    public void initiate()
    {

    }

    /*
     * Transfers one block of the Image to the server.
     */
    public void transfer()
    {

    }

    /*
     * Verifies the integrity of the Image before activation.
     */
    public void verify()
    {

    }

    /*
     * Activates the Image(s).
     */
    public void activate()
    {

    }    
}
