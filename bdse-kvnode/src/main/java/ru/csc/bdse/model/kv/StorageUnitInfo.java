package ru.csc.bdse.model.kv;

import ru.csc.bdse.util.Require;

/**
 * @author semkagtn
 */
public class StorageUnitInfo {

    private final String name;
    private final StorageUnitState state;

    public StorageUnitInfo(final String name, final StorageUnitState state) {
        Require.nonNull(name, "null name");
        Require.nonNull(state, "null state");
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public StorageUnitState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageUnitInfo)) return false;

        StorageUnitInfo that = (StorageUnitInfo) o;

        if (!getName().equals(that.getName())) return false;
        return getState() == that.getState();
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getState().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StorageUnitInfo{" +
                "name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
