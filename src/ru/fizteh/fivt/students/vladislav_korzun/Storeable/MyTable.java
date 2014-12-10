package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;

public class MyTable implements Table {
    
    private TableProvider provider;
    private String name;
    private Path path;
    private Map<String, Storeable> filemap;
    private Map<String, Storeable> buffermap;
    private List<Class<?>> structure;
    
    MyTable(Path currentTablePath, String currentTable, List<Class<?>> columnTypes) {
        filemap = new HashMap<>();
        Map<String, String>  mapXML = new HashMap<>();
        this.path = currentTablePath;
        this.name = currentTable;
        this.structure = columnTypes;
        FileManager filemanager = new FileManager(); 
        filemanager.readTable(this.path);
        mapXML = filemanager.filemap;
        for (Entry<String, String> entry : mapXML.entrySet()) {
            try {
            Storeable val = provider.deserialize(this, entry.getValue());
            filemap.put(entry.getKey(), val);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        this.buffermap = this.filemap;
    }
    public MyTable(Path currentTablePath, String currentTable) {
        Map<String, String> mapXML = new HashMap<>();
        this.path = currentTablePath;
        this.name = currentTable;
        FileManager filemanager = new FileManager(); 
        filemanager.readTable(this.path);
        mapXML = filemanager.filemap;
        List<Class<?>> columns = new ArrayList<>();
        for (Entry<String, String> entry : mapXML.entrySet()) {
            try {
                MyStoreable val = (MyStoreable) provider.deserialize(this, entry.getValue());
                for (int i = 0; i < val.size(); i++) {
                    columns.add(val.getClass());
                }
                break;
            } catch (ParseException e) {
                e.printStackTrace();
            }           
        }
        this.structure = columns;
        for (Entry<String, String> entry : mapXML.entrySet()) {
            try {
            Storeable val = provider.deserialize(this, entry.getValue());
            filemap.put(entry.getKey(), val);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        this.buffermap = this.filemap;
    }
    @Override
    public String getName() {
        if (this.name != null) {
            return name;
        }
        return null;
    }

    @Override
    public Storeable get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        Storeable val = filemap.get(key);
        if (val == null) {
            return null;
        } else {
            return val;
        }
    }

    @Override
    public Storeable put(String key, Storeable value)
            throws ColumnFormatException {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or Value is null");
        } 
        try {
            for (int i = 0; i < structure.size(); i++) {
                if (value.getColumnAt(i) != null && structure.get(i) != value.getColumnAt(i).getClass()) {
                    throw new ColumnFormatException("Storeable has a wrong "
                            + "column format");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnFormatException("Storeable has a wrong "
                    + "column format: " + e.getMessage(), e);
        }
        Storeable oldval = this.filemap.put(key, value); 
        if (oldval == null) {
            return null; 
        } else {
            return oldval;
        }
    }

    @Override
    public Storeable remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        Storeable val = filemap.remove(key);
        if (val == null) {
            return null; 
        } else {
            return val;
        }
    }

    @Override
    public int size() {
        return this.filemap.size();
    }

    @Override
    public List<String> list() {
        Set<String> keys = filemap.keySet();
        List<String> keylist = new ArrayList<String>();
        for (String key : keys) {
            keylist.add(key);
        }
        return keylist;
    }

    @Override
    public int commit() throws IOException {
        FileManager filemanager = new FileManager();
        Map<String, String> mapXML = new HashMap<>();
        for (Entry<String, Storeable> entry : this.filemap.entrySet()) {
            try {
                String val = provider.serialize(this, entry.getValue());
                mapXML.put(entry.getKey(), val);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        filemanager.filemap = mapXML;
        filemanager.writeTable(this.path);
        this.buffermap = this.filemap;
        return 0;
    }

    @Override
    public int rollback() {
        this.filemap = this.buffermap;
        return 0;
    }

    @Override
    public int getNumberOfUncommittedChanges() {
        return this.filemap.size() - this.buffermap.size();
    }

    @Override
    public int getColumnsCount() {
        return this.structure.size();
    }

    @Override
    public Class<?> getColumnType(int columnIndex)
            throws IndexOutOfBoundsException {
        if (columnIndex >= this.structure.size() || columnIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid column index.");
        }
        return this.structure.get(columnIndex).getClass();
    }

}
