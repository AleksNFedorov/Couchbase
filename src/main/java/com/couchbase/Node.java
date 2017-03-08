package com.couchbase;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of single graph node
 *
 * @author Aleks
 */
public class Node {

    private final String id;
    private final List<Node> children;


    public Node(String id) {
        checkNotNull(id, "Node id must be specified");
        this.id = id;
        children = new ArrayList<>();
    }

    public void addChild(Node childNode) {
        checkNotNull(childNode, "Child node must not be null");
        children.add(childNode);
    }

    public ImmutableList<Node> getChildren() {
        return ImmutableList.copyOf(children);
    }

    public String getId() {
        return id;
    }

    //TODO(aleks) consider to replace with code generation
    //equals
    //hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id != null ? id.equals(node.id) : node.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
