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

    /**
     * @param localLabel the name of the label relative to its environment
     * @return a globally unique label for the given label, given the current environment
     * @throws LabelNotFoundException if the label is not part of this environment
     */
    public String canonicalLabel(String localLabel) throws LabelNotFoundException{
        if (localLabel == null)
            throw new LabelNotFoundException("label is null");
        if (!children.containsKey(localLabel))
            throw new LabelNotFoundException("label " + localLabel + " is not contained in the environment" + (value != null ? " \"" + value.toString() + "\"" : ""));
        String label = localLabel;
        for (EnvNode node = this; node != null; node = node.getParent()) {
            label = node.value.toString() + "_" + label;
        }
        return label;
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
