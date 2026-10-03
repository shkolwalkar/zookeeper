package zookeeper;

// this class is for the nodes of the tree
class Node implements java.io.Serializable {
   // each parent node has up to two children
   public Node left;
   public Node right;
   
   // generalized constructor
   public Node(Node left, Node right) {
      this.left = left;
      this.right = right;
   }
   
   // generalized get method
   public String get() {
      return "";
   }
   
   // this class is only used for polymorphism, never actually instantiated   
}

class QuestionNode extends Node {
   // subclass for nodes that signify a question
   private String question;
   
   public QuestionNode(String question, Node left, Node right) { // constructor asking for an input question
      super(left,right);
      this.question = question;
   }
   
   QuestionNode(String question) { // input question without children
      super(null,null);
      this.question = question;
   }
   
   public String get() { // get method returns question
      return question;
   }
}

class AnimalNode extends Node {
   // subclass for nodes that have an animal
   private String animal;
   
   AnimalNode(String animal) { // constructor asks for an input animal
      super(null,null);
      this.animal = animal;
   }
   
   public String get() { // get method returns the animal
      return animal;
   }
}