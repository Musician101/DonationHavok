package io.musician101.donationhavok.gui.tree;

import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class HavokBranchingTreeNode<J extends JsonElement> extends HavokTreeNode<J> {

    final List<TreeNode> children = new ArrayList<>();

    HavokBranchingTreeNode(@Nullable String key, @Nonnull J json) {
        super(key);
        parseJson(json);
    }

    public void add(@Nonnull MutableTreeNode child) {
        MutableTreeNode oldParent = (MutableTreeNode) child.getParent();
        if (oldParent != null) {
            oldParent.remove(child);
        }

        child.setParent(this);
        children.add(child);
    }

    @Override
    public Enumeration children() {
        return new Enumeration<TreeNode>() {

            int count = 0;

            @Override
            public boolean hasMoreElements() {
                return count < children.size();
            }

            @Override
            public TreeNode nextElement() {
                synchronized (HavokBranchingTreeNode.this) {
                    if (hasMoreElements()) {
                        return getChildAt(count++);
                    }
                }

                throw new NoSuchElementException("HavokTreeNode Enumeration");
            }
        };
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        if (children.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public void insert(@Nonnull MutableTreeNode child, int index) {
        MutableTreeNode oldParent = (MutableTreeNode) child.getParent();
        if (oldParent != null) {
            oldParent.remove(child);
        }

        child.setParent(this);
        children.add(index, child);
    }

    protected abstract void parseJson(J json);

    @Override
    public void remove(int index) {
        children.remove(index);
    }

    @Override
    public void remove(@Nonnull MutableTreeNode node) {
        children.remove(node);
    }

    @Override
    public String toString() {
        return (getKey() == null ? "" : getKey() + ": ") + getChildCount() + " " + (getChildCount() == 1 ? "Entry" : "Entries");
    }
}
