package ru.csc.bdse.kv;

/**
 * Represent node information
 *
 * @author alesavin
 */
public class NodeInfo {

    private String name;
    private NodeStatus status;

    public NodeInfo(String name, NodeStatus status) {
        this.name = name;
        this.status = status;
    }

    public NodeInfo() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public NodeStatus getStatus() {
        return status;
    }
}
