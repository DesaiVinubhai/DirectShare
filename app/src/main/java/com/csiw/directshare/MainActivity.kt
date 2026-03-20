package com.csiw.directshare

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csiw.directsharekit.ui.ShareManager

/**
 * Main Activity demonstrating the Document Sharing Module
 * Shows how to use all sharing features in a clean MVVM architecture
 */
class MainActivity : AppCompatActivity() {

    private lateinit var pickSingleButton: Button
    private lateinit var pickMultipleButton: Button
    private lateinit var clearSelectionButton: Button
    private lateinit var refreshAppsButton: Button

    private lateinit var shareWhatsAppSingleButton: Button
    private lateinit var shareWhatsAppMultiButton: Button
    private lateinit var shareTelegramSingleButton: Button
    private lateinit var shareTelegramMultiButton: Button
    private lateinit var shareMessengerSingleButton: Button
    private lateinit var shareMessengerMultiButton: Button
    private lateinit var shareGmailSingleButton: Button
    private lateinit var shareGmailMultiButton: Button
    private lateinit var shareDriveSingleButton: Button
    private lateinit var shareDriveMultiButton: Button
    private lateinit var shareGenericSingleButton: Button
    private lateinit var shareGenericMultiButton: Button

    private lateinit var selectionStatusText: TextView
    private lateinit var appStatusText: TextView

    private val selectedUris = arrayListOf<Uri>()
    private var singleSelectionMimeType: String = "*/*"
    private var isWhatsAppInstalled = false
    private var isTelegramInstalled = false
    private var isMessengerInstalled = false
    private var isGmailInstalled = false
    private var isDriveInstalled = false

    private val demoPhoneNumber = "919876543210"
    private val demoRecipient = "demo@example.com"

    private val pickSingleDocument = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            clearSelection()
            return@registerForActivityResult
        }

        takeReadPermission(uri)
        selectedUris.clear()
        selectedUris.add(uri)
        singleSelectionMimeType = contentResolver.getType(uri) ?: "*/*"
        updateSelectionUi()
    }

    private val pickMultipleDocuments = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isEmpty()) {
            clearSelection()
            return@registerForActivityResult
        }

        selectedUris.clear()
        uris.forEach { uri ->
            takeReadPermission(uri)
            selectedUris.add(uri)
        }
        singleSelectionMimeType = "*/*"
        updateSelectionUi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        bindActions()
        refreshInstalledAppsUi()
        updateSelectionUi()
    }

    override fun onResume() {
        super.onResume()
        refreshInstalledAppsUi()
    }

    private fun bindViews() {
        pickSingleButton = findViewById(R.id.btnPickSingle)
        pickMultipleButton = findViewById(R.id.btnPickMultiple)
        clearSelectionButton = findViewById(R.id.btnClearSelection)
        refreshAppsButton = findViewById(R.id.btnRefreshApps)

        shareWhatsAppSingleButton = findViewById(R.id.btnShareWhatsAppSingle)
        shareWhatsAppMultiButton = findViewById(R.id.btnShareWhatsAppMulti)
        shareTelegramSingleButton = findViewById(R.id.btnShareTelegramSingle)
        shareTelegramMultiButton = findViewById(R.id.btnShareTelegramMulti)
        shareMessengerSingleButton = findViewById(R.id.btnShareMessengerSingle)
        shareMessengerMultiButton = findViewById(R.id.btnShareMessengerMulti)
        shareGmailSingleButton = findViewById(R.id.btnShareGmailSingle)
        shareGmailMultiButton = findViewById(R.id.btnShareGmailMulti)
        shareDriveSingleButton = findViewById(R.id.btnShareDriveSingle)
        shareDriveMultiButton = findViewById(R.id.btnShareDriveMulti)
        shareGenericSingleButton = findViewById(R.id.btnShareGenericSingle)
        shareGenericMultiButton = findViewById(R.id.btnShareGenericMulti)

        selectionStatusText = findViewById(R.id.tvSelectionStatus)
        appStatusText = findViewById(R.id.tvAppStatus)
    }

    private fun bindActions() {
        pickSingleButton.setOnClickListener {
            pickSingleDocument.launch(arrayOf("*/*"))
        }

        pickMultipleButton.setOnClickListener {
            pickMultipleDocuments.launch(arrayOf("*/*"))
        }

        clearSelectionButton.setOnClickListener {
            clearSelection()
        }

        refreshAppsButton.setOnClickListener {
            refreshInstalledAppsUi()
        }

        shareWhatsAppSingleButton.setOnClickListener {
            shareSingleWithApp(isWhatsAppInstalled, R.string.whatsapp_not_installed) { uri ->
                ShareManager.shareToWhatsApp(
                    context = this,
                    uri = uri,
                    mimeType = singleSelectionMimeType,
                    phoneNumber = demoPhoneNumber,
                    message = "Single-file demo from DirectShareKit"
                )
            }
        }

        shareWhatsAppMultiButton.setOnClickListener {
            shareMultipleWithApp(isWhatsAppInstalled, R.string.whatsapp_not_installed) { uris ->
                ShareManager.shareMultipleToWhatsApp(
                    context = this,
                    uris = uris,
                    phoneNumber = demoPhoneNumber,
                    message = "Multi-file demo from DirectShareKit"
                )
            }
        }

        shareTelegramSingleButton.setOnClickListener {
            shareSingleWithApp(isTelegramInstalled, R.string.telegram_not_installed) { uri ->
                ShareManager.shareToTelegram(
                    context = this,
                    uri = uri,
                    mimeType = singleSelectionMimeType,
                    message = "Single-file demo via shareToTelegram"
                )
            }
        }

        shareTelegramMultiButton.setOnClickListener {
            shareMultipleWithApp(isTelegramInstalled, R.string.telegram_not_installed) { uris ->
                ShareManager.shareMultipleToTelegram(
                    context = this,
                    uris = uris,
                    message = "Multi-file demo via shareMultipleToTelegram"
                )
            }
        }

        shareMessengerSingleButton.setOnClickListener {
            shareSingleWithApp(isMessengerInstalled, R.string.messenger_not_installed) { uri ->
                ShareManager.shareToMessenger(
                    context = this,
                    uri = uri,
                    mimeType = singleSelectionMimeType
                )
            }
        }

        shareMessengerMultiButton.setOnClickListener {
            shareMultipleWithApp(isMessengerInstalled, R.string.messenger_not_installed) { uris ->
                ShareManager.shareMultipleToMessenger(
                    context = this,
                    uris = uris
                )
            }
        }

        shareGmailSingleButton.setOnClickListener {
            shareSingleWithApp(isGmailInstalled, R.string.gmail_not_installed) { uri ->
                ShareManager.shareViaGmail(
                    context = this,
                    uri = uri,
                    recipient = demoRecipient,
                    subject = "DirectShareKit single-file demo",
                    body = "This email was triggered using shareViaGmail()."
                )
            }
        }

        shareGmailMultiButton.setOnClickListener {
            shareMultipleWithApp(isGmailInstalled, R.string.gmail_not_installed) { uris ->
                ShareManager.shareMultipleViaGmail(
                    context = this,
                    uris = uris,
                    recipient = demoRecipient,
                    subject = "DirectShareKit multi-file demo",
                    body = "This email was triggered using shareMultipleViaGmail()."
                )
            }
        }

        shareDriveSingleButton.setOnClickListener {
            shareSingleWithApp(isDriveInstalled, R.string.drive_not_installed) { uri ->
                ShareManager.shareViaDrive(
                    context = this,
                    uri = uri
                )
            }
        }

        shareDriveMultiButton.setOnClickListener {
            shareMultipleWithApp(isDriveInstalled, R.string.drive_not_installed) { uris ->
                ShareManager.shareMultipleViaDrive(
                    context = this,
                    uris = uris
                )
            }
        }

        shareGenericSingleButton.setOnClickListener {
            shareSingleSelection { uri ->
                ShareManager.shareGeneric(
                    context = this,
                    uri = uri,
                    mimeType = singleSelectionMimeType,
                    title = "DirectShareKit: generic single share"
                )
            }
        }

        shareGenericMultiButton.setOnClickListener {
            shareMultipleSelection { uris ->
                ShareManager.shareMultiple(
                    context = this,
                    uris = uris,
                    title = "DirectShareKit: generic multi share"
                )
            }
        }
    }

    private fun clearSelection() {
        selectedUris.clear()
        singleSelectionMimeType = "*/*"
        updateSelectionUi()
        Toast.makeText(this, getString(R.string.file_selection_cleared), Toast.LENGTH_SHORT).show()
    }

    private fun updateSelectionUi() {
        val count = selectedUris.size
        selectionStatusText.text = when (count) {
            0 -> getString(R.string.no_file_selected)
            1 -> getString(R.string.single_file_selected)
            else -> getString(R.string.multiple_files_selected, count)
        }
        updateShareButtonsState(count)
    }

    private fun updateShareButtonsState(selectionCount: Int) {
        val hasSingle = selectionCount == 1
        val hasMultiple = selectionCount > 1

        shareWhatsAppSingleButton.isEnabled = hasSingle && isWhatsAppInstalled
        shareWhatsAppMultiButton.isEnabled = hasMultiple && isWhatsAppInstalled
        shareTelegramSingleButton.isEnabled = hasSingle && isTelegramInstalled
        shareTelegramMultiButton.isEnabled = hasMultiple && isTelegramInstalled
        shareMessengerSingleButton.isEnabled = hasSingle && isMessengerInstalled
        shareMessengerMultiButton.isEnabled = hasMultiple && isMessengerInstalled
        shareGmailSingleButton.isEnabled = hasSingle && isGmailInstalled
        shareGmailMultiButton.isEnabled = hasMultiple && isGmailInstalled
        shareDriveSingleButton.isEnabled = hasSingle && isDriveInstalled
        shareDriveMultiButton.isEnabled = hasMultiple && isDriveInstalled
        shareGenericSingleButton.isEnabled = hasSingle
        shareGenericMultiButton.isEnabled = hasMultiple
    }

    private fun refreshInstalledAppsUi() {
        isWhatsAppInstalled = ShareManager.isWhatsAppInstalled(this)
        isTelegramInstalled = ShareManager.isTelegramInstalled(this)
        isMessengerInstalled = ShareManager.isMessengerInstalled(this)
        isGmailInstalled = ShareManager.isGmailInstalled(this)
        isDriveInstalled = ShareManager.isDriveInstalled(this)

        appStatusText.text = getString(
            R.string.apps_status_template,
            appInstallLabel(isWhatsAppInstalled),
            appInstallLabel(isTelegramInstalled),
            appInstallLabel(isMessengerInstalled),
            appInstallLabel(isGmailInstalled),
            appInstallLabel(isDriveInstalled)
        )

        updateShareButtonsState(selectedUris.size)
    }

    private fun appInstallLabel(installed: Boolean): String {
        return if (installed) {
            getString(R.string.app_installed)
        } else {
            getString(R.string.app_not_installed)
        }
    }

    private fun shareSingleWithApp(isInstalled: Boolean, appMissingMessageRes: Int, action: (Uri) -> Unit) {
        if (!isInstalled) {
            Toast.makeText(this, getString(appMissingMessageRes), Toast.LENGTH_SHORT).show()
            return
        }
        shareSingleSelection(action)
    }

    private fun shareMultipleWithApp(isInstalled: Boolean, appMissingMessageRes: Int, action: (ArrayList<Uri>) -> Unit) {
        if (!isInstalled) {
            Toast.makeText(this, getString(appMissingMessageRes), Toast.LENGTH_SHORT).show()
            return
        }
        shareMultipleSelection(action)
    }

    private fun shareSingleSelection(action: (Uri) -> Unit) {
        if (selectedUris.size != 1) {
            val messageRes = if (selectedUris.isEmpty()) {
                R.string.please_select_file
            } else {
                R.string.please_select_single_file
            }
            Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
            return
        }
        action(selectedUris.first())
    }

    private fun shareMultipleSelection(action: (ArrayList<Uri>) -> Unit) {
        if (selectedUris.size < 2) {
            val messageRes = if (selectedUris.isEmpty()) {
                R.string.please_select_file
            } else {
                R.string.please_select_multiple_files
            }
            Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
            return
        }
        action(ArrayList(selectedUris))
    }

    private fun takeReadPermission(uri: Uri) {
        val flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            contentResolver.takePersistableUriPermission(uri, flags)
        } catch (_: SecurityException) {
            // Some providers do not allow persistable permission; one-time grants still work.
        }
    }
}