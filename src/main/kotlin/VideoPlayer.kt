import csstype.NamedColor
import csstype.Position
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3

external interface VideoPlayerProps : Props {
    var videoEntry: VideoEntry
    var onWatchedButtonPressed: (VideoEntry) -> Unit
    var unwatchedVideo: Boolean
}

val VideoPlayer = FC<VideoPlayerProps> { props ->
    div {
        css {
            position = Position.absolute
            top = 10.px
            right = 100.px
        }
        h3 {
            +"${props.videoEntry.video.speaker}: ${props.videoEntry.video.title}"
        }
        button {
            css {
                backgroundColor = if (props.unwatchedVideo) NamedColor.lightgreen else NamedColor.lightcoral
            }
            onClick = {
                props.onWatchedButtonPressed(props.videoEntry)
            }
            if (props.unwatchedVideo) {
                +"Mark as watched"
            } else {
                +"Mark as unwatched"
            }
        }
        button {
            css {
                backgroundColor = if (props.videoEntry.isFavourite) NamedColor.lightgoldenrodyellow else NamedColor.aliceblue
            }
            onClick = {
                props.videoEntry.isFavourite = !props.videoEntry.isFavourite
            }
            if (!props.videoEntry.isFavourite) {
                +"Add to Favourites"
            } else {
                +"Remove from Favourites"
            }
        }
        ReactPlayer {
            url = props.videoEntry.video.videoUrl
            controls = true
        }
        div {
            css {
                position = Position.absolute
                right = 0.px
            }
            EmailShareButton {
                url = props.videoEntry.video.videoUrl
                EmailIcon {
                    size = 32
                    round = true
                }
            }
            TelegramShareButton {
                url = props.videoEntry.video.videoUrl
                TelegramIcon {
                    size = 32
                    round = true
                }
            }
            FacebookShareButton {
                url = props.videoEntry.video.videoUrl
                FacebookIcon {
                    size = 32
                    round = true
                }
            }
        }
    }
}