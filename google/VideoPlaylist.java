package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private final VideoLibrary videoLibrary;
    // Stores the name of the playlist
    private final String playListName;
    // Store the ids inside the playlists
    private ArrayList<String> playListIDs = new ArrayList<>();

    public VideoPlaylist(String playListName){
        this.videoLibrary = new VideoLibrary();
        this.playListName = playListName;
    }

    // Add id to the playlist
    public void addToPlayList(String ID){
        playListIDs.add(ID);
    }

    // Checks whether the id was already added
    public boolean alreadyAdded(String ID){
        if(playListIDs.contains(ID)){ return true; }
        if(!playListIDs.contains(ID)){ return false; }
        return false;
    }

    // Prints the playlist
    public void printPlaylist(){
        // If there are no videos
        if(playListIDs.size() == 0){
            System.out.println("No videos here yet");
        } else{
            for(String videos: playListIDs){
                String title = videoLibrary.getVideo(videos).getTitle();
                String ID = videoLibrary.getVideo(videos).getVideoId();
                String tags = videoLibrary.getVideo(videos).getTags().toString().replace(",", "");
                System.out.printf("%s (%s) %s\n", title,ID,tags);
            }
        }
    }

    // Clears playlist
    public void clearPlaylist(){
        playListIDs.clear();
    }

    //Remove from playlist
    public void removePlaylist(String ID){
        playListIDs.remove(ID);
    }

    // Checks whether an ID is already removed
    public boolean alreadyRemoved(String ID){
        if(playListIDs.contains(ID)){ return false; }
        if(!playListIDs.contains(ID)){ return true; }
        return true;
    }
}
