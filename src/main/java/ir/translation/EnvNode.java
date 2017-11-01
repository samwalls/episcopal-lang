package ir.translation;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps labels to bound data
 * @param <T>
 */
public class EnvNode<T> {

    private Map<String, EnvNode> children;

    public T value;

    public EnvNode(T value) {
        this.value = value;
        children = new HashMap<>();
    }

    public void add(String label, EnvNode child) {
        children.put(label, child);
    }

    public void remove(String label) {
        children.remove(label);
    }

    public EnvNode get(String label) {
        return children.get(label);
    }
}
