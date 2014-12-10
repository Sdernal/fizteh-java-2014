package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;

public class MyStoreableTest{

    MyStoreable storeable;

    private void initStoreable() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Integer.class);
        list.add(Long.class);
        list.add(Byte.class);
        list.add(Float.class);
        list.add(Double.class);
        list.add(Boolean.class);
        list.add(String.class);
        storeable = new MyStoreable(list);
    }


    @Test
    public void correctConstructor() {
        initStoreable();
    }

    @Test
    public void initColumn() {
        initStoreable();
        storeable.setColumnAt(0, Integer.valueOf(1));
        storeable.setColumnAt(1, Long.valueOf(1));
        storeable.setColumnAt(2, Byte.valueOf((byte) 1));
        storeable.setColumnAt(3, Float.valueOf((float) 1.0));
        storeable.setColumnAt(4, Double.valueOf(1.0));
        storeable.setColumnAt(5, true);
        storeable.setColumnAt(6, "test");

        Assert.assertTrue((storeable.getColumnAt(0)).equals(1));
        Assert.assertTrue((storeable.getColumnAt(1)).equals((long) 1));
        Assert.assertTrue((storeable.getColumnAt(2)).equals((byte) 1));
        Assert.assertTrue((storeable.getColumnAt(3)).equals((float) 1.0));
        Assert.assertTrue((storeable.getColumnAt(4)).equals(1.0));
        Assert.assertTrue((storeable.getColumnAt(5)).equals(true));
        Assert.assertTrue((storeable.getColumnAt(6)).equals("test"));

        Assert.assertEquals(Integer.valueOf(1), storeable.getIntAt(0));
        Assert.assertEquals(Long.valueOf(1), storeable.getLongAt(1));
        Assert.assertEquals(Byte.valueOf((byte) 1), storeable.getByteAt(2));
        Assert.assertEquals(Float.valueOf((float) 1.0), storeable.getFloatAt(3));
        Assert.assertEquals(Double.valueOf(1.0), storeable.getDoubleAt(4));
        Assert.assertEquals(true, storeable.getBooleanAt(5));
        Assert.assertEquals("test", storeable.getStringAt(6));
    }

    @Test
    public void nullGet() {
        initStoreable();
        Assert.assertNull((storeable.getColumnAt(0)));
        Assert.assertNull((storeable.getColumnAt(1)));
        Assert.assertNull((storeable.getColumnAt(2)));
        Assert.assertNull((storeable.getColumnAt(3)));
        Assert.assertNull((storeable.getColumnAt(4)));
        Assert.assertNull((storeable.getColumnAt(5)));

        Assert.assertNull(storeable.getIntAt(0));
        Assert.assertNull(storeable.getLongAt(1));
        Assert.assertNull(storeable.getByteAt(2));
        Assert.assertNull(storeable.getFloatAt(3));
        Assert.assertNull(storeable.getDoubleAt(4));
        Assert.assertNull(storeable.getBooleanAt(5));
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void getColumnOutOfBounds() {
        initStoreable();
        storeable.getColumnAt(7);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void integerOutOfBounds() {
        initStoreable();
        storeable.getIntAt(7);
    }
   
    @Test(expected = ColumnFormatException.class)
    public void integerColumnInvalidFormat() {
        initStoreable();
        storeable.getIntAt(1);
    }

    @Test(expected = ColumnFormatException.class)
    public void longColumnInvalidFormat() {
        initStoreable();
        storeable.getLongAt(0);
    }
}

