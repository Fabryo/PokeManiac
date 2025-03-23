package com.shodo.android.posttransaction.step2.ui

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions.Companion.Default
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.text.style.TextAlign
import com.shodo.android.coreui.R
import com.shodo.android.coreui.theme.PokeManiacTheme
import com.shodo.android.coreui.ui.GenericLoader
import com.shodo.android.coreui.ui.PrimaryButton
import com.shodo.android.domain.repositories.entities.NewActivityType
import com.shodo.android.domain.repositories.entities.NewActivityType.Purchase
import com.shodo.android.domain.repositories.entities.NewActivityType.Sale
import com.shodo.android.posttransaction.step2.PostTransactionStep2UiState
import com.shodo.android.posttransaction.step2.PostTransactionStep2ViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTransactionStep2View(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    viewModel: PostTransactionStep2ViewModel,
    onBackPressed: () -> Unit,
    onActivitySaved: () -> Unit
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.success.collectLatest { onActivitySaved() }
    }

    val uiState by viewModel.uiState.collectAsState()
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
                .padding(horizontal = PokeManiacTheme.dimens.standard)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Start,
            verticalArrangement = Arrangement.spacedBy(PokeManiacTheme.dimens.standard)
        ) {
            when (uiState) {
                PostTransactionStep2UiState.Filling -> FillPostTransactionInfosView(modifier, imageUri, viewModel::saveActivity)
                PostTransactionStep2UiState.Loading -> {
                    Spacer(modifier = Modifier.weight(1f))
                    GenericLoader(modifier = Modifier.align(CenterHorizontally))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillPostTransactionInfosView(modifier: Modifier = Modifier, imageUri: Uri, onSaveActivity: (pokemonName: String, pokemonNumber: Int, transactionType: NewActivityType, transactionPrice: Int, uri: Uri) -> Unit) {

    val focusManager = LocalFocusManager.current

    val numberFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }
    val priceFocusRequester = remember { FocusRequester() }

    var pokemonNumber by remember { mutableStateOf("") }
    var pokemonName by remember { mutableStateOf("") }
    var transactionPrice by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val transactionTypes = listOf(
        stringResource(R.string.new_post_step_2_transaction_type_purchase),
        stringResource(R.string.new_post_step_2_transaction_type_sale)
    )
    var selectedTransactionType by remember { mutableStateOf(transactionTypes[0]) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = PokeManiacTheme.colors.backgroundCell,
        unfocusedContainerColor = PokeManiacTheme.colors.backgroundCell,
        cursorColor = PokeManiacTheme.colors.backgroundCell,
        focusedLabelColor = Color.Transparent,
        unfocusedPlaceholderColor = PokeManiacTheme.colors.fourthText,
        focusedPlaceholderColor = PokeManiacTheme.colors.thirdText,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    Text(
        text = stringResource(R.string.new_post_step_2_title),
        modifier = modifier.padding(top = PokeManiacTheme.dimens.standard),
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t4
    )

    Text(
        text = stringResource(R.string.new_post_step_2_pokemon_number),
        modifier = Modifier.padding(top = PokeManiacTheme.dimens.large),
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t7
    )
    TextField(
        value = pokemonNumber,
        onValueChange = { if (it.all { char -> char.isDigit() }) pokemonNumber = it },
        placeholder = { Text(stringResource(R.string.new_post_step_2_pokemon_number_placeholder)) },
        textStyle = PokeManiacTheme.typography.t7.copy(color = PokeManiacTheme.colors.primaryText),
        colors = textFieldColors,
        modifier = Modifier.fillMaxWidth().focusRequester(numberFocusRequester),
        keyboardOptions = Default.copy(keyboardType = Number, imeAction = Next),
        keyboardActions = KeyboardActions(onNext = { nameFocusRequester.requestFocus() }),
        singleLine = true
    )

    Text(
        text = stringResource(R.string.new_post_step_2_pokemon_name),
        modifier = modifier,
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t7
    )
    TextField(
        value = pokemonName,
        onValueChange = { pokemonName = it },
        textStyle = PokeManiacTheme.typography.t7.copy(color = PokeManiacTheme.colors.primaryText),
        placeholder = { Text(stringResource(R.string.new_post_step_2_pokemon_name_placeholder)) },
        colors = textFieldColors,
        modifier = Modifier.fillMaxWidth().focusRequester(nameFocusRequester),
        keyboardOptions = Default.copy(imeAction = Next),
        keyboardActions = KeyboardActions(onNext = { priceFocusRequester.requestFocus() }),
        singleLine = true
    )

    Text(
        text = stringResource(R.string.new_post_step_2_price),
        modifier = modifier,
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t7
    )
    TextField(
        value = transactionPrice,
        onValueChange = { if (it.all { char -> char.isDigit() }) transactionPrice = it },
        textStyle = PokeManiacTheme.typography.t7.copy(color = PokeManiacTheme.colors.primaryText),
        placeholder = { Text(stringResource(R.string.new_post_step_2_price_placeholder)) },
        colors = textFieldColors,
        modifier = Modifier.fillMaxWidth().focusRequester(priceFocusRequester),
        keyboardOptions = Default.copy(keyboardType = Number, imeAction = Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true
    )

    Text(
        text = stringResource(R.string.new_post_step_2_transaction_type),
        modifier = modifier,
        color = PokeManiacTheme.colors.primaryText,
        style = PokeManiacTheme.typography.t7
    )
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(PrimaryNotEditable, true)
                .fillMaxWidth(),
            readOnly = true,
            value = selectedTransactionType,
            onValueChange = {},
            placeholder = { Text(stringResource(R.string.new_post_step_2_transaction_type)) },
            textStyle = PokeManiacTheme.typography.t7.copy(color = PokeManiacTheme.colors.primaryText),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = PokeManiacTheme.colors.primaryText,
                    modifier = modifier.rotate(if (expanded) 180f else 0f)
                )
            },
            colors = textFieldColors
        )

        DropdownMenu(
            modifier = Modifier.background(color = PokeManiacTheme.colors.backgroundCell),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            transactionTypes.forEach { label ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            modifier = modifier,
                            color = PokeManiacTheme.colors.primaryText,
                            style = PokeManiacTheme.typography.t5
                        )
                    },
                    onClick = {
                        selectedTransactionType = label
                        expanded = false
                    },
                    modifier = Modifier.background(color = PokeManiacTheme.colors.backgroundCell)
                )
            }
        }
    }

    if (pokemonName.isNotEmpty() && pokemonNumber.isNotEmpty() && transactionPrice.isNotEmpty()) {
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = PokeManiacTheme.dimens.small,
                    end = PokeManiacTheme.dimens.small,
                    bottom = PokeManiacTheme.dimens.xxLarge
                ),
            text = stringResource(R.string.next),
            onClick = { onSaveActivity(
                pokemonName,
                try { pokemonNumber.toInt() } catch (e: NumberFormatException) { 0 },
                when  {
                    transactionTypes[0] == selectedTransactionType -> Purchase
                    else -> Sale
                },
                try { transactionPrice.toInt() } catch (e: NumberFormatException) { 0 },
                imageUri
            ) }
        )
    }
}