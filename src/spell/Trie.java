package spell;

public class Trie implements ITrie {
    private Node root;
    private int wordCount;
    private int nodeCount = 1 ;

    public Trie() {
        root = new Node();
    }

    @Override
    public void add(String word) {
        String lowerWord = word.toLowerCase();
        char[] chars = lowerWord.toCharArray();
        Node currentNode = root;
        for (int i = 0; i < chars.length; i++) {
            int index = chars[i] - 'a';
            Node[] children = currentNode.getChildren();
            //If node does not exist create one
            if (children[index] == null) {
                children[index] = new Node();
                nodeCount++;
            }
            //Sets created node to current node for the next char
            currentNode = children[index];
        }
        //If node's value is greater than 0 meaning there isn't a word than ended there before,
        if (currentNode.getValue() < 1) {
            wordCount++;
        }
        //Increment the node's value signaling the end of the new word.
        currentNode.incrementValue();
    }
    @Override
    public Node find(String word) {
        String lowerWord = word.toLowerCase();
        char[] chars = lowerWord.toCharArray();
        Node currentNode = root;
        for (int i = 0; i < chars.length; i++) {
            int index = chars[i] - 'a';
            if (currentNode.getChildren()[index] == null) {
                return null;
            }
            currentNode = currentNode.getChildren()[index];
        }
        if (currentNode.getValue() != 0) {
            return currentNode;
        }
        return null;
    }
    @Override
    public int getWordCount() {
        return wordCount;
    }
    @Override
    public int getNodeCount() {
        return nodeCount;
    }
    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder words = new StringBuilder();
        //Recursive function
        toStringTraversal(root, word, words);
        return words.toString();
    }
    @Override
    public int hashCode() {
        int finalHash = 0;
        finalHash += wordCount * 31;
        finalHash += nodeCount * 31;
        Node[] nodeChildren = root.getChildren();
        for (int i = 0; i < nodeChildren.length; i++) {
            if (nodeChildren[i] != null) {
                finalHash += i * 31;
                finalHash += nodeChildren[i].getValue() * 31;
            }
        }
        return finalHash;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Trie)) {
            return false;
        }
        Trie param = (Trie) o;

        //Compare nodeCount and wordCount
        if (wordCount != param.wordCount || nodeCount != param.nodeCount) {
            return false;
        }
        //Compare nodes
        return compareChildren(root, param.root);
    }

    private void toStringTraversal(Node node, StringBuilder word, StringBuilder words) {
        //If node is the end of a word
        if (node.getValue() > 0) {
            words.append(word.toString());
            words.append('\n');
        }
        //Goes through each child of the current node and searches their children
        Node[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                word.append((char)('a'+i));
                toStringTraversal(children[i], word, words);
                word.deleteCharAt(word.length() - 1);
            }
        }
    }


    private boolean compareChildren(Node thisNode, Node paramNode ) {
        if ((thisNode == null && paramNode != null) || (thisNode != null && paramNode == null)) {
            return false;
        }
        if (thisNode == null && paramNode == null) {
            return true;
        }
        if (thisNode.getValue() != paramNode.getValue()) {
            return false;
        }
        for (int i = 0; i < thisNode.getChildren().length; i++) {
            if (!compareChildren(thisNode.getChildren()[i], paramNode.getChildren()[i])) {
                return false;
            }
        }
        return true;
    }
}