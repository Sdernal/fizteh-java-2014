package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.util.ArrayList;
import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;

public class MyStoreable implements Storeable {
    
    private List<Object> components;    
    
    public MyStoreable(List<Class<?>> structure) {
        components = new ArrayList<>(structure.size());
    }
    
    public int size() {
        return this.components.size();
    }
    
    @Override
    public void setColumnAt(int columnIndex, Object value)
            throws ColumnFormatException, IndexOutOfBoundsException {
        if (value.getClass() != this.components.get(columnIndex).getClass()) {
            throw new ColumnFormatException("Value type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        this.components.set(columnIndex, value);
    }

    @Override
    public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return this.components.get(columnIndex);
    }

    @Override
    public Integer getIntAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Integer.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Integer) this.components.get(columnIndex);
    }

    @Override
    public Long getLongAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Long.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Long) this.components.get(columnIndex);
    }

    @Override
    public Byte getByteAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Byte.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Byte) this.components.get(columnIndex);
    }

    @Override
    public Float getFloatAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Float.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Float) this.components.get(columnIndex);
    }

    @Override
    public Double getDoubleAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Double.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Double) this.components.get(columnIndex);
    }

    @Override
    public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != Boolean.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (Boolean) this.components.get(columnIndex);
    }

    @Override
    public String getStringAt(int columnIndex) throws ColumnFormatException,
            IndexOutOfBoundsException {
        if (this.components.get(columnIndex).getClass() != String.class) {
            throw new ColumnFormatException("The requested type does not match column.");
        }
        if (columnIndex >= this.components.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return (String) this.components.get(columnIndex);
    }

}
