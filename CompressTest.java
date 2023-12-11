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

package gurux.dlms;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompressTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public final void setUp() {
    }

    @After
    public final void tearDown() {
    }

    private static class Token {
        int offset;
        int length;
        char nextChar;

        Token(int offset, int length, char nextChar) {
            this.offset = offset;
            this.length = length;
            this.nextChar = nextChar;
        }

        @Override
        public String toString() {
            return "(" + offset + "," + length + "," + nextChar + ")";
        }
    }

    public static List<Token> compress(String input) {
        List<Token> tokens = new ArrayList<>();
        int windowSize = 1024;

        int i = 0;
        while (i < input.length()) {
            int maxMatchLength = 0;
            int maxMatchOffset = 0;

            for (int j = Math.max(0, i - windowSize); j < i; j++) {
                int k = j;
                int length = 0;

                while (k < input.length() && i + length < input.length()
                        && input.charAt(k) == input.charAt(i + length)) {
                    length++;
                    k++;
                }

                if (length > maxMatchLength) {
                    maxMatchLength = length;
                    maxMatchOffset = i - j;
                }
            }

            if (maxMatchLength > 0) {
                tokens.add(new Token(maxMatchOffset, maxMatchLength,
                        input.charAt(i + maxMatchLength)));
                i += maxMatchLength + 1;
            } else {
                tokens.add(new Token(0, 0, input.charAt(i)));
                i++;
            }
        }

        return tokens;
    }

    /*
     * Test byte to hex.
     */
    @Test
    public final void compressTest() {
        String input = "ABABABABABABAB";
        List<Token> compressed = compress(input);

        // Print compressed tokens
        for (Token token : compressed) {
            System.out.print(token);
        }
    }

    /*
     * Test byte to hex.
     */
    @Test
    public final void compress2Test() {
        String input = "ABCDEXABCDEYABCDE";
        List<Token> compressed = compress(input);

        // Print compressed tokens
        for (Token token : compressed) {
            System.out.print(token);
        }
    }

    /*
     * Test byte to hex.
     */
    @Test
    public final void compress3Test() {
        String input = "0F 70 53 9C 2D B5 FF 2D B0 C2";
        String pdu = "05 C4 02 04 03 60 00 60 E2 01";
        List<Token> compressed = compress(input);
        // Print compressed tokens
        for (Token token : compressed) {
            System.out.print(token);
        }
    }
}
