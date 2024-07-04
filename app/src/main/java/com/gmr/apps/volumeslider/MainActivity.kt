package com.gmr.apps.volumeslider

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.gmr.apps.volumeslider.ui.theme.VolumeSliderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VolumeSliderTheme {

                VolumeSlider()

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeSlider(){

    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val (sliderValue, setSliderValue) = remember{ mutableFloatStateOf(currentVolume.toFloat()) }


    Box(
        contentAlignment = Alignment.Center,
        modifier= Modifier
            .padding(top = 150.dp, start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(20.dp))

    ){
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(16.dp)
                .align(Alignment.Center)

        ) {

            Text(text = "Volume",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 0.dp,top=0.dp, bottom = 10.dp)
            )


            Row (
                verticalAlignment = Alignment.CenterVertically
            ){



            var image by remember{ mutableIntStateOf(R.drawable.volume_down) }

            Icon(
                painter = painterResource(id = image),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(35.dp)
            )


                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .semantics { contentDescription = "Volume control" },
                    value = sliderValue,
                    valueRange = 0f..1f,
                    onValueChange = {value ->
                        setSliderValue(value)
                        val newVolume =(value * maxVolume).toInt()
                        audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            newVolume,
                            0
                        )

                        image =when{
                            value == 0f -> R.drawable.volume_off
                            value < 0.5f -> R.drawable.volume_down
                            else -> R.drawable.volume_up
                        }
                    },
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.surface,
                        inactiveTrackColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.4f
                        )
                    ),
                    thumb = {
                        SliderDefaults.Thumb(interactionSource = remember {
                            MutableInteractionSource()
                        },
                            thumbSize = DpSize(35.dp,35.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.surface
                            )

                        )

                    }
                )


            }


        }

    }

}