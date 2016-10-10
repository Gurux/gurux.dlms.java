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

final class SerialNumberCounter {

    /**
     * Constructor.
     */
    private SerialNumberCounter() {

    }

    /*
     * Get values to count together.
     */
    private static List<String> getValues(final String expressions) {
        List<String> values = new ArrayList<String>();
        int last = 0, index = 0;
        for (char ch : expressions.toCharArray()) {
            switch (ch) {
            case '%':
            case '+':
            case '-':
            case '*':
            case '/':
                values.add(expressions.substring(last, index));
                values.add(String.valueOf(ch));
                last = index + 1;
                break;
            default:
                break;
            }
            ++index;
        }
        if (index != last) {
            values.add(expressions.substring(last, index));
        }
        return values;
    }

    static int getValue(final String value, final int sn) {
        if ("sn".equals(value)) {
            return sn;
        }
        return Integer.parseInt(value);
    }

    /// <summary>
    /// Count serial number using formula.
    /// </summary>
    /// <param name="sn">Serial number</param>
    /// <param name="formula">Formula to used.</param>
    /// <returns></returns>
    public static int count(final int sn, final String formula) {
        List<String> values = getValues(formatString(formula));
        if (values.size() % 2 == 0) {
            throw new IllegalArgumentException(
                    "Invalid serial number formula.");
        }
        String str;
        int value = getValue(values.get(0), sn);
        for (int index = 1; index != values.size(); index += 2) {
            str = values.get(index);
            if ("%".equals(str)) {
                value = value % getValue(values.get(index + 1), sn);
            } else if ("+".equals(str)) {
                value = value + getValue(values.get(index + 1), sn);
            } else if ("-".equals(str)) {
                value = value - getValue(values.get(index + 1), sn);
            } else if ("*".equals(str)) {
                value = value * getValue(values.get(index + 1), sn);
            } else if ("/".equals(str)) {
                value = value / getValue(values.get(index + 1), sn);
            } else {
                throw new IllegalArgumentException(
                        "Invalid serial number formula.");
            }
        }
        return value;
    }

    /**
     * Produce formatted String by the given math expression.
     * 
     * @param expression
     *            Unformatted math expression.
     * @return Formatted math expression.
     */
    private static String formatString(final String expression) {
        if (expression == null || expression.length() == 0) {
            throw new RuntimeException("Expression is null or empty");
        }

        StringBuilder sb = new StringBuilder();
        for (char ch : expression.toLowerCase().toCharArray()) {
            if (ch == '(' || ch == ')') {
                throw new RuntimeException("Invalid serial number formula.");
            }
            // Is white space.
            if (ch == ' ') {
                continue;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
