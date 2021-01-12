package de.haw.tree;

public class Tree {
    private final Node<?> root;
    private Node<?> selectedNode;

    public Tree(Node<?> root) {
        this.root = root;
    }

    public Node<?> getRoot() {
        return root;
    }

    public Node<?> getSelectedNode() {
        return selectedNode;
    }

    public void selectNode(Node<?> node) {
        selectedNode = node;
    }

    public void selectNextNode() {
        // TODO: Select next node via BFS
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("Tree{");
        toString(root, 1, sb);
        return sb.toString();
    }

    private void toString(Node<?> node, int tab, StringBuilder sb) {
        if (node == null || node.getData() == null) return;
        sb.append(System.lineSeparator());
        for (int i = 0; i <= tab; i++) {
            sb.append("\t");
        }
        sb.append(node);
        tab++;
        for (var c : node.getChildren()) {
            toString(c, tab, sb);
        }
    }
}
