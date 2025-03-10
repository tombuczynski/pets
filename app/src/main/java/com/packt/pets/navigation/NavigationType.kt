package com.packt.pets.navigation

import android.graphics.Rect
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by Tom Buczynski on 10.01.2025.
 */
/**
 * Created by Tom Buczynski on 28.12.2024.
 */
data class NavigationType(
    val navControlType: NavControlType,
    val contentType: ContentType,
    val featureBounds: Rect,
)

enum class NavControlType {
    BOTTOM_NAVIGATION,
    NAVIGATION_DRAWER,
    NAVIGATION_RAIL,
}

enum class ContentType {
    LIST,
    LIST_AND_DETAIL,
}

/**
 * Determines the appropriate navigation and content type based on the window size class and
 * presence of a folding feature.
 *
 * This function analyzes the provided [windowSizeClass] and optional [foldingFeature] to
 * determine the optimal navigation and content presentation for the application. It considers
 * factors such as window size, height, and the presence of physical display features
 * like hinges or folds.
 *
 * The function returns a [NavigationType] object that encapsulates the recommended navigation
 * control type ([NavControlType]), the content display type ([ContentType]), and the
 * bounds of any relevant folding features ([Rect]).
 *
 * @param windowSizeClass The window size class, describing the general dimensions of the display.
 * @param foldingFeature An optional folding feature, representing the presence of a physical
 *                       hinge or fold in the display. Can be null if no such feature is detected.
 * @return A [NavigationType] object containing the recommended navigation and content types,
 *         along with the bounds of any relevant folding features.
 */
fun determineNavigationType(
    windowSizeClass: WindowSizeClass,
    foldingFeature: FoldingFeature?,
): NavigationType {
    var navControlType = NavControlType.NAVIGATION_RAIL
    var contentType = ContentType.LIST
    var featureBounds = Rect()

    if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
        navControlType = NavControlType.NAVIGATION_DRAWER
    } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        navControlType = NavControlType.BOTTOM_NAVIGATION
    } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
        if (isVerticallySeparated(foldingFeature)) {
            contentType = ContentType.LIST_AND_DETAIL
            featureBounds = foldingFeature.bounds
        }
    } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        contentType = ContentType.LIST_AND_DETAIL
        if (isVerticallySeparated(foldingFeature) || isBookPostured(foldingFeature)) {
            featureBounds = foldingFeature.bounds
        }
    }

    return NavigationType(navControlType, contentType, featureBounds)
}

@OptIn(ExperimentalContracts::class)
fun isVerticallySeparated(foldingFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies(foldingFeature != null) }

    return (foldingFeature?.orientation == FoldingFeature.Orientation.VERTICAL) &&
        (foldingFeature.state == FoldingFeature.State.FLAT) && (foldingFeature.isSeparating)
}

@OptIn(ExperimentalContracts::class)
fun isBookPostured(foldingFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies(foldingFeature != null) }

    return (foldingFeature?.orientation == FoldingFeature.Orientation.VERTICAL) &&
        (foldingFeature.state == FoldingFeature.State.HALF_OPENED)
}