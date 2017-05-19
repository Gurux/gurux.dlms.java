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
package gurux.dlms;

import java.util.ArrayList;
import java.util.List;

import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Standard OBIS code collection is used to save all default OBIS Codes.
 */
class GXStandardObisCodeCollection
        extends java.util.ArrayList<GXStandardObisCode> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * Convert Logical name string to bytes.
     */
    static int[] getBytes(final String ln) {
        if (ln == null || ln.isEmpty()) {
            return null;
        }
        List<String> tmp = GXCommon.split(ln, '.');
        if (tmp.size() != 6) {
            // If value is give as hex.
            byte[] tmp2 = GXCommon.hexToBytes(ln);
            if (tmp2.length == 6) {
                return new int[] { tmp2[0] & 0xFF, tmp2[1] & 0xFF,
                        tmp2[2] & 0xFF, tmp2[3] & 0xFF, tmp2[4] & 0xFF,
                        tmp2[5] & 0xFF };
            }
            if (tmp.size() != 6) {
                throw new IllegalArgumentException("Invalid OBIS Code.");
            }
        }
        int[] code = new int[6];
        code[0] = Integer.parseInt(tmp.get(0));
        code[1] = Integer.parseInt(tmp.get(1));
        code[2] = Integer.parseInt(tmp.get(2));
        code[3] = Integer.parseInt(tmp.get(3));
        code[4] = Integer.parseInt(tmp.get(4));
        code[5] = Integer.parseInt(tmp.get(5));
        return code;
    }

    public final GXStandardObisCode[] find(final String ln,
            final ObjectType objectType) {
        int ic = 0;
        if (objectType != null) {
            ic = objectType.getValue();
        }
        return find(getBytes(ln), ic);
    }

    /**
     * Check is interface included to standard.
     */
    private boolean equalsInterface(final GXStandardObisCode it, final int ic) {
        // If all interfaces are allowed.
        if (ic == 0 || it.getInterfaces().equals("*")) {
            return true;
        }
        return GXCommon.split(it.getInterfaces(), ',')
                .contains((new Integer(ic)).toString());
    }

    /**
     * Check OBIS codes.
     */
    private static boolean equalsMask(final String obis, final int ic) {
        boolean number = true;
        if (obis.indexOf(',') != -1) {
            List<String> tmp = GXCommon.split(obis, ',');
            for (String it : tmp) {
                if (it.indexOf('-') != -1) {
                    if (equalsMask(it, ic)) {
                        return true;
                    }
                } else if (Integer.parseInt(it) == ic) {
                    return true;
                }
            }
            return false;
        } else if (obis.indexOf('-') != -1) {
            number = false;
            List<String> tmp = GXCommon.split(obis, '-');
            return ic >= Integer.parseInt(tmp.get(0))
                    && ic <= Integer.parseInt(tmp.get(1));
        }
        if (number) {
            if (obis.equals("&")) {
                return ic == 0 || ic == 1 || ic == 7;
            }
            return Integer.parseInt(obis) == ic;
        }
        return false;
    }

    public static boolean equalsMask(final String obisMask, final String ln) {
        return equalsObisCode(
                GXCommon.split(obisMask, '.').toArray(new String[0]),
                getBytes(ln));
    }

    /**
     * Check OBIS code.
     */
    private static boolean equalsObisCode(final String[] obisMask,
            final int[] ic) {
        if (ic == null) {
            return true;
        }
        if (!equalsMask(obisMask[0], ic[0])) {
            return false;
        }
        if (!equalsMask(obisMask[1], ic[1])) {
            return false;
        }
        if (!equalsMask(obisMask[2], ic[2])) {
            return false;
        }
        if (!equalsMask(obisMask[3], ic[3])) {
            return false;
        }
        if (!equalsMask(obisMask[4], ic[4])) {
            return false;
        }
        if (!equalsMask(obisMask[5], ic[5])) {
            return false;
        }
        return true;
    }

    /**
     * Get description.
     */
    // CHECKSTYLE:OFF
    private String getDescription(final String str) {
        if (str == null || str.length() == 0 || str.charAt(0) != '$') {
            return "";
        }
        int value = Integer.parseInt(str.substring(1));
        switch (value) {
        case 1:
            return "Sum Li Active power+ (QI+QIV)";
        case 2:
            return "Sum Li Active power- (QII+QIII)";
        case 3:
            return "Sum Li Reactive power+ (QI+QII)";
        case 4:
            return "Sum Li Reactive power- (QIII+QIV)";
        case 5:
            return "Sum Li Reactive power QI";
        case 6:
            return "Sum Li Reactive power QII";
        case 7:
            return "Sum Li Reactive power QIII";
        case 8:
            return "Sum Li Reactive power QIV";
        case 9:
            return "Sum Li Apparent power+ (QI+QIV)";
        case 10:
            return "Sum Li Apparent power- (QII+QIII)";
        case 11:
            return "Current: any phase";
        case 12:
            return "Voltage: any phase";
        case 13:
            return "Sum Li Power factor";
        case 14:
            return "Supply frequency";
        case 15:
            return "Sum LI Active power (abs(QI+QIV)+abs(QII+QIII))";
        case 16:
            return "Sum LI Active power (abs(QI+QIV)-abs(QII+QIII))";
        case 17:
            return "Sum Li Active power QI";
        case 18:
            return "Sum Li Active power QII";
        case 19:
            return "Sum Li Active power QIII";
        case 20:
            return "Sum Li Active power QIV";
        case 21:
            return "L1 Active power+ (QI+QIV)";
        case 22:
            return "L1 Active power- (QII+QIII)";
        case 23:
            return "L1 Reactive power+ (QI+QII)";
        case 24:
            return "L1 Reactive power- (QIII+QIV)";
        case 25:
            return "L1 Reactive power QI";
        case 26:
            return "L1 Reactive power QII";
        case 27:
            return "L1 Reactive power QIII";
        case 28:
            return "L1 Reactive power QIV";
        case 29:
            return "L1 Apparent power+ (QI+QIV)";
        case 30:
            return "L1 Apparent power- (QII+QIII)";
        case 31:
            return "L1 Current";
        case 32:
            return "L1 Voltage";
        case 33:
            return "L1 Power factor";
        case 34:
            return "L1 Supply frequency";
        case 35:
            return "L1 Active power (abs(QI+QIV)+abs(QII+QIII))";
        case 36:
            return "L1 Active power (abs(QI+QIV)-abs(QII+QIII))";
        case 37:
            return "L1 Active power QI";
        case 38:
            return "L1 Active power QII";
        case 39:
            return "L1 Active power QIII";
        case 40:
            return "L1 Active power QIV";
        case 41:
            return "L2 Active power+ (QI+QIV)";
        case 42:
            return "L2 Active power- (QII+QIII)";
        case 43:
            return "L2 Reactive power+ (QI+QII)";
        case 44:
            return "L2 Reactive power- (QIII+QIV)";
        case 45:
            return "L2 Reactive power QI";
        case 46:
            return "L2 Reactive power QII";
        case 47:
            return "L2 Reactive power QIII";
        case 48:
            return "L2 Reactive power QIV";
        case 49:
            return "L2 Apparent power+ (QI+QIV)";
        case 50:
            return "L2 Apparent power- (QII+QIII)";
        case 51:
            return "L2 Current";
        case 52:
            return "L2 Voltage";
        case 53:
            return "L2 Power factor";
        case 54:
            return "L2 Supply frequency";
        case 55:
            return "L2 Active power (abs(QI+QIV)+abs(QII+QIII))";
        case 56:
            return "L2 Active power (abs(QI+QIV)-abs(QI+QIII))";
        case 57:
            return "L2 Active power QI";
        case 58:
            return "L2 Active power QII";
        case 59:
            return "L2 Active power QIII";
        case 60:
            return "L2 Active power QIV";
        case 61:
            return "L3 Active power+ (QI+QIV)";
        case 62:
            return "L3 Active power- (QII+QIII)";
        case 63:
            return "L3 Reactive power+ (QI+QII)";
        case 64:
            return "L3 Reactive power- (QIII+QIV)";
        case 65:
            return "L3 Reactive power QI";
        case 66:
            return "L3 Reactive power QII";
        case 67:
            return "L3 Reactive power QIII";
        case 68:
            return "L3 Reactive power QIV";
        case 69:
            return "L3 Apparent power+ (QI+QIV)";
        case 70:
            return "L3 Apparent power- (QII+QIII)";
        case 71:
            return "L3 Current";
        case 72:
            return "L3 Voltage";
        case 73:
            return "L3 Power factor";
        case 74:
            return "L3 Supply frequency";
        case 75:
            return "L3 Active power (abs(QI+QIV)+abs(QII+QIII))";
        case 76:
            return "L3 Active power (abs(QI+QIV)-abs(QI+QIII))";
        case 77:
            return "L3 Active power QI";
        case 78:
            return "L3 Active power QII";
        case 79:
            return "L3 Active power QIII";
        case 80:
            return "L3 Active power QIV";
        case 82:
            return "Unitless quantities (pulses or pieces)";
        case 84:
            return "Sum Li Power factor-";
        case 85:
            return "L1 Power factor-";
        case 86:
            return "L2 Power factor-";
        case 87:
            return "L3 Power factor-";
        case 88:
            return "Sum Li A2h QI+QII+QIII+QIV";
        case 89:
            return "Sum Li V2h QI+QII+QIII+QIV";
        case 90:
            return "SLi current (algebraic sum of the – unsigned – value of the currents in all phases)";
        case 91:
            return "Lo Current (neutral)";
        case 92:
            return "Lo Voltage (neutral)";
        default:
            return "";
        }
    }
    // CHECKSTYLE:ON

    /**
     * Get OBIS value.
     * 
     * @param formula
     *            OBIS formula.
     * @param value
     *            OBIS value.
     * @return OBIS value as integer.
     */
    private static int getObisValue(final String formula, final int value) {
        return value + Integer.parseInt(formula.substring(1));
    }

    /**
     * Find Standard OBIS Code description.
     */
    public final GXStandardObisCode[] find(final int[] obisCode, final int ic) {
        GXStandardObisCode tmp;
        List<GXStandardObisCode> list = new ArrayList<GXStandardObisCode>();

        for (GXStandardObisCode it : this) {
            // Interface is tested first because it's faster.
            if (equalsInterface(it, ic)
                    && equalsObisCode(it.getOBIS(), obisCode)) {
                tmp = new GXStandardObisCode(it.getOBIS(), it.getDescription(),
                        it.getInterfaces(), it.getDataType());
                list.add(tmp);
                List<String> tmp2 = GXCommon.split(it.getDescription(), ';');
                if (tmp2.size() > 1) {
                    String desc = "";
                    if (obisCode != null && "$1".equals(tmp2.get(1).trim())) {
                        desc = getDescription("$" + obisCode[2]);
                    }
                    if (!desc.equals("")) {
                        tmp2.set(1, desc);
                        StringBuilder builder = new StringBuilder();
                        for (String s : tmp2) {
                            if (builder.capacity() != 0) {
                                builder.append(";");
                            }
                            builder.append(s);
                        }
                        tmp.setDescription(builder.toString());
                    }
                }

                if (obisCode != null) {
                    String[] obis = tmp.getOBIS();
                    obis[0] = Integer.toString(obisCode[0]);
                    obis[1] = Integer.toString(obisCode[1]);
                    obis[2] = Integer.toString(obisCode[2]);
                    obis[3] = Integer.toString(obisCode[3]);
                    obis[4] = Integer.toString(obisCode[4]);
                    obis[5] = Integer.toString(obisCode[5]);

                    tmp.setOBIS(obis);
                    String desc = tmp.getDescription();
                    desc = desc.replace("$A", Integer.toString(obisCode[0]));
                    desc = desc.replace("$B", Integer.toString(obisCode[1]));
                    desc = desc.replace("$C", Integer.toString(obisCode[2]));
                    desc = desc.replace("$D", Integer.toString(obisCode[3]));
                    desc = desc.replace("$E", Integer.toString(obisCode[4]));
                    desc = desc.replace("$F", Integer.toString(obisCode[5]));
                    // Increase value
                    int begin = desc.indexOf("#$(");
                    if (begin != -1) {
                        List<String> arr =
                                GXCommon.split(desc.substring(begin + 2),
                                        new char[] { '(', ')', '$' });
                        desc = desc.substring(0, begin);
                        for (String v : arr) {
                            switch (v.charAt(0)) {
                            case 'A':
                                desc += "#" + getObisValue(v, obisCode[0]);
                                break;
                            case 'B':
                                desc += "#" + getObisValue(v, obisCode[1]);
                                break;
                            case 'C':
                                desc += "#" + getObisValue(v, obisCode[2]);
                                break;
                            case 'D':
                                desc += "#" + getObisValue(v, obisCode[3]);
                                break;
                            case 'E':
                                desc += "#" + getObisValue(v, obisCode[4]);
                                break;
                            case 'F':
                                desc += "#" + getObisValue(v, obisCode[5]);
                                break;
                            default:
                                desc += v;
                                break;
                            }
                        }
                    }
                    desc = desc.replace(';', ' ').replace("  ", " ").trim();
                    tmp.setDescription(desc);
                }
            }
        }
        // If invalid OBIS code.
        if (list.size() == 0) {
            tmp = new GXStandardObisCode(null, "Invalid",
                    (new Integer(ic)).toString(), "");
            String[] obis = tmp.getOBIS();
            obis[0] = Integer.toString(obisCode[0]);
            obis[1] = Integer.toString(obisCode[1]);
            obis[2] = Integer.toString(obisCode[2]);
            obis[3] = Integer.toString(obisCode[3]);
            obis[4] = Integer.toString(obisCode[4]);
            obis[5] = Integer.toString(obisCode[5]);
            tmp.setOBIS(obis);
            list.add(tmp);
        }
        return list.toArray(new GXStandardObisCode[list.size()]);
    }
}