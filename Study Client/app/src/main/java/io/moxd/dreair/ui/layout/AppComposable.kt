package io.moxd.dreair.ui.layout

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.moxd.dreair.viewmodel.CountdownViewModel
import io.moxd.dreair.viewmodel.PlaygroundViewModel
import io.moxd.dreair.viewmodel.StudyEntryViewModel
import io.moxd.dreair.viewmodel.StudyExecutionViewModel

@Composable
fun AppComposable(
    entryViewModel: StudyEntryViewModel,
    playgroundViewModel: PlaygroundViewModel,
    countdownViewModel: CountdownViewModel,
    executionViewModel: StudyExecutionViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Entry.route) {
        composable(Screen.Entry.route) {
            StudyEntryScreen(viewModel = entryViewModel,
                navigatePlayground = {
                    navController.navigate(Screen.Playground.route)
                }, navigateExecution = {
                    navController.navigate(Screen.Countdown.route)
                })
        }

        composable(Screen.Playground.route) {
            PlaygroundScreen(viewModel = playgroundViewModel,
                navigateBack = {
                    navController.popBackStack()
                })
        }

        composable(Screen.Countdown.route) {
            CountdownScreen(viewModel = countdownViewModel,
                navigateExecution = {
                    navController.navigate(Screen.Execution.route) {
                        popUpTo(0)
                    }
                })
        }

        composable(Screen.Execution.route) {
            StudyExecutionScreen(viewModel = executionViewModel,
                navigateCountdown = {
                    navController.navigate(Screen.Countdown.route) {
                        popUpTo(0)
                    }
                },
                navigateEntry = {
                    navController.navigate(Screen.Entry.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

private sealed class Screen(val route: String) {
    object Entry : Screen("entry")
    object Playground : Screen("playground")
    object Execution : Screen("execution")
    object Countdown : Screen("countdown")
}