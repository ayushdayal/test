// Java program to implement the
// Compressed Trie

class Trie {

    // Root Node
    private final Node root = new Node(false);

    // 'a' for lower, 'A' for upper
    private final char CASE;

    // Default case
    public Trie() { CASE = 'a'; }

    // Constructor accepting the
    // starting symbol
    public Trie(char CASE)
    {
        this.CASE = CASE;
    }

    // Function to insert a word in
    // the compressed trie
    public void insert(String word)
    {
        // Store the root
        Node trav = root;
        int i = 0;

        // Iterate i less than word
        // length
        while (i < word.length()
                && trav.edgeLabel[word.charAt(i) - CASE]
                != null) {

            // Find the index
            int index = word.charAt(i) - CASE, j = 0;
            StringBuilder label = trav.edgeLabel[index];

            // Iterate till j is less
            // than label length
            while (j < label.length() && i < word.length()
                    && label.charAt(j) == word.charAt(i)) {
                ++i;
                ++j;
            }

            // If is the same as the
            // label length
            if (j == label.length()) {
                trav = trav.children[index];
            }
            else {

                // Inserting a prefix of
                // the existing word
                if (i == word.length()) {
                    Node existingChild
                            = trav.children[index];
                    Node newChild = new Node(true);
                    StringBuilder remainingLabel
                            = strCopy(label, j);

                    // Making "faceboook"
                    // as "face"
                    label.setLength(j);

                    // New node for "face"
                    trav.children[index] = newChild;
                    newChild
                            .children[remainingLabel.charAt(0)
                            - CASE]
                            = existingChild;
                    newChild
                            .edgeLabel[remainingLabel.charAt(0)
                            - CASE]
                            = remainingLabel;
                }
                else {

                    // Inserting word which has
                    // a partial match with
                    // existing word
                    StringBuilder remainingLabel
                            = strCopy(label, j);

                    Node newChild = new Node(false);
                    StringBuilder remainingWord
                            = strCopy(word, i);

                    // Store the trav in
                    // temp node
                    Node temp = trav.children[index];

                    label.setLength(j);
                    trav.children[index] = newChild;
                    newChild
                            .edgeLabel[remainingLabel.charAt(0)
                            - CASE]
                            = remainingLabel;
                    newChild
                            .children[remainingLabel.charAt(0)
                            - CASE]
                            = temp;
                    newChild
                            .edgeLabel[remainingWord.charAt(0)
                            - CASE]
                            = remainingWord;
                    newChild
                            .children[remainingWord.charAt(0)
                            - CASE]
                            = new Node(true);
                }

                return;
            }
        }

        // Insert new node for new word
        if (i < word.length()) {
            trav.edgeLabel[word.charAt(i) - CASE]
                    = strCopy(word, i);
            trav.children[word.charAt(i) - CASE]
                    = new Node(true);
        }
        else {

            // Insert "there" when "therein"
            // and "thereafter" are existing
            trav.isEnd = true;
        }
    }

    // Function that creates new String
    // from an existing string starting
    // from the given index
    private StringBuilder strCopy(
            CharSequence str, int index)
    {
        StringBuilder result
                = new StringBuilder(100);

        while (index != str.length()) {
            result.append(str.charAt(index++));
        }

        return result;
    }

    // Function to print the Trie
    public void print()
    {
        printUtil(root, new StringBuilder());
    }

    // Fuction to print the word
    // starting from the given node
    private void printUtil(
            Node node, StringBuilder str)
    {
        if (node.isEnd) {
            System.out.println(str);
        }

        for (int i = 0;
             i < node.edgeLabel.length; ++i) {

            // If edgeLabel is not
            // NULL
            if (node.edgeLabel[i] != null) {
                int length = str.length();

                str = str.append(node.edgeLabel[i]);
                printUtil(node.children[i], str);
                str = str.delete(length, str.length());
            }
        }
    }

    // Function to search a word
    public boolean search(String word)
    {
        int i = 0;

        // Stores the root
        Node trav = root;

        while (i < word.length()
                && trav.edgeLabel[word.charAt(i) - CASE]
                != null) {
            int index = word.charAt(i) - CASE;
            StringBuilder label = trav.edgeLabel[index];
            int j = 0;

            while (i < word.length()
                    && j < label.length()) {

                // Character mismatch
                if (word.charAt(i) != label.charAt(j)) {
                    return false;
                }

                ++i;
                ++j;
            }

            if (j == label.length() && i <= word.length()) {

                // Traverse further
                trav = trav.children[index];
            }
            else {

                // Edge label is larger
                // than target word
                // searching for "face"
                // when tree has "facebook"
                return false;
            }
        }

        // Target word fully traversed
        // and current node is word
        return i == word.length() && trav.isEnd;
    }

    // Function to search the prefix
    public boolean startsWith(String prefix)
    {
        int i = 0;

        // Stores the root
        Node trav = root;

        while (i < prefix.length()
                && trav.edgeLabel[prefix.charAt(i) - CASE]
                != null) {
            int index = prefix.charAt(i) - CASE;
            StringBuilder label = trav.edgeLabel[index];
            int j = 0;

            while (i < prefix.length()
                    && j < label.length()) {

                // Character mismatch
                if (prefix.charAt(i) != label.charAt(j)) {
                    return false;
                }

                ++i;
                ++j;
            }

            if (j == label.length()
                    && i <= prefix.length()) {

                // Traverse further
                trav = trav.children[index];
            }
            else {

                // Edge label is larger
                // than target word,
                // which is fine
                return true;
            }
        }

        return i == prefix.length();
    }
}

// Node class
class Node {

    // Number of symbols
    private final static int SYMBOLS = 26;
    Node[] children = new Node[SYMBOLS];
    StringBuilder[] edgeLabel = new StringBuilder[SYMBOLS];
    boolean isEnd;

    // Function to check if the end
    // of the string is reached
    public Node(boolean isEnd)
    {
        this.isEnd = isEnd;
    }
}

class GFG {

    // Driver Code
    public static void main(String[] args)
    {
        Trie trie = new Trie();

        // Insert words
        trie.insert("facebook");
        trie.insert("face");
        trie.insert("this");
        trie.insert("there");
        trie.insert("then");

        // Print inserted words
        trie.print();

        // Check if these words
        // are present or not
        System.out.println(
                trie.search("there"));
        System.out.println(
                trie.search("therein"));
        System.out.println(
                trie.startsWith("th"));
        System.out.println(
                trie.startsWith("fab"));
    }
}