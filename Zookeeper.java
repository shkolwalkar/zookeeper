package zookeeper;

import java.util.*;
import java.io.*;
import java.nio.*;

// main class where things happen
public class Zookeeper {
   static Tree mainTree; // main decision tree that the zookeeper uses
   static Node root; // root of said tree
   static Scanner userInput = new Scanner(System.in); // scanner for user input
   static boolean userLovesMe = false; // should the zookeeper keep asking the user for animals?
   
   static File store;



   public static void main(String[] args) {
      execute();
   }
   
   public static void execute() {
      System.out.println("Welcome to the Zookeeper!");
      System.out.println("Would you like to add data to the zookeeper's book of information?");
      System.out.println("Type YES or NO");
      String addInfo = userInput.nextLine();
      addInfo = addInfo.toUpperCase();
      
      if (addInfo.equals("YES")) {
         userLovesMe = true;
      }
      
      // if the "storage.txt" file (which stores the decision tree) exists, read it from the file
      // otherwise, start a new tree
      String dir = System.getProperty("user.dir");
      store = new File(dir + "/zookeeper/storage.txt");
      if (!store.exists()) {
         root = new QuestionNode("Does the animal have fur?");
         mainTree = new Tree(root);
      }
      else {
         readFromFile();
      }
      
      // keep adding animals until user doesn't want to anymore
      while (userLovesMe) {
         addQuestion();
         
         System.out.println("Would you like to add data to the zookeeper's book of information?");
         System.out.println("Type YES or NO");
         String moreInfo = userInput.nextLine();
         moreInfo = moreInfo.toUpperCase();
         
         if(moreInfo.equals("NO")) {
            userLovesMe = false;
         }
      }
      
      // writing the information the user provided to a file
      System.out.println("Saving the info you've already given me...");
      writeToFile();
      
      // closing the session
      System.out.println("Thank you for adding information to the zookeeper!");
      System.exit(0);

   }
   
   // method used to add a question
   public static void addQuestion() {
      System.out.println("I'd like you to think of an animal.");
      Node current = root; // current node that the below iterator is own
      Node lastCurrent = root; // the last node
      boolean lastDirection = false; // the direction taken to get from the last node to the current one
      while (current instanceof QuestionNode) { // while we're going through QuestionNode...
         boolean r = askQuestion(current); // ask user a question
         lastCurrent = current; // current node -> last node
         if (r) { // if the answer to the question was yes
            lastDirection = true; // go to the right child of the last node (the new current)
            if (current.right == null) {
               break;
            }
            else {
               current = current.right;
            }
         }
         else { // if the answer to the question was no
            lastDirection = false; // go to the left child of the last node (the new current)
            if (current.left == null) {
               break;
            }
            else {
               current = current.left;
            }
         }
      }
      if (current instanceof AnimalNode) { // once we get to the animal nodes (the bottom-most nodes of the tree)
         // evaluate whether the zookeeper was right
         System.out.println("Is the animal a " + current.get() + "?");
         System.out.println("Type YES or NO");
         String isRightAnimal = userInput.nextLine();
         isRightAnimal = isRightAnimal.toUpperCase();
         if (isRightAnimal.equals("NO")) { // if the zookeeper was wrong
            System.out.println("What animal were you thinking of?");
            String realAnimal = userInput.nextLine();
            
            // obtain the new animal, then find a question that would distinguish the zookeeper's guess from the correct animal (the one the user thought of)
            AnimalNode newAnimal = new AnimalNode(realAnimal);
            System.out.println("What is a question that would distinguish " + newAnimal.get() + " and " + current.get() + "?");
            String distinguisher = userInput.nextLine();
            
            // add the question where the zookeeper's guess once was
            Node newQuestion = new QuestionNode(distinguisher);
            if (lastDirection) {
               lastCurrent.right = newQuestion;
            }
            else {
               lastCurrent.left = newQuestion;
            }
            
            // add the animals based on what a user thinking of the correct animal would answer to the distinguishing question
            // if the correct animal says, it becomes the right child, and vice versa
            System.out.println("What would someone thinking of a " + newAnimal.get() + " answer to the question you provided?");
            System.out.println("(Something thinking of a " + current.get() + " would respond THE OPPOSITE WAY to this question)");
            System.out.println("Type YES or NO");
            
            String newAnimalAnswer = userInput.nextLine();
            newAnimalAnswer = newAnimalAnswer.toUpperCase();
            
            if (newAnimalAnswer.equals("YES")) {
               newQuestion.right = newAnimal;
               newQuestion.left = current;
            }
            else {
               newQuestion.right = current;
               newQuestion.left = newAnimal;
            } 
         }
         else {
            // if the zookeeper's guess is right
            System.out.println("Glad I could help you.");
            System.out.println("But I still need more data to ensure the accuracy of my model...");
         }
      }
      else {
         // if the 
         System.out.println("Dagnabbit! I don't have more questions for you...");
         System.out.println("What animal were you thinking of?");
         String realAnimal = userInput.nextLine();            
         AnimalNode newAnimal = new AnimalNode(realAnimal);
         System.out.println("A " + newAnimal.get() + ", I see...");
         if (lastDirection) {
            current.right = newAnimal;
         }
         else {
            current.left = newAnimal;
         }
      }
         
   }
   
   public static boolean askQuestion(Node n) {
      if (n instanceof QuestionNode) {
         boolean response = false;
         System.out.println(n.get());
         System.out.println("Type YES or NO");
         String yesno = userInput.nextLine();
         yesno = yesno.toUpperCase();
         if (yesno.equals("YES")) {
            response = true;
         }
         return response;
      }
      else return false;
   }
   
   public static void writeToFile() {
      try {
         String dir = System.getProperty("user.dir");
         store = new File(dir + "/zookeeper/storage.txt");
         System.out.println("Store file created");
         FileOutputStream fileStream = new FileOutputStream(store);
         ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
      
         objStream.writeObject(mainTree);
      
         objStream.close();
         fileStream.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void readFromFile() {
      try {
         FileInputStream fileStream = new FileInputStream(store);
         ObjectInputStream objStream = new ObjectInputStream(fileStream);
         
         mainTree = (Tree)objStream.readObject();
         root = mainTree.root;
         
         System.out.println("Tree back in system!");
      
         objStream.close();
         fileStream.close();        
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

}