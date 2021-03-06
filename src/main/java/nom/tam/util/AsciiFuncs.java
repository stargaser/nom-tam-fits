/*
 * This class provides conversions to ASCII strings without breaking
 * compatibility with Java 1.5.
 */
package nom.tam.util;

/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 2004 - 2015 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */

import java.io.UnsupportedEncodingException;

/**
 * @author tmcglynn
 */
public class AsciiFuncs {

    public final static String ASCII = "US-ASCII";

    /** Convert to ASCII or return null if not compatible */
    public static String asciiString(byte[] buf) {
        return asciiString(buf, 0, buf.length);
    }

    /** Convert to ASCII or return null if not compatible */
    public static String asciiString(byte[] buf, int start, int len) {
        try {
            return new String(buf, start, len, ASCII);
        } catch (java.io.UnsupportedEncodingException e) {
            // Shouldn't happen
            System.err.println("AsciiFuncs.asciiString error finding ASCII encoding");
            return null;
        }
    }

    /** Convert an ASCII string to bytes */
    public static byte[] getBytes(String in) {
        try {
            return in.getBytes(ASCII);
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unable to find ASCII encoding");
            return null;
        }
    }
}
