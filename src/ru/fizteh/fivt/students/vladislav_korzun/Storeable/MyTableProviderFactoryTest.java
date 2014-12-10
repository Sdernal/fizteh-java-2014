package ru.fizteh.fivt.students.vladislav_korzun.Storeable;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class MyTableProviderFactoryTest {

    private MyTableProviderFactory factory = new MyTableProviderFactory();

    @Test(expected = IllegalArgumentException.class)
    public void createNullProvider() throws IOException {
        factory.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmptyProvider() throws IOException {
        factory.create("");
    }   

    @Test(expected = IllegalArgumentException.class)
    public void createProviderInFile() throws IOException {
        File f = new File(System.getProperty("fizteh.db.dir") + File.separator + "fail.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.err.println("Can't create file " + f.getName());
            throw new IllegalArgumentException();
        }
        factory.create(f.getName());
    }   

    @Test
    public void createProvider() throws IOException {
        factory.create(System.getProperty("fizteh.db.dir"));
    }

    @After
    public void clear() {
        try {
            File testDir = new File(System.getProperty("fizteh.db.dir"));
            for (File curDir : testDir.listFiles()) {
                for (File file : curDir.listFiles()) {
                    file.delete();
                }
                curDir.delete();
            }
            testDir.delete();
        } catch (NullPointerException e) {
            //if it happens, than it means, that we were trying to create something in other directory;
        }

    }

}
