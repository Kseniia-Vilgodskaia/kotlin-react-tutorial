fun filterVideos(
    videos: List<VideoEntry>,
    query: String,
    isFavouritesChosen: Boolean
): List<VideoEntry> {
    var filteredVideos: List<VideoEntry> = videos
    if (isFavouritesChosen) {
        filteredVideos = videos.filter { it.isFavourite }
    }

    return filteredVideos.filter { videoEntry: VideoEntry ->
        videoEntry.video.run {
            this.title.lowercase().contains(query.lowercase()) || this.speaker.lowercase().contains(query.lowercase())
        }
    }
}