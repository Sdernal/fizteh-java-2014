package ru.fizteh.fivt.students.ivan_ivanov.multifilehashmap;

import ru.fizteh.fivt.students.ivan_ivanov.shell.Command;
import ru.fizteh.fivt.students.ivan_ivanov.shell.Shell;

import java.io.IOException;
import java.util.Set;

public class CmdDrop implements Command {

    @Override
    public final String getName() {

        return "drop";
    }

    @Override
    public final void executeCmd(final Shell shell, final String[] args) throws IOException {

    	if (1 == ((MultiFileHashMap) shell).getMFHMState().getFlag()) {
    		if (((MultiFileHashMap) shell).getMFHMState().getCurrentTable().getName().equals(args[0])) {
    			Set<String> keys = ((MultiFileHashMap) shell).getMFHMState().
    					getDataBaseFromCurrentTable().keySet();
    			for (String key : keys) {
    				((MultiFileHashMap) shell).getMFHMState().removeFromCurrentTable(key);
    				}
        		((MultiFileHashMap) shell).getMFHMState().setCurrentTable();
        		((MultiFileHashMap) shell).getMFHMState().changeFlag();
    		}
    	}


        if (((MultiFileHashMap) shell).getMFHMState().getTable(args[0]) == null) {
            System.out.println(args[0] + " not exists");
            return;
        } else {
            ((MultiFileHashMap) shell).getMFHMState().deleteTable(args[0]);
            System.out.println("dropped");
        }
    }
}

