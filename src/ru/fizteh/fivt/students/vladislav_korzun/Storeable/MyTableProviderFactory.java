package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.io.IOException;

import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.storage.structured.TableProviderFactory;

public class MyTableProviderFactory implements TableProviderFactory {
    
    public MyTableProviderFactory() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public TableProvider create(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Directory name is null");
        }
        return new MyTableProvider(path);
    }

}
