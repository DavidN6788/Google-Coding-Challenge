package com.google;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;


public class VideoPlayer {
  private final VideoLibrary videoLibrary;
  /*
  Part 1 Variables
  */
  //Stores all videoIds
  private TreeSet<String> videoIDs = new TreeSet<>();
  // Stores whether the video is playing
  private boolean isPlaying = false;
  // Stores the current video playing
  private String currentVideoPlaying = null;
  // Stores the current video id
  private String currentVideoID = null;
  // Stores the current video tag
  private String currentVideoTag = null;
  // Store whether the video is paused
  private boolean isPaused = false;

  /*
  Part 2 Variables
  */
  // List of playlist names
  private final ArrayList<String> listNames = new ArrayList<>();
  // A class which for a playlist
  private VideoPlaylist playlist;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    addVideoIDs();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  // Add all video ids into a hashset
  public void addVideoIDs(){
    var videoLib = videoLibrary.getVideos();
    for(Video video: videoLib){ videoIDs.add(video.getVideoId()); }
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    for(String videos: videoIDs){
      String title = videoLibrary.getVideo(videos).getTitle();
      String ID = videoLibrary.getVideo(videos).getVideoId();
      String tags = videoLibrary.getVideo(videos).getTags().toString().replace(",", "");
      System.out.printf("%s (%s) %s\n", title,ID,tags);
    }
  }

  public void playVideo(String videoId) {
    // Unpause video
    isPaused = false;
    //Checks if video exists
    if(!videoIDs.contains(videoId)){
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    // Video exists!
    String videoTitle = videoLibrary.getVideo(videoId).getTitle();
    if(videoIDs.contains(videoId) && !isPlaying){
      System.out.printf("Playing video: %s\n", videoTitle);
      currentVideoPlaying = videoTitle;
      currentVideoID = videoId;
      currentVideoTag = videoLibrary.getVideo(videoId).getTags().toString().replace(",", "");
      isPlaying = true; // The video is now playing
    }else if(videoIDs.contains(videoId) && isPlaying){
      System.out.printf("Stopping video: %s\n", currentVideoPlaying);
      System.out.printf("Playing video: %s\n", videoTitle);
    }

  }

  public void stopVideo() {
    //if video is playing stop video
    if(isPlaying){
      System.out.printf("Stopping video: %s\n", currentVideoPlaying);
      isPlaying = false;
    }else{
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    var videoLib = videoLibrary.getVideos();
    // Convert HashSet to an array
    String[] arrayNumbers = videoIDs.toArray(new String[videoIDs.size()]);
    // Generate a random number
    Random rndm = new Random();
    // This will generate a random number between 0 and
    // HashSet.size - 1
    int rndmNumber = rndm.nextInt(videoIDs.size());
    playVideo(arrayNumbers[rndmNumber]);
  }

  public void pauseVideo() {
    // if video is playing AND it is not already paused
    if(isPlaying && !isPaused){
      System.out.printf("Pausing video: %s\n", currentVideoPlaying);
      isPaused = true; // Pause the video
    }
    // if video is playing and is paused
    else if(isPlaying && isPaused){
      System.out.printf("Video already paused: %s", currentVideoPlaying);
    }
    // if video is not playing
    else if(!isPlaying){
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    // If video is playing AND not paused cannot continue
    if(isPlaying && !isPaused){
      System.out.println("Cannot continue video: Video is not paused");
    }
    // if video is playing AND is paused, continue
    if(isPlaying && isPaused){
      System.out.printf("Continuing video: %s", currentVideoPlaying);
    }
    // if video is not playing
    if(!isPlaying){
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    // if no video is playing
    if(!isPlaying){
      System.out.println("No video is currently playing");
    }
    // if a video is playing but not paused
    if(isPlaying && !isPaused){
      System.out.printf("Currently playing: %s (%s) %s\n", currentVideoPlaying, currentVideoID, currentVideoTag);
    }
    // if a video is playing but paused
    if(isPlaying && isPaused){
      System.out.printf("Currently playing: %s (%s) %s - PAUSED\n", currentVideoPlaying, currentVideoID, currentVideoTag.toLowerCase());
    }
  }

  public void createPlaylist(String playlistName) {
    String name = playlistName.toLowerCase();
    if(listNames.contains(name)){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }
    listNames.add(name);
    System.out.printf("Successfully created new playlist: %s\n", playlistName);
    playlist = new VideoPlaylist(name);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    String name = playlistName.toLowerCase();
    // If ID exists and playlist name exists and name is not already added, add to playlist
    if(listNames.contains(name) && videoIDs.contains(videoId) && !playlist.alreadyAdded(videoId)) {
      playlist.addToPlayList(videoId);
      System.out.printf("Added video to %s: %s\n", playlistName, videoLibrary.getVideo(videoId).getTitle());
    }
    // If ID already exists and playlist name exists, cannot add to playlist
    else if(listNames.contains(name) && playlist.alreadyAdded(videoId)){
      System.out.printf("Cannot add video to %s: Video already added\n", playlistName);
    }
    // If ID does not exist and playlist name exist
    else if(listNames.contains(name) && !videoIDs.contains(videoId)){
      System.out.printf("Cannot add video to %s: Video does not exist\n", name);
    }
    // If playlist name does not exist and ID exist
    else if(!listNames.contains(name) || videoIDs.contains(videoId)){
      System.out.printf("Cannot add video to %s: Playlist does not exist\n", name);
    }
  }

  public void showAllPlaylists() {
    // If playlist does not exist
    if(listNames.isEmpty()){
      System.out.println("No playlists exist yet");
      return;
    }
    // Sort the names of the playlists
    Collections.sort(listNames);
    System.out.println("Showing all playlists:");
    for(String name : listNames){
      System.out.println(name);
    }
  }

  public void showPlaylist(String playlistName) {
    String name = playlistName.toLowerCase();
    //If playlist name exists
    if(listNames.contains(name)){
      System.out.printf("Showing playlist: %s\n", playlistName);
      playlist.printPlaylist();
    }
    //If playlist does not exist
    if(!listNames.contains(name)){
      System.out.printf("Cannot show playlist %s: Playlist does not exist\n", playlistName);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    String name = playlistName.toLowerCase();
    // If playlist exists and video id exists and video is not removed
    if(listNames.contains(name) && videoIDs.contains(videoId) && !playlist.alreadyRemoved(videoId)){
      playlist.removePlaylist(videoId);
      System.out.printf("Removed video from %s: %s\n", playlistName, videoLibrary.getVideo(videoId).getTitle());
    }
    // If ID already removed and playlist name exists, cannot remove from playlist
    else if(listNames.contains(name) && playlist.alreadyRemoved(videoId)){
      System.out.printf("Cannot remove video from %s: Video is not in playlist\n", playlistName);
    }
    // if playlist exist and video does not exist
    else if(listNames.contains(name) && !videoIDs.contains(videoId)){
      System.out.printf("Cannot remove video from %s: Video does not exist\n", playlistName);
    }
    // if playlist does not exist
    else if(!listNames.contains(name)){
      System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);
    }

  }

  public void clearPlaylist(String playlistName) {
    // Avoid case sensitivity
    String name = playlistName.toLowerCase();
    // If playlist exists, clear ids
    if(listNames.contains(name)){
      playlist.clearPlaylist();
      System.out.printf("Successfully removed all videos from %s\n", playlistName);
    }
    // If playlist name does not exist
    if(!listNames.contains(name)){
      System.out.printf("Cannot clear playlist %s: Playlist does not exist\n", playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    String name = playlistName.toLowerCase();
    // If playlist exists
    if(listNames.contains(name)){
      listNames.remove(playlistName);
      System.out.printf("Deleted playlist: %s\n", playlistName);
    }
    // If playlist does not exist
    if(!listNames.contains(name)){
      System.out.printf("Cannot delete playlist %s: Playlist does not exist\n", playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }

}