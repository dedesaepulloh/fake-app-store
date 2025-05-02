package com.dedesaepulloh.fakeappstore.presentation.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedesaepulloh.fakeappstore.presentation.category.state.CategoryState
import com.dedesaepulloh.fakeappstore.presentation.components.ErrorScreen
import com.dedesaepulloh.fakeappstore.presentation.components.LoadingScreen

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit = {},
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val categoryState by viewModel.categoryState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }
    Box(modifier = modifier.fillMaxSize()) {
        when (val state = categoryState) {
            is CategoryState.Loading -> {
                LoadingScreen()
            }

            is CategoryState.Success -> {
                val categories = state.data
                CategoryContent(
                    categories = categories,
                    onCategoryClick = onCategoryClick
                )
            }

            is CategoryState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = {
                        viewModel.getCategories()
                    }
                )
            }

            else -> Unit
        }
    }
}

@Composable
fun CategoryContent(
    categories: List<String>,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(categories) { category ->
                CategoryItem(name = category, onClick = { onCategoryClick(category) })
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
