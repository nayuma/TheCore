///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Rob Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////


package gnu.trove.stack.array;

import gnu.trove.impl.Constants;
import gnu.trove.list.array.TCharArrayList;
import gnu.trove.stack.TCharStack;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////


/**
 * A stack of char primitives, backed by a TCharArrayList
 */
public class TCharArrayStack implements TCharStack, Externalizable {
    static final long serialVersionUID = 1L;

    /**
     * the list used to hold the stack values.
     */
    protected TCharArrayList _list;

    public static final int DEFAULT_CAPACITY = Constants.DEFAULT_CAPACITY;


    /**
     * Creates a new <code>TCharArrayStack</code> instance with the default
     * capacity.
     */
    public TCharArrayStack() {
        this(DEFAULT_CAPACITY);
    }


    /**
     * Creates a new <code>TCharArrayStack</code> instance with the
     * specified capacity.
     *
     * @param capacity the initial depth of the stack
     */
    public TCharArrayStack(int capacity) {
        _list = new TCharArrayList(capacity);
    }


    /**
     * Creates a new <code>TCharArrayStack</code> instance with the
     * specified capacity.
     *
     * @param capacity       the initial depth of the stack
     * @param no_entry_value value that represents null
     */
    public TCharArrayStack(int capacity, char no_entry_value) {
        _list = new TCharArrayList(capacity, no_entry_value);
    }


    /**
     * Creates a new <code>TCharArrayStack</code> instance that is
     * a copy of the instanced passed to us.
     *
     * @param stack the instance to copy
     */
    public TCharArrayStack(TCharStack stack) {
        if (stack instanceof TCharArrayStack) {
            TCharArrayStack array_stack = (TCharArrayStack) stack;
            this._list = new TCharArrayList(array_stack._list);
        } else {
            throw new UnsupportedOperationException("Only support TCharArrayStack");
        }
    }


    /**
     * Returns the value that is used to represent null. The default
     * value is generally zero, but can be changed during construction
     * of the collection.
     *
     * @return the value that represents null
     */
    public char getNoEntryValue() {
        return _list.getNoEntryValue();
    }


    /**
     * Pushes the value onto the top of the stack.
     *
     * @param val an <code>char</code> value
     */
    public void push(char val) {
        _list.add(val);
    }


    /**
     * Removes and returns the value at the top of the stack.
     *
     * @return an <code>char</code> value
     */
    public char pop() {
        return _list.removeAt(_list.size() - 1);
    }


    /**
     * Returns the value at the top of the stack.
     *
     * @return an <code>char</code> value
     */
    public char peek() {
        return _list.get(_list.size() - 1);
    }


    /**
     * Returns the current depth of the stack.
     */
    public int size() {
        return _list.size();
    }


    /**
     * Clears the stack.
     */
    public void clear() {
        _list.clear();
    }


    /**
     * Copies the contents of the stack into a native array. Note that this will NOT
     * pop them out of the stack.  The front of the list will be the top of the stack.
     *
     * @return an <code>char[]</code> value
     */
    public char[] toArray() {
        char[] retval = _list.toArray();
        reverse(retval, 0, size());
        return retval;
    }


    /**
     * Copies a slice of the list into a native array. Note that this will NOT
     * pop them out of the stack.  The front of the list will be the top
     * of the stack.
     * <p>
     * If the native array is smaller than the stack depth,
     * the native array will be filled with the elements from the top
     * of the array until it is full and exclude the remainder.
     *
     * @param dest the array to copy into.
     */
    public void toArray(char[] dest) {
        int size = size();
        int start = size - dest.length;
        if (start < 0) {
            start = 0;
        }

        int length = Math.min(size, dest.length);
        _list.toArray(dest, start, length);
        reverse(dest, 0, length);
        if (dest.length > size) {
            dest[size] = _list.getNoEntryValue();
        }
    }


    /**
     * Reverse the order of the elements in the range of the list.
     *
     * @param dest the array of data
     * @param from the inclusive index at which to start reversing
     * @param to   the exclusive index at which to stop reversing
     */
    private void reverse(char[] dest, int from, int to) {
        if (from == to) {
            return;             // nothing to do
        }
        if (from > to) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        for (int i = from, j = to - 1; i < j; i++, j--) {
            swap(dest, i, j);
        }
    }


    /**
     * Swap the values at offsets <tt>i</tt> and <tt>j</tt>.
     *
     * @param dest the array of data
     * @param i    an offset into the data array
     * @param j    an offset into the data array
     */
    private void swap(char[] dest, int i, int j) {
        char tmp = dest[i];
        dest[i] = dest[j];
        dest[j] = tmp;
    }


    /**
     * Returns a String representation of the list, top to bottom.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        for (int i = _list.size() - 1; i > 0; i--) {
            buf.append(_list.get(i));
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append(_list.get(0));
        }
        buf.append("}");
        return buf.toString();
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TCharArrayStack that = (TCharArrayStack) o;

        return _list.equals(that._list);
    }


    public int hashCode() {
        return _list.hashCode();
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        // VERSION
        out.writeByte(0);

        // LIST
        out.writeObject(_list);
    }


    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {

        // VERSION
        in.readByte();

        // LIST
        _list = (TCharArrayList) in.readObject();
    }
} // TCharArrayStack
