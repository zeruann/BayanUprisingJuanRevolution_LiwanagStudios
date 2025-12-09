package Aa_05_GGameInfo;

public class StoryList {
	
	
    private StoryNode head;
    private StoryNode tail;
    public StoryNode current;

    public void addImage(String path) {
        StoryNode node = new StoryNode(path);

        if (head == null) {
            head = tail = current = node;
            return;
        }

        tail.next = node;
        node.prev = tail;
        tail = node;
    }
    

    public String nextImage() {
        if (current.next != null)
            current = current.next;

        return current.imagePath;
    }

    public String prevImage() {
        if (current.prev != null)
            current = current.prev;

        return current.imagePath;
    }

    public String getCurrent() {
        return current.imagePath;
    }
}
 



/*
// ========================================================================================
//        	CIRCULAR LINKEDLIST
// ========================================================================================    
        
        // First node → points to itself (circular)
        if (head == null) {
            head = tail = current = node;
            node.next = node;
            node.prev = node;
            return;
        }

        // Normal insertion at tail – but circular
        tail.next = node;
        node.prev = tail;

        node.next = head;
        head.prev = node;

        tail = node;
    }

    public String nextImage() {
        // No null check needed because it's circular
        current = current.next;
        return current.imagePath;
    }

    public String prevImage() {
        current = current.prev;
        return current.imagePath;
    }

    public String getCurrent() {
        return current.imagePath;
    }
    
    // ========================================================================================
    // ========================================================================================
    // ========================================================================================
    
    
    /*DOUBLY LINKEDLIST   
        public void addImage(String path) {
        StoryNode node = new StoryNode(path);

        


    */

