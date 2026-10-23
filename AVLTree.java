
import java.util.Scanner;

class AVLNode {
    int accountNumber;
    int height;
    AVLNode left, right;

    AVLNode(int accountNumber) {
        this.accountNumber = accountNumber;
        this.height = 1;
    }
}

public class AVLTree {
    static AVLNode root = null;
    static Scanner sc = new Scanner(System.in);

    static int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    static int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    static AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode temp = x.right;

        x.right = y;
        y.left = temp;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    static AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode temp = y.left;

        y.left = x;
        x.right = temp;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }

    // Insert an account number and balance the tree
    static AVLNode insert(AVLNode node, int accountNumber) {
        if (node == null) {
            return new AVLNode(accountNumber);
        }

        if (accountNumber < node.accountNumber) {
            node.left = insert(node.left, accountNumber);
        } else if (accountNumber > node.accountNumber) {
            node.right = insert(node.right, accountNumber);
        } else {
            System.out.println("Account number already exists.");
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Left-Left case
        if (balance > 1 && accountNumber < node.left.accountNumber) {
            return rightRotate(node);
        }

        // Right-Right case
        if (balance < -1 && accountNumber > node.right.accountNumber) {
            return leftRotate(node);
        }

        // Left-Right case
        if (balance > 1 && accountNumber > node.left.accountNumber) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left case
        if (balance < -1 && accountNumber < node.right.accountNumber) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    static boolean search(AVLNode node, int accountNumber) {
        if (node == null) {
            return false;
        }

        if (node.accountNumber == accountNumber) {
            return true;
        }

        if (accountNumber < node.accountNumber) {
            return search(node.left, accountNumber);
        }

        return search(node.right, accountNumber);
    }

    static AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    // Delete an account number and rebalance the tree
    static AVLNode delete(AVLNode node, int accountNumber) {
        if (node == null) {
            return null;
        }

        if (accountNumber < node.accountNumber) {
            node.left = delete(node.left, accountNumber);
        } else if (accountNumber > node.accountNumber) {
            node.right = delete(node.right, accountNumber);
        } else {
            if (node.left == null || node.right == null) {
                AVLNode child = (node.left != null) ? node.left : node.right;

                if (child == null) {
                    return null;
                } else {
                    node = child;
                }
            } else {
                AVLNode successor = minValueNode(node.right);
                node.accountNumber = successor.accountNumber;
                node.right = delete(node.right, successor.accountNumber);
            }
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Left-Left case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left-Right case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Right case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right-Left case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Display account numbers in sorted order
    static void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.accountNumber + " ");
            inorder(node.right);
        }
    }

    public static void main(String[] args) {
        int choice = 0;

        do {
            System.out.println("\n===== BANK AVL TREE SYSTEM =====");
            System.out.println("1. Insert Account Number");
            System.out.println("2. Search Account Number");
            System.out.println("3. Delete Account Number");
            System.out.println("4. Display Accounts (Sorted)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    root = insert(root, sc.nextInt());
                    System.out.println("Insertion operation completed.");
                    break;

                case 2:
                    System.out.print("Enter account number to search: ");
                    int searchNumber = sc.nextInt();

                    if (search(root, searchNumber)) {
                        System.out.println("Account number found.");
                    } else {
                        System.out.println("Account number not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter account number to delete: ");
                    int deleteNumber = sc.nextInt();

                    if (search(root, deleteNumber)) {
                        root = delete(root, deleteNumber);
                        System.out.println("Account deleted and tree balanced.");
                    } else {
                        System.out.println("Account number not found.");
                    }
                    break;

                case 4:
                    System.out.print("Account numbers in sorted order: ");
                    inorder(root);
                    System.out.println();
                    break;

                case 5:
                    System.out.println("Exiting AVL Tree System.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }
}
