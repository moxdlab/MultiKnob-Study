package io.moxd.dreair

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import io.moxd.dreair.ui.layout.*
import io.moxd.dreair.ui.theme.DreaIRTheme
import io.moxd.dreair.viewmodel.CountdownViewModel
import io.moxd.dreair.viewmodel.PlaygroundViewModel
import io.moxd.dreair.viewmodel.StudyEntryViewModel
import io.moxd.dreair.viewmodel.StudyExecutionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val entryViewModel: StudyEntryViewModel by viewModels()
        val playgroundViewModel: PlaygroundViewModel by viewModels()
        val executionViewModel: StudyExecutionViewModel by viewModels()
        val countdownViewModel: CountdownViewModel by viewModels()

        setContent {
            DreaIRTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppComposable(
                        entryViewModel,
                        playgroundViewModel,
                        countdownViewModel,
                        executionViewModel
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "No back press allowed :)", Toast.LENGTH_SHORT).show()
    }
}