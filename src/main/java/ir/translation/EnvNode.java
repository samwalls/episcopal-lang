package ir.translation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps labels to bound data
 * @param <T>
 */
public class EnvNode<T> {

    private EnvNode parent;

    private Map<String, EnvNode> children;

    public T value;

    public EnvNode(T value) {
        this.value = value;
        children = new HashMap<>();
    }

    public void add(String label, EnvNode child) {
        children.put(label, child);
        if (child != null)
            child.parent = this;
    }

    public void remove(String label) {
        EnvNode child = children.get(label);
        if (child != null)
            child.parent = null;
        children.remove(label);
    }

    public EnvNode get(String label) {
        return children.get(label);
    }

    public Set<String> keySet() {
        return children.keySet();
    }

    public boolean contains(String identifier) {
        return children.containsKey(identifier);
    }

    public EnvNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}
