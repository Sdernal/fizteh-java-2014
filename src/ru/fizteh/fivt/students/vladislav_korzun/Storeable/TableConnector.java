package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;

public final class TableConnector {
    private TableProvider manager;
    private Table usedTable;
    
    public TableConnector(TableProvider manager) {
        this.manager = manager;
        usedTable = null;
    }
    
    public void setUsedTable(Table newUsedTable) {
        usedTable = newUsedTable;
    }
    
    public Table getUsedTable() {
        return usedTable;
    }
    
    public TableProvider getManager() {
        return manager;
    }
}
