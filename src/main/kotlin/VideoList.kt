import react.FC
import react.Props
import react.dom.html.ReactHTML.p
import react.key

external interface VideoListProps : Props {
    var videos: List<VideoEntry>
    var selectedVideo: VideoEntry?
    var onSelectVideo: (VideoEntry) -> Unit
}

val VideoList = FC<VideoListProps> { props ->
    for (videoEntry in props.videos) {
        p {
            key = videoEntry.video.id.toString()
            onClick = {
                props.onSelectVideo(videoEntry)
            }
            if (videoEntry == props.selectedVideo) {
                +"▶ "
            }
            if (videoEntry.isFavourite) {
                +"★"
            }
            +"${videoEntry.video.speaker}: ${videoEntry.video.title}"
        }
    }
}