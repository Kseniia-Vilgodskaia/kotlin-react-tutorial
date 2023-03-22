import csstype.FontWeight
import csstype.Position
import csstype.px
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.useEffectOnce
import react.useState

suspend fun fetchVideo(id: Int): Video {
    val response = window
        .fetch("https://my-json-server.typicode.com/kotlin-hands-on/kotlinconf-json/videos/$id")
        .await()
        .text()
        .await()
    return Json.decodeFromString(response)
}

suspend fun fetchVideos(): List<Video> = coroutineScope {
    (1..25).map { id ->
        async {
            fetchVideo(id)
        }
    }.awaitAll()
}

val mainScope = MainScope()

val App = FC<Props> {
    var query: String by useState("")
    var currentVideo: VideoEntry? by useState(null)
    var unwatchedVideos: List<VideoEntry> by useState(emptyList())
    var watchedVideos: List<VideoEntry> by useState(emptyList())
    var isFavouritesChosen: Boolean by useState(false)

    useEffectOnce {
        mainScope.launch {
            unwatchedVideos = fetchVideos().map { VideoEntry(it, false)}
        }
    }
    div {
        label {
            css {
                fontWeight = FontWeight.bold
            }
            +"Search: "
        }
        input {
            css {
                width = 400.px
            }
            onChange = { event -> query = event.target.value }
            type = InputType.search
            placeholder = "Enter video title or speaker"
        }
    }
    div {
        h3 {
            +"Videos to watch"
        }
        VideoList() {
            videos = filterVideos(unwatchedVideos, query, isFavouritesChosen)
            selectedVideo = currentVideo
            onSelectVideo = { video -> currentVideo = video }
        }
    }
    div {
        h3 {
            +"Videos watched"
        }
        VideoList() {
            videos = filterVideos(watchedVideos, query, isFavouritesChosen)
            selectedVideo = currentVideo
            onSelectVideo = { video -> currentVideo = video }
        }
    }
    div {
        css {
            position = Position.absolute
            top = 50.px
            right = 1230.px
        }
        input {
            type = InputType.checkbox
            onChange = { isFavouritesChosen = !isFavouritesChosen }
        }
        label {
            css {
                fontWeight = FontWeight.bold
            }
            +"Only Favourites"
        }
    }
    div {
        currentVideo?.let { curr ->
            VideoPlayer {
                videoEntry = curr
                unwatchedVideo = curr in unwatchedVideos
                onWatchedButtonPressed = {
                    if (videoEntry in unwatchedVideos) {
                        unwatchedVideos = unwatchedVideos - videoEntry
                        watchedVideos = watchedVideos + videoEntry
                    } else {
                        watchedVideos = watchedVideos - videoEntry
                        unwatchedVideos = unwatchedVideos + videoEntry
                    }
                }
            }
        }
    }
}