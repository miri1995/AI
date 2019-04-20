import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class DTree {



    private PrintWriter writer;
    private Node root;
    private List<String> treeToPrint;
    private List<String> treeToWrite;
    private List<Node> temp;


    public DTree() {
        this.treeToPrint = new ArrayList<String>();
        this.treeToWrite = new ArrayList<String>();
        this.temp=new ArrayList<>();

    }


    public void setRoot(Node root) {
        this.root = root;
        this.temp.add(root);
    }


    public Node getRoot() {
        return this.root;
    }


    public void printTreeToFile() {
        try {
            this.writer = new PrintWriter("output_tree.txt");
            recursiveWrite(root, 0);
            if (treeToPrint.get(treeToPrint.size()-1).equals("\n")){
                treeToPrint.remove(treeToPrint.size()-1);
            }
            for (int i = 0; i < treeToPrint.size(); i++) {
                writer.print(treeToPrint.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (this.writer != null) {
                writer.close();
            }
        }
    }


    public void recursiveWrite(Node node, int depth) {
        if (node.isLeaf()) {
            treeToPrint.add(":"+node.getData()+"\n");

        } else {
            SortedSet<String> sortedChildren = new TreeSet<>(node.getChildren().keySet());
          //  System.out.println(sortedChildren);
            for (String branch : sortedChildren) {
                for (int i = 0; i < depth; i++) {
                    treeToPrint.add("\t");
                    this.temp.add(new Node(branch));
                }

                if (node.getParent()!=null) {
                    treeToPrint.add("|");
                }
                treeToPrint.add(node.getData()+ "=" + branch);

                Node childNode = node.getChildren().get(branch);
                if (!childNode.isLeaf()) {
                    treeToPrint.add("\n");
                }
                recursiveWrite(childNode, depth + 1);
            }
        }
    }





}
