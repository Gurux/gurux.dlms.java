package gurux.dlms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import gurux.dlms.enums.DataType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;

public class GXDLMSConverter {
    /**
     * Collection of standard OBIS codes.
     */
    private GXStandardObisCodeCollection codes =
            new GXStandardObisCodeCollection();

    /**
     * Update OBIS code information.
     * 
     * @param it
     *            COSEM object.
     */
    private static void updateOBISCodeInfo(
            final GXStandardObisCodeCollection codes, final GXDLMSObject it) {
        if (!(it.getDescription() == null || it.getDescription().equals(""))) {
            return;
        }
        String ln = it.getLogicalName();
        GXStandardObisCode code = codes.find(ln, it.getObjectType());
        if (code != null) {
            it.setDescription(code.getDescription());
            // If string is used
            if (code.getDataType().contains("10")) {
                code.setDataType("10");
            } else if (code.getDataType().contains("25")
                    || code.getDataType().contains("26")) {
                // If date time is used.
                code.setDataType("25");
            } else if (code.getDataType().contains("9")) {
                // Time stamps of the billing periods objects (first
                // scheme
                // if there are two)
                if ((GXStandardObisCodeCollection
                        .equalsMask("0.0-64.96.7.10-14.255", ln)
                        // Time stamps of the billing periods objects
                        // (second scheme)
                        || GXStandardObisCodeCollection
                                .equalsMask("0.0-64.0.1.5.0-99,255", ln)
                        // Time of power failure
                        || GXStandardObisCodeCollection
                                .equalsMask("0.0-64.0.1.2.0-99,255", ln)
                        // Time stamps of the billing periods
                        // objects (first
                        // scheme if there are two)
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.1.2.0-99,255", ln)
                        // Time stamps of the billing periods
                        // objects
                        // (second scheme)
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.1.5.0-99,255", ln)
                        // Time expired since last end of
                        // billing
                        // period
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.0.255", ln)
                        // Time of last reset
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.6.255", ln)
                        // Date of last reset
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.7.255", ln)
                        // Time expired since last end of
                        // billing
                        // period
                        // (Second billing period scheme)
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.13.255", ln)
                        // Time of last reset (Second billing
                        // period
                        // scheme)
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.14.255", ln)
                        // Date of last reset (Second billing
                        // period
                        // scheme)
                        || GXStandardObisCodeCollection
                                .equalsMask("1.0-64.0.9.15.255", ln))) {
                    code.setDataType("25");
                } else if (GXStandardObisCodeCollection
                        .equalsMask("1.0-64.0.9.1.255", ln)) {
                    // Local time
                    code.setDataType("27");
                } else if (GXStandardObisCodeCollection
                        .equalsMask("1.0-64.0.9.2.255", ln)) {
                    // Local date
                    code.setDataType("26");
                }
            }
            if (!code.getDataType().equals("*")
                    && !code.getDataType().equals("")
                    && !code.getDataType().contains(",")) {
                DataType type =
                        DataType.forValue(Integer.parseInt(code.getDataType()));
                switch (it.getObjectType()) {
                case DATA:
                case REGISTER:
                case REGISTER_ACTIVATION:
                case EXTENDED_REGISTER:
                    it.setUIDataType(2, type);
                    break;
                default:
                    break;
                }
            }
        } else {
            System.out.println("Unknown OBIS Code: " + it.getLogicalName()
                    + " Type: " + it.getObjectType());
        }
    }

    /**
     * Update standard OBIS codes description and type if defined.
     * 
     * @param object
     *            COSEM object.
     */
    public final void updateOBISCodeInformation(final GXDLMSObject object) {
        synchronized (codes) {
            if (codes.size() == 0) {
                readStandardObisInfo(codes);
            }
            updateOBISCodeInfo(codes, object);
        }
    }

    /**
     * Update standard OBIS codes descriptions and type if defined.
     * 
     * @param objects
     *            Collection of COSEM objects to update.
     */
    public final void
            updateOBISCodeInformation(final GXDLMSObjectCollection objects) {
        synchronized (codes) {
            if (codes.size() == 0) {
                readStandardObisInfo(codes);
            }
            for (GXDLMSObject it : objects) {
                updateOBISCodeInfo(codes, it);
            }
        }
    }

    /**
     * Read standard OBIS code information from the file.
     * 
     * @param codes
     *            Collection of standard OBIS codes.
     */
    private static void
            readStandardObisInfo(final GXStandardObisCodeCollection codes) {
        InputStream stream =
                GXDLMSClient.class.getResourceAsStream("/OBISCodes.txt");
        if (stream == null) {
            return;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1000];
        try {
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        String str = buffer.toString();
        List<String> rows = GXCommon.split(str, "\r\n");
        for (String it : rows) {
            List<String> items = GXCommon.split(it, ';');
            List<String> obis = GXCommon.split(items.get(0), '.');
            GXStandardObisCode code = new GXStandardObisCode(
                    obis.toArray(new String[0]),
                    items.get(3) + "; " + items.get(4) + "; " + items.get(5)
                            + "; " + items.get(6) + "; " + items.get(7),
                    items.get(1), items.get(2));
            codes.add(code);
        }
    }

}
