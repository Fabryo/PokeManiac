package com.shodo.android.coreui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme

@Composable
fun GenericLoader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally), color = PokeManiacTheme.colors.primaryText)
        Text(
            modifier = Modifier.align(CenterHorizontally).padding(top = PokeManiacTheme.dimens.small),
            text = stringResource(R.string.loading),
            style = PokeManiacTheme.typography.t3,
            color = PokeManiacTheme.colors.primaryText
        )
    }
}

@Preview
@Composable
fun GenericLoaderPreview() {
    GenericLoader()
}