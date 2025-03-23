package com.shodo.android.posttransaction.step1.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.checkSelfPermission
import coil3.compose.rememberAsyncImagePainter
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.PrimaryButton
import com.shodo.android.coreui.ui.SecondaryButton
import com.shodo.android.posttransaction.step1.PostTransactionStep1ViewModel
import java.io.File
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTransactionStep1View(
    modifier: Modifier = Modifier,
    viewModel: PostTransactionStep1ViewModel,
    onNextStep: (Uri) -> Unit,
    onBackPressed: () -> Unit
) {
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = PokeManiacTheme.colors.backgroundApp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PokeManiacTheme.colors.backgroundApp
                ),
                title = {
                    Text(
                        text = stringResource(R.string.new_post_title),
                        style = PokeManiacTheme.typography.t1,
                        textAlign = TextAlign.Center,
                        color = PokeManiacTheme.colors.primaryText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PokeManiacTheme.colors.primaryText
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .background(color = PokeManiacTheme.colors.backgroundApp)
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.new_post_step_1_title),
                modifier = modifier
                    .padding(PokeManiacTheme.dimens.standard)
                    .align(Alignment.Start),
                color = PokeManiacTheme.colors.primaryText,
                style = PokeManiacTheme.typography.t4
            )
            if (capturedImageUri.path?.isNotEmpty() == true) {
                Image(
                    modifier = Modifier.padding(top = PokeManiacTheme.dimens.standard).fillMaxWidth().aspectRatio(1f),
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                TakePhotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PokeManiacTheme.dimens.small, vertical = PokeManiacTheme.dimens.small),
                    text = stringResource(R.string.new_post_step_1_retake_photo),
                    onSetUri = { uri -> capturedImageUri = uri },
                    createImageFile = viewModel::createImageFile,
                    getUriForImageFile = viewModel::getUriForFile
                )
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = PokeManiacTheme.dimens.small, end = PokeManiacTheme.dimens.small, bottom = PokeManiacTheme.dimens.xxLarge),
                    text = stringResource(R.string.next),
                    onClick = { onNextStep(capturedImageUri) }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(CenterHorizontally),
                    painter = painterResource(id = com.shodo.android.pokemaniac.R.drawable.pokemaniac),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                TakePhotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = PokeManiacTheme.dimens.small, end = PokeManiacTheme.dimens.small, bottom = PokeManiacTheme.dimens.xxLarge),
                    text = stringResource(R.string.new_post_step_1_take_photo),
                    onSetUri = { uri -> capturedImageUri = uri },
                    createImageFile = viewModel::createImageFile,
                    getUriForImageFile = viewModel::getUriForFile
                )
            }
        }
    }
}

@Composable
fun TakePhotoButton(
    modifier: Modifier = Modifier,
    text: String,
    onSetUri: (Uri) -> Unit,
    createImageFile: (Context) -> File?,
    getUriForImageFile: (Context, File?) -> Uri?
) {
    val context = LocalContext.current

    val imageFile = remember { createImageFile(context) }
    val uri = getUriForImageFile(context, imageFile)

    val launcher = rememberLauncherForActivityResult(contract = TakePicture()) { success ->
        if (success) {
            uri?.let(onSetUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            uri?.let(launcher::launch)
        } else {
            Toast.makeText(context, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    SecondaryButton(
        modifier = modifier,
        text = text
    ) {
        if (checkSelfPermission(context, Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            uri?.let(launcher::launch)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}
