package nom.tam.fits;

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

import java.io.IOException;

import nom.tam.util.ArrayDataInput;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.RandomAccess;

/**
 * This class provides methods to access the data segment of an HDU.
 */
public abstract class Data implements FitsElement {

    /**
     * This is the object which contains the actual data for the HDU.
     * <ul>
     * <li>For images and primary data this is a simple (but possibly
     * multi-dimensional) primitive array. When group data is supported it will
     * be a possibly multidimensional array of group objects.
     * <li>For ASCII data it is a two dimensional Object array where each of the
     * constituent objects is a primitive array of length 1.
     * <li>For Binary data it is a two dimensional Object array where each of
     * the constituent objects is a primitive array of arbitrary (more or less)
     * dimensionality.
     * </ul>
     */
    /** The starting location of the data when last read */
    protected long fileOffset = -1;

    /** The size of the data when last read */
    protected long dataSize;

    /** The inputstream used. */
    protected RandomAccess input;

    /** Get the file offset */
    @Override
    public long getFileOffset() {
        return fileOffset;
    }

    /** Set the fields needed for a re-read */
    protected void setFileOffset(Object o) {
        if (o instanceof RandomAccess) {
            fileOffset = FitsUtil.findOffset(o);
            dataSize = getTrueSize();
            input = (RandomAccess) o;
        }
    }

    /**
     * Write the data -- including any buffering needed
     * 
     * @param o
     *            The output stream on which to write the data.
     */
    @Override
    public abstract void write(ArrayDataOutput o) throws FitsException;

    /**
     * Read a data array into the current object and if needed position to the
     * beginning of the next FITS block.
     * 
     * @param i
     *            The input data stream
     */
    @Override
    public abstract void read(ArrayDataInput i) throws FitsException;

    @Override
    public void rewrite() throws FitsException {

        if (!rewriteable()) {
            throw new FitsException("Illegal attempt to rewrite data");
        }

        FitsUtil.reposition(input, fileOffset);
        write((ArrayDataOutput) input);
        try {
            ((ArrayDataOutput) input).flush();
        } catch (IOException e) {
            throw new FitsException("Error in rewrite flush: " + e);
        }
    }

    @Override
    public boolean reset() {
        try {
            FitsUtil.reposition(input, fileOffset);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean rewriteable() {
        if (input == null || fileOffset < 0 || (getTrueSize() + 2879) / 2880 != (dataSize + 2879) / 2880) {
            return false;
        } else {
            return true;
        }
    }

    abstract long getTrueSize();

    /** Get the size of the data element in bytes */
    @Override
    public long getSize() {
        return FitsUtil.addPadding(getTrueSize());
    }

    /**
     * Return the data array object.
     */
    public abstract Object getData() throws FitsException;

    /** Return the non-FITS data object */
    public Object getKernel() throws FitsException {
        return getData();
    }

    /**
     * Modify a header to point to this data
     */
    abstract void fillHeader(Header head) throws FitsException;
}
