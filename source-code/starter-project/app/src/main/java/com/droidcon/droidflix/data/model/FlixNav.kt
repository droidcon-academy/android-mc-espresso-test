package com.droidcon.droidflix.data.model

import androidx.annotation.StringRes
import com.droidcon.droidflix.R

enum class FlixNav(@StringRes val title: Int, val route: String) {
    FlixList(title = R.string.app_name, route = "FlixList"),
    FlixDetail(title = R.string.details, route = "FlixDetail/{id}"),
    FlixWeb(title = R.string.web, route = "FlixWeb");

    companion object {
        fun getScreenByRoute(route: String): FlixNav {
            return when (route) {
                FlixList.route -> FlixList
                FlixDetail.route -> FlixDetail
                FlixWeb.route -> FlixWeb
                else -> throw IllegalArgumentException("Unknown DroidFlix route: $route")
            }
        }
    }
}