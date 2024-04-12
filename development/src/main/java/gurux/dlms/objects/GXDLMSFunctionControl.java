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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2.
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXArray;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt16;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSFunctionControl
 */
public class GXDLMSFunctionControl extends GXDLMSObject implements IGXDLMSBase {
    /**
     * The current status of each functional block defined in the functions
     * property.
     */
    private ArrayList<Entry<String, Boolean>> activationStatus;
    /**
     * List of modified functions.
     */
    private ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functionList;

    /**
     * Constructor.
     */
    public GXDLMSFunctionControl() {
        this("0.0.44.1.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSFunctionControl(String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSFunctionControl(String ln, int sn) {
        super(ObjectType.FUNCTION_CONTROL, ln, sn);
        activationStatus = new ArrayList<Entry<String, Boolean>>();
        functionList = new ArrayList<Entry<String, ArrayList<GXDLMSObject>>>();
    }

    /**
     * @return The current status of each functional block defined in the
     *         functions property.
     */
    public final ArrayList<Entry<String, Boolean>> getActivationStatus() {
        return activationStatus;
    }

    /**
     * @param value
     *            The current status of each functional block defined in the
     *            functions property.
     */
    public final void setActivationStatus(ArrayList<Entry<String, Boolean>> value) {
        activationStatus = value;
    };

    /**
     * @return List of modified functions.
     */
    public final ArrayList<Entry<String, ArrayList<GXDLMSObject>>> getFunctionList() {
        return functionList;
    }

    /**
     * @param value
     *            List of modified functions.
     */
    public final void setFunctionList(ArrayList<Entry<String, ArrayList<GXDLMSObject>>> value) {
        functionList = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getActivationStatus(), getFunctionList() };
    }

    /**
     * Adjusts the value of the current credit amount attribute.
     * 
     * @param client
     *            DLMS client.
     * @param functions
     *            Enabled or disabled functions.
     * @return Action bytes. * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] setFunctionStatus(GXDLMSClient client, ArrayList<Entry<String, Boolean>> functions)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, functionStatusToByteArray(functions), DataType.ARRAY);
    }

    /**
     * Adjusts the value of the current credit amount attribute.
     * 
     * @param client
     *            DLMS client.
     * @param functions
     *            Added functions.
     * @return Action bytes.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] addFunction(GXDLMSClient client, ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 2, functionListToByteArray(functions), DataType.ARRAY);
    }

    /**
     * Adjusts the value of the current credit amount attribute.
     * 
     * @param client
     *            DLMS client.
     * @param functions
     *            Added functions.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] RemoveFunction(GXDLMSClient client,
            ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 3, functionListToByteArray(functions), DataType.ARRAY);
    }

    /**
     * Convert function states to byte array.
     * 
     * @param functions
     *            Functions.
     * @return
     */
    private static byte[] functionStatusToByteArray(ArrayList<Entry<String, Boolean>> functions) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY);
        GXCommon.setObjectCount(functions.size(), bb);
        for (Entry<String, Boolean> it : functions) {
            bb.setUInt8(DataType.STRUCTURE);
            bb.setUInt8(2);
            bb.setUInt8(DataType.OCTET_STRING);
            GXCommon.setObjectCount(it.getKey().length(), bb);
            bb.set(it.getKey().getBytes());
            bb.setUInt8(DataType.BOOLEAN);
            bb.setUInt8((byte) (it.getValue() ? 1 : 0));
        }
        return bb.array();
    }

    /**
     * Get function states from byte array.
     * 
     * @param values
     *            Byte buffer.
     * @return Function statuses.
     */
    private static ArrayList<Entry<String, Boolean>> functionStatusFromByteArray(ArrayList<?> values) {
        ArrayList<Entry<String, Boolean>> functions = new ArrayList<Entry<String, Boolean>>();
        for (Object tmp : values) {
            GXStructure it = (GXStructure) tmp;
            functions.add(new GXSimpleEntry<String, Boolean>(new String((byte[]) it.get(0)), (boolean) (it.get(1))));
        }
        return functions;
    }

    /**
     * Convert function list to byte array.
     * 
     * @param functions
     *            Functions.
     * @return Action bytes.
     */
    private static byte[] functionListToByteArray(ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY);
        GXCommon.setObjectCount(functions.size(), bb);
        for (Entry<String, ArrayList<GXDLMSObject>> it : functions) {
            bb.setUInt8(DataType.STRUCTURE);
            bb.setUInt8(2);
            bb.setUInt8(DataType.OCTET_STRING);
            GXCommon.setObjectCount(it.getKey().length(), bb);
            bb.set(it.getKey().getBytes());
            bb.setUInt8(DataType.ARRAY);
            GXCommon.setObjectCount(it.getValue().size(), bb);
            for (GXDLMSObject obj : it.getValue()) {
                bb.setUInt8(DataType.STRUCTURE);
                bb.setUInt8(2);
                // Object type.
                bb.setUInt8(DataType.UINT16);
                bb.setUInt16(obj.getObjectType().getValue());
                // LN
                GXCommon.setData(null, bb, DataType.OCTET_STRING, GXCommon.logicalNameToBytes(obj.getLogicalName()));
            }
        }
        return bb.array();
    }

    /**
     * Convert function list to byte array.
     * 
     * @param functions
     *            Functions.
     * @return Action bytes.
     */
    private static ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functionListFromByteArray(ArrayList<?> values) {
        ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions =
                new ArrayList<Entry<String, ArrayList<GXDLMSObject>>>();
        for (Object tmp : values) {
            GXStructure it = (GXStructure) tmp;
            String fn = new String((byte[]) it.get(0));
            ArrayList<GXDLMSObject> objects = new ArrayList<GXDLMSObject>();
            for (Object tmp2 : (GXArray) it.get(1)) {
                GXStructure it2 = (GXStructure) tmp2;
                ObjectType ot = ObjectType.forValue(((GXUInt16) it2.get(0)).intValue());
                byte[] ln = (byte[]) it2.get(1);
                GXDLMSObject obj = GXDLMSClient.createObject(ot);
                obj.setLogicalName(GXCommon.toLogicalName(ln));
                objects.add(obj);
            }
            functions.add(new GXSimpleEntry<String, ArrayList<GXDLMSObject>>(fn, objects));
        }
        return functions;
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            ArrayList<Entry<String, Boolean>> functions = functionStatusFromByteArray((ArrayList<?>) e.getParameters());
            for (Entry<String, Boolean> f : functions) {
                for (Entry<String, Boolean> w : activationStatus) {
                    {
                        if (f.getKey().equals(w.getKey())) {
                            w.setValue(f.getValue());
                            break;
                        }
                    }
                }
            }
        } else if (e.getIndex() == 2) {
            ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions =
                    functionListFromByteArray((ArrayList<?>) e.getParameters());
            functionList.addAll(functions);
        } else if (e.getIndex() == 3) {
            ArrayList<Entry<String, ArrayList<GXDLMSObject>>> functions =
                    functionListFromByteArray((ArrayList<?>) e.getParameters());
            for (Entry<String, ArrayList<GXDLMSObject>> f : functions) {
                for (Entry<String, ArrayList<GXDLMSObject>> w : functionList) {
                    {
                        if (f.getKey().equals(w.getKey())) {
                            functionList.remove(f);
                            break;
                        }
                    }
                }
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        ArrayList<Integer> attributes = new ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // activation_status
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // function_list
        if (all || canRead(3)) {
            attributes.add(3);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "ActivationStatus", "FunctionList" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "SetFunctionStatus", "AddFunction", "RemoveFunction" };
    }

    @Override
    public final int getAttributeCount() {
        return 3;
    }

    @Override
    public final int getMethodCount() {
        return 3;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.ARRAY;
        case 3:
            return DataType.ARRAY;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            return functionStatusToByteArray(getActivationStatus());
        case 3:
            return functionListToByteArray(getFunctionList());
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            setActivationStatus(functionStatusFromByteArray((ArrayList<?>) e.getValue()));
            break;
        case 3:
            setFunctionList(functionListFromByteArray((ArrayList<?>) e.getValue()));
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        if (reader.isStartElement("Activations", true)) {
            getActivationStatus().clear();
            while (reader.isStartElement("Item", true)) {
                String name = reader.readElementContentAsString("Name");
                boolean status = reader.readElementContentAsInt("Status") != 0;
                getActivationStatus().add(new GXSimpleEntry<String, Boolean>(name, status));
            }
            reader.readEndElement("Activations");
        }
        if (reader.isStartElement("Functions", true)) {
            getFunctionList().clear();
            while (reader.isStartElement("Item", true)) {
                String name = reader.readElementContentAsString("Name");
                ArrayList<GXDLMSObject> objects = new ArrayList<GXDLMSObject>();
                getFunctionList().add(new GXSimpleEntry<String, ArrayList<GXDLMSObject>>(name, objects));
                if (reader.isStartElement("Objects", true)) {
                    while (reader.isStartElement("Object", true)) {
                        ObjectType ot = ObjectType.forValue(reader.readElementContentAsInt("ObjectType"));
                        String ln = reader.readElementContentAsString("LN");
                        GXDLMSObject obj = GXDLMSClient.createObject(ot);
                        obj.setLogicalName(ln);
                        objects.add(obj);
                    }
                    reader.readEndElement("Objects");
                }
            }
            reader.readEndElement("Functions");
        }
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("Activations");
        for (Entry<String, Boolean> it : getActivationStatus()) {
            writer.writeStartElement("Item");
            writer.writeElementString("Name", it.getKey());
            writer.writeElementString("Status", it.getValue());
            writer.writeEndElement();
        }
        writer.writeEndElement(); // Activations
        writer.writeStartElement("Functions");
        for (Entry<String, ArrayList<GXDLMSObject>> it : getFunctionList()) {
            writer.writeStartElement("Item");
            writer.writeElementString("Name", it.getKey());
            writer.writeStartElement("Objects");
            for (GXDLMSObject obj : it.getValue()) {
                writer.writeStartElement("Object");
                writer.writeElementString("ObjectType", obj.getObjectType().getValue());
                writer.writeElementString("LN", obj.getLogicalName());
                writer.writeEndElement(); // Object
            }
            writer.writeEndElement(); // Objects
            writer.writeEndElement(); // Item
        }
        writer.writeEndElement(); // Functions
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
