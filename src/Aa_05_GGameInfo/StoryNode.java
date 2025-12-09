package Aa_05_GGameInfo;

class StoryNode {
    StoryNode next;
    StoryNode prev;
    String imagePath;

    public StoryNode(String path) {
        this.imagePath = path;
    }
}

