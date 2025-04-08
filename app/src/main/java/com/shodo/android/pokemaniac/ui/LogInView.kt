package com.shodo.android.pokemaniac.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.PrimaryButton
import com.shodo.android.coreui.ui.SecondaryButton

@Composable
fun LogInView(
    modifier: Modifier = Modifier,
    onSignUpClicked:() -> Unit,
    onSignInClicked:() -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = PokeManiacTheme.colors.backgroundApp),
        containerColor = PokeManiacTheme.colors.backgroundApp,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = PokeManiacTheme.colors.backgroundApp)
        ) {
            Welcome(modifier = Modifier.padding(start = PokeManiacTheme.dimens.large, top = PokeManiacTheme.dimens.xxLarge))

            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .align(CenterHorizontally),
                painter = painterResource(id = R.drawable.pokemaniac),
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.small),
                text = stringResource(R.string.signup),
                onClick = onSignUpClicked
            )

            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = PokeManiacTheme.dimens.small, end = PokeManiacTheme.dimens.small, bottom = PokeManiacTheme.dimens.xxLarge),
                text = stringResource(R.string.signin),
                onClick = onSignInClicked
            )
        }
    }
}


@Composable
fun Welcome( modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.welcome),
        modifier = modifier,
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t1
    )
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    PokeManiacTheme {
        Welcome()
    }
}