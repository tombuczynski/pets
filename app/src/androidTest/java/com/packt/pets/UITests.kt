package com.packt.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.packt.pets.data.Cat
import com.packt.pets.views.PetListItem
import org.junit.Rule
import org.junit.Test

/**
 * Created by Tom Buczynski on 30.03.2025.
 */
class UITests {
    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Test
    fun testPetListItem() {
        with(composeTestRule) {
            setContent {
                var pet by remember {
                    mutableStateOf(
                        Cat(
                            id = "GUOunrpLPB6Jw0zE",
                            tags = listOf(
                                "handicapped",
                                "tabby",
                                "double",
                                "multiple",
                            ),
                            isFavorite = false,
                        ),
                    )
                }

                PetListItem(
                    pet = pet,
                    onPetClicked = { },
                    onFavoritePetClicked = { pet = pet.copy(isFavorite = true) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            onNodeWithTag("PetListItemCard").assertExists()

            onNodeWithTag("PetListItemColumn", useUnmergedTree = true).assertExists()

            onNodeWithText("tabby").assertIsDisplayed()

            onNodeWithContentDescription("Cute Cat").run {
                isDisplayed()
                assertWidthIsAtLeast(200.dp)
            }

            onNodeWithContentDescription("Favorite").run {
                isDisplayed()
                assertIsOff()
                performClick()
                assertIsOn()
            }
        }
    }
}