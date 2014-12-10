package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;

public class MyTableProviderTest {

    private static MyTableProvider provider;
    private static List<Class<?>> structure;
    private static List<Object> valuesList;
    private static String correctSerialized;
    private static String nullSerialized;


    @BeforeClass
    public static void beforeClass() throws IOException {

        structure = new ArrayList<>();
        structure.add(Integer.class);
        structure.add(Long.class);
        structure.add(Byte.class);
        structure.add(Float.class);
        structure.add(Double.class);
        structure.add(Boolean.class);
        structure.add(String.class);

        valuesList = new ArrayList<>();
        valuesList.add(1);
        valuesList.add((long) 2);
        valuesList.add((byte) 3);
        valuesList.add((float) 4);
        valuesList.add((double) 5);
        valuesList.add(true);
        valuesList.add("seven"); 

        correctSerialized = "<row><col>1</col><col>2</col><col>3</col><col>4.0</col>"
                + "<col>5.0</col><col>true</col><col>seven</col></row>";
        nullSerialized = "<row><null/><null/><null/><null/><null/><null/><null/></row>";

        provider = (MyTableProvider) (new MyTableProviderFactory()).create(System.getProperty("fizteh.db.dir"));
    } 

    @Test(expected = IllegalArgumentException.class)
    public void createNull() throws IOException {
        provider.createTable(null, structure);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullSignature() throws IOException {
        provider.createTable("table", null);
    }

    @Test
    public void createNotExisting() throws IOException {
        Assert.assertNotNull(provider.createTable("table", structure));
    }

    @Test
    public void createExisting() throws IOException {
        provider.createTable("table", structure);
        Assert.assertNull(provider.createTable("table", structure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNull() throws IOException {
        provider.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public void removeNotExisting() throws IOException {
        provider.removeTable("table");
    }

    @Test
    public void removeExisting() throws IOException {
        provider.createTable("table", structure);
        provider.removeTable("table");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNull() {
        provider.getTable(null);
    }

    @Test
    public void getNotExisting() {
        Assert.assertNull(provider.getTable("table"));
    }

    @Test
    public void getExisting() throws IOException {
        provider.createTable("table", structure);
        Assert.assertNotNull(provider.getTable("table"));
    }

    @Test
    public void get() throws IOException {
        provider.createTable("table", structure);
        provider.createTable("table2", structure);
        provider.removeTable("table");
        Assert.assertNull(provider.getTable("table"));
        Assert.assertNotNull(provider.getTable("table2"));
    }

    @Test
    public void emptyCreateFor() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table);
        storeable.getIntAt(0);
        storeable.getLongAt(1);
        storeable.getByteAt(2);
        storeable.getFloatAt(3);
        storeable.getDoubleAt(4);
        storeable.getBooleanAt(5);
        storeable.getStringAt(6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void emptyCreateForOutOfBounds() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table);
        storeable.getColumnAt(7);
    }

    @Test
    public void createFor() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table, valuesList);
        Assert.assertEquals(valuesList.get(0), storeable.getIntAt(0));
        Assert.assertEquals(valuesList.get(1), storeable.getLongAt(1));
        Assert.assertEquals(valuesList.get(2), storeable.getByteAt(2));
        Assert.assertEquals(valuesList.get(3), storeable.getFloatAt(3));
        Assert.assertEquals(valuesList.get(4), storeable.getDoubleAt(4));
        Assert.assertEquals(valuesList.get(5), storeable.getBooleanAt(5));
        Assert.assertEquals(valuesList.get(6), storeable.getStringAt(6));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createForOutOfBounds() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table, valuesList);
        storeable.getColumnAt(7);
    }

    @Test(expected = ColumnFormatException.class)
    public void createForInvalidColumnFormat() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        List<Object> invalidValues = new ArrayList<>();
        invalidValues.add("test");
        invalidValues.add("fail");
        provider.createFor(table, invalidValues);
    }

    @Test
    public void serialize() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table, valuesList);
        String serialized = provider.serialize(table, storeable);
        Assert.assertEquals(serialized, correctSerialized);
    }

    @Test
    public void serializeNullValues() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        MyStoreable storeable = (MyStoreable) provider.createFor(table);
        String serialized = provider.serialize(table, storeable);
        Assert.assertEquals(serialized, nullSerialized);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void serializeSmallerSize() throws IOException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        List<Class<?>> invalidList = new ArrayList<>();
        for (int i = 0; i + 1 < structure.size(); ++i) {
            invalidList.add(structure.get(i));
        }
        MyTable invalidTable = (MyTable) provider.createTable("table2", invalidList);
        MyStoreable storeable = (MyStoreable) provider.createFor(invalidTable);
        provider.serialize(table, storeable);
    } 

    @Test(expected = ParseException.class)
    public void deserializeInvalidXmlRow()
            throws IOException, ParseException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        provider.deserialize(table, "<col>gg</col>");
    }

    @Test(expected = ParseException.class)
    public void deserializeInvalidXmlCol()
            throws IOException, ParseException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        provider.deserialize(table, "<row><col>gg</row>");
    }

    @Test(expected = ParseException.class)
    public void deserializeInvalidXmlLessColumns()
            throws IOException, ParseException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        provider.deserialize(table, "<row><col>1</col></row>");
    }

    @Test(expected = ParseException.class)
    public void deserializeInvalidXmlMoreColumns()
            throws IOException, ParseException {
        MyTable table = (MyTable) provider.createTable("table", structure);
        String incorrectSerialized =
                correctSerialized.replace("</row", "<col>1</col></row>");
        provider.deserialize(table, incorrectSerialized);
    }

    @Test
    public void deserialize() throws IOException, ParseException {
        MyTable table =
                (MyTable) provider.createTable("table", structure);
        MyStoreable storeable =
                (MyStoreable) provider.deserialize(table, correctSerialized);
        for (int columnNumber = 0; columnNumber < valuesList.size();
             columnNumber++) {
            Assert.assertEquals(valuesList.get(columnNumber),
                    storeable.getColumnAt(columnNumber));
        }
    }

    @Test
    public void deserializeNullValues()
            throws IOException, ParseException {
        MyTable table =
                (MyTable) provider.createTable("table", structure);
        MyStoreable storeable =
                (MyStoreable) provider.deserialize(table, nullSerialized);
        for (int columnNumber = 0; columnNumber < structure.size();
             columnNumber++) {
            Assert.assertNull(storeable.getColumnAt(columnNumber));
        }
    }

    @Test
    public void getTableNames() throws IOException {
        provider.createTable("table1", structure);
        provider.createTable("table2", structure);
        List<String> names = provider.getTableNames();

        Assert.assertTrue(("table1".equals(names.get(0)) && "table2".equals(names.get(1)))
                || ("table2".equals(names.get(0)) && "table1".equals(names.get(1))));
    }

    @After
    public void clearAll() throws IOException {
        try {
            File testDir = new File(System.getProperty("fizteh.db.dir"));
            for (File curDir : testDir.listFiles()) {
                for (File file : curDir.listFiles()) {
                    Files.delete(file.toPath());
                    file.delete();
                }
                curDir.delete();
            }
        } catch (NullPointerException e) {
            //if it happens, than it means, that we were trying to create something in other directory;
        }
    }
}
