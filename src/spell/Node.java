package spell;

public class Node implements INode {
    private int count;
    private Node[] children;

    public Node() {
        children = new Node[26];
    }

    public int getValue() {
        return count;
    }
    public void incrementValue() {
        count++;
    }
    public Node[] getChildren() {
        return children;
    }
}