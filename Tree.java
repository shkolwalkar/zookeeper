package zookeeper;

import java.io.*;

class Tree implements java.io.Serializable {
   public Node root;
   
   public Tree(Node r) {
      root = r;
   }
}