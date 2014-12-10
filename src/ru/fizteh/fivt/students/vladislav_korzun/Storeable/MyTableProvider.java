package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;

public class MyTableProvider implements TableProvider {

    private List<String> tables;
    private Path rootDir;
    
    public MyTableProvider(String path) {
        rootDir = Paths.get(path);
        if (!rootDir.toFile().exists()) {
            rootDir.toFile().mkdir();
        } else {
            if (!rootDir.toFile().isDirectory()) {
                throw new IllegalArgumentException("Path of root directory is file"); 
            } else {
                tables = new ArrayList<>();
                String[] tableDirs = rootDir.toFile().list();
                for (String currentTable: tableDirs) {
                    Path currentTablePath = rootDir.resolve(currentTable);
                    if (currentTablePath.toFile().isDirectory()) {
                        new MyTable(currentTablePath, currentTable);
                        tables.add(currentTable);
                    }
                }
            }
        }
    }

    @Override
    public Table getTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (this.tables.contains(name)) {
            return new MyTable(this.rootDir.resolve(name), name);
        } else {
            throw new IllegalArgumentException("No such table");
        }
    }

    @Override
    public Table createTable(String name, List<Class<?>> columnTypes)
            throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (!this.tables.contains(name)) {
            return null;
        } else {
            Path newTablePath = rootDir.resolve(name);
            newTablePath.toFile().mkdir();
            Table newTable = new MyTable(newTablePath, name, columnTypes);
            this.tables.add(name);
            return newTable;
        }
    }

    @Override
    public void removeTable(String name) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        Path tableDir = this.rootDir.resolve(name);
        if (!this.tables.remove(name)) {
            throw new IllegalStateException("No table with name"  + name);
        } else {
            removedat(tableDir);
        }
}

    @Override
    public Storeable deserialize(Table table, String value)
            throws ParseException {
        if (!value.startsWith("<row>")) {
            throw new ParseException("<row> expected", 0);
        } else if (!value.endsWith("</row>")) {
            throw new ParseException("</row> expected", value.length() - 1);
        }
        String subValue = value.substring(5, value.length() - 6);
        Storeable storeable = createFor(table);
        for (int columnNumber = 0; columnNumber < table.getColumnsCount();
                columnNumber++) {
               if (subValue.startsWith("<null/>")) {
                   subValue = subValue.substring(7);
               } else if (subValue.startsWith("<col>")) {
                   subValue = subValue.substring(5);
                   int pos = subValue.indexOf("</col>");
                   if (pos == -1) {
                       throw new ParseException("Incorrect xml syntax", 0);
                   } else {
                       String column = subValue.substring(0, pos);
                       storeable.setColumnAt(columnNumber,
                               parseObject(column, table.getColumnType(columnNumber)));
                       subValue = value.substring(pos + 6);
                   }
               } else {
                   throw new ParseException("Incorrect xml syntax", 0);
               }
           }
           
           return storeable;
    }

    private Object parseObject(String column, Class<?> columnType) throws ParseException {
        try { 
            if (columnType == Integer.class) {
                return Integer.parseInt(column);
            }
            if (columnType == Long.class) {
                return Long.parseLong(column);
            }
            if (columnType == Byte.class) {
                return Byte.parseByte(column);
            }
            if (columnType == Float.class) {
                return Float.parseFloat(column);
            }
            if (columnType == Double.class) {
                return Double.parseDouble(column);
            }
            if (columnType == Boolean.class) {
                if (!"true".equals(column) && !"false".equals(column)) {
                    throw new ParseException("Incorrect xml syntax", 0);
                }
                return Boolean.parseBoolean(column);
            } else {
                return column;
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Incorrect xml syntax", 0);
        }       
    }

    @Override
    public String serialize(Table table, Storeable value)
            throws ColumnFormatException {
        for (int i = 0; i < table.getColumnsCount(); ++i) {
            Object val = value.getColumnAt(i);
            if (val != null && !val.getClass().equals(table.getColumnType(i))) {
                throw new ColumnFormatException("Invalid storeable format");
            }
        }

        StringBuilder serialized = new StringBuilder("<row>");
        for (int i = 0; i < table.getColumnsCount(); ++i) {
            Object column = value.getColumnAt(i);
            if (column == null) {
                serialized.append("<null/>");
            } else {
                serialized.append("<col>");
                if (column.getClass() == String.class) {
                    serialized.append(column);
                } else {
                    serialized.append(valueToString(column));
                }
                serialized.append("</col>");
            }
        }
        serialized.append("</row>");
        return serialized.toString();
    }

    private Object valueToString(Object column) {
        if (column.getClass() == Integer.class) {
            return Integer.toString((Integer) column);
        }
        if (column.getClass() == Long.class) {
            return Long.toString((Long) column);
        }
        if (column.getClass() == Byte.class) {
            return Byte.toString((Byte) column);
        }
        if (column.getClass() == Float.class) {
            return Float.toString((Float) column);
        }
        if (column.getClass() == Double.class) {
            return Double.toString((Double) column);
        }
        if (column.getClass() == Boolean.class) {
            return Boolean.toString((Boolean) column);
        }
        return (String) column;
    }

    @Override
    public Storeable createFor(Table table) {
        List<Class<?>> types = new ArrayList<>();
        for (int i = 0; i < table.getColumnsCount(); i++) {
            types.add(table.getColumnType(i));
        }
        return new MyStoreable(types);
    }

    @Override
    public Storeable createFor(Table table, List<?> values)
            throws ColumnFormatException, IndexOutOfBoundsException {
            List<Class<?>> types = new ArrayList<>();
            for (int i = 0; i < table.getColumnsCount(); i++) {
                types.add(table.getColumnType(i));
            }
            Storeable store = new MyStoreable(types);
            for (int i = 0; i < values.size(); i++) {
                store.setColumnAt(i, values.get(i));
            }
            return store;
    }

    @Override
    public List<String> getTableNames() {
        return this.tables;
    }

    private void removedat(Path tableDir) {
        File table = new File(tableDir.toString());
        File[] dirs = table.listFiles();
        for (File dir : dirs) {
            File[] fls = dir.listFiles();
            for (File fl : fls) {
                fl.delete();
            }
            dir.delete();
        }
    }
}
