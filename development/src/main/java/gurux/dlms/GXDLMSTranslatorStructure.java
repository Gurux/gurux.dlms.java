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

import java.util.HashMap;

import gurux.dlms.enums.DataType;
import gurux.dlms.internal.GXCommon;

/**
 * This class is used internally in GXDLMSTranslator to save generated XML.
 */
public class GXDLMSTranslatorStructure {
    private StringBuilder sb = new StringBuilder();
    private HashMap<Integer, String> tags;

    private final TranslatorOutputType outputType;
    /**
     * Are numeric values shows as hex.
     */
    private boolean showNumericsAsHex;

    /**
     * Is string serialized as hex.
     */
    private boolean showStringAsHex;

    /**
     * Amount of spaces.
     */
    private int offset;
    /**
     * Add comments.
     */
    private boolean comments;

    /**
     * @return Amount of spaces.
     */
    public final TranslatorOutputType getOutputType() {
        return outputType;
    }

    /**
     * @return Amount of spaces.
     */
    public final int getOffset() {
        return offset;
    }

    /**
     * @param value
     *            Amount of spaces.
     */
    public final void setOffset(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("offset");
        }
        offset = value;
    }

    public final boolean getShowStringAsHex() {
        return showStringAsHex;
    }

    final void setShowStringAsHex(final boolean value) {
        showStringAsHex = value;
    }

    /**
     * Constructor.
     * 
     * @param list
     *            List of tags.
     */
    GXDLMSTranslatorStructure(final TranslatorOutputType type,
            final boolean numericsAshex, final boolean hex,
            final boolean addComments, final HashMap<Integer, String> list) {
        outputType = type;
        showNumericsAsHex = numericsAshex;
        setShowStringAsHex(hex);
        tags = list;
        comments = addComments;
    }

    @Override
    public final String toString() {
        return sb.toString();
    }

    public final String getDataType(final DataType type) {
        return tags.get(GXDLMS.DATA_TYPE_OFFSET + type.getValue());
    }

    /**
     * Append spaces to the buffer.
     * 
     * @param count
     *            Amount of spaces.
     */
    private static void appendSpaces(final StringBuilder sb, final int count) {
        for (int pos = 0; pos != count; ++pos) {
            sb.append(' ');
        }
    }

    public final void appendLine(final String str) {
        appendSpaces(sb, 2 * offset);
        sb.append(str);
        sb.append('\r');
        sb.append('\n');
    }

    public final void appendLine(final int tag, final String name,
            final String value) {
        String tmp = tags.get(tag);
        if (tmp == null) {
            throw new IllegalArgumentException("Tag");
        }
        appendLine(tmp, name, value);
    }

    public final void appendLine(final String tag, final String name,
            final String value) {
        appendSpaces(sb, 2 * offset);
        sb.append('<');
        sb.append(tag);
        if (outputType == TranslatorOutputType.SIMPLE_XML) {
            sb.append(' ');
            if (name == null) {
                sb.append("Value");
            } else {
                sb.append(name);
            }
            sb.append("=\"");
        } else {
            sb.append('>');
        }
        sb.append(value);
        if (outputType == TranslatorOutputType.SIMPLE_XML) {
            sb.append("\" />");
        } else {
            sb.append("</");
            sb.append(tag);
            sb.append('>');
        }
        sb.append('\r');
        sb.append('\n');
    }

    /**
     * Append comment.
     * 
     * @param comment
     *            Comment to add.
     */
    public final void appendComment(final String comment) {
        if (comments) {
            appendSpaces(sb, 2 * offset);
            sb.append("<!--");
            sb.append(comment);
            sb.append("-->");
            sb.append('\r');
            sb.append('\n');
        }
    }

    /**
     * Start comment section.
     * 
     * @param comment
     *            Comment to add.
     */
    public void startComment(final String comment) {
        if (comments) {
            appendSpaces(sb, 2 * offset);
            sb.append("<!--");
            sb.append(comment);
            sb.append('\r');
            sb.append('\n');
            ++offset;
        }
    }

    /**
     * End comment section.
     */
    public final void endComment() {
        if (comments) {
            --offset;
            appendSpaces(sb, 2 * offset);
            sb.append("-->");
            sb.append('\r');
            sb.append('\n');
        }
    }

    public final void append(final String value) {
        sb.append(value);
    }

    public final void append(final int tag, final boolean start) {
        if (start) {
            appendSpaces(sb, 2 * offset);
            sb.append('<');
        } else {
            sb.append("</");
        }
        sb.append(tags.get(tag));
        sb.append('>');
    }

    public final void appendStartTag(final int tag, final String name,
            final String value) {
        appendStartTag(tags.get(tag), name, value);
    }

    public final void appendStartTag(final String tag, final String name,
            final String value) {
        appendSpaces(sb, 2 * offset);
        sb.append('<');
        sb.append(tag);
        if (outputType == TranslatorOutputType.SIMPLE_XML && name != null) {
            sb.append(' ');
            sb.append(name);
            sb.append("=\"");
            sb.append(value);
            sb.append("\" >");
        } else {
            sb.append('>');
        }
        sb.append('\r');
        sb.append('\n');
        ++offset;
    }

    public final void appendStartTag(final int tag) {
        String tmp = tags.get(tag);
        if (tmp == null) {
            throw new IllegalArgumentException("appendStartTag");
        }
        appendSpaces(sb, 2 * offset);
        sb.append("<");
        sb.append(tmp);
        sb.append('>');
        sb.append('\r');
        sb.append('\n');
        ++offset;
    }

    public final void appendStartTag(final int cmd, final byte type) {
        appendStartTag(cmd << 8 | type);
    }

    public final void appendEndTag(final int cmd, final byte type) {
        appendEndTag(cmd << 8 | type);
    }

    public final void appendEndTag(final int tag) {
        appendEndTag(tags.get(tag));
    }

    public final void appendEndTag(final String tag) {
        setOffset(getOffset() - 1);
        appendSpaces(sb, 2 * offset);
        sb.append("</");
        sb.append(tag);
        sb.append('>');
        sb.append('\r');
        sb.append('\n');
    }

    /**
     * Remove \r\n.
     */
    public final void trim() {
        sb.setLength(sb.length() - 2);
    }

    /**
     * @return XML Length.
     */
    public final int getXmlLength() {
        return sb.length();
    }

    /**
     * @param value
     *            Set XML Length.
     */
    public final void setXmlLength(final int value) {
        sb.setLength(value);
    }

    /**
     * Convert integer to string.
     * 
     * @param value
     *            Converted value.
     * @param desimals
     *            Amount of decimals.
     * @return Integer as string.
     */
    public final String integerToHex(final long value, final int desimals) {
        return integerToHex(value, desimals, false);
    }

    /**
     * Convert integer to string.
     * 
     * @param value
     *            Converted value.
     * @param desimals
     *            Amount of decimals.
     * @param forceHex
     *            Force value as hex.
     * @return Integer as string.
     */
    public final String integerToHex(final long value, final int desimals,
            final boolean forceHex) {
        if (forceHex || (showNumericsAsHex
                && outputType == TranslatorOutputType.SIMPLE_XML)) {
            return GXCommon.integerToHex(value, desimals);
        }
        return String.valueOf(value);
    }

    /**
     * Convert integer to string.
     * 
     * @param value
     *            Converted value.
     * @param desimals
     *            Amount of decimals.
     * @return Integer as string.
     */
    public final String integerToHex(final Object value, final int desimals) {
        if (showNumericsAsHex
                && outputType == TranslatorOutputType.SIMPLE_XML) {
            return GXCommon.integerToHex(value, desimals);
        }
        return String.valueOf(value);
    }

    /**
     * @return Are comments added.
     */
    public boolean isComments() {
        return comments;
    }

    /**
     * @param value
     *            Are comments added.
     */
    public void setComments(final boolean value) {
        comments = value;
    }
}
