package app.myfinances

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_SMS: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Included priming views to explain to user why we need the messaging permissions

        //Check if messaging permissions are granted and show or hide priming view
        when(isMessagingPermissionsGranted()) {
            true -> hidePrimingViews()
            false -> showPrimingViews()
        }

        permissionsPrimingButton.setOnClickListener { getMessagingPermissions() }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_PERMISSIONS_REQUEST_READ_SMS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    hidePrimingViews()
                    readSMSes()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(permissionsPrimingView, "SMS Permissions Denied :(", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Re-Request") {
                                requestMessagingPermissions()
                            }
                            .show()
                }
                return
            }
            else -> {}
        }
    }

    private fun getMessagingPermissions() {
        when(isMessagingPermissionsGranted()){
            true -> hidePrimingViews()
            false -> requestMessagingPermissions()
        }
    }

    private fun isMessagingPermissionsGranted(): Boolean {
        return when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED -> true
            else -> false
        }
    }

    private fun requestMessagingPermissions() {
        // Permission is not granted. Pass a request code to view it later onRequestPermissionsResult
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS),
                MY_PERMISSIONS_REQUEST_READ_SMS)
    }

    private fun showPrimingViews() {
        showHidePrimingViews(true)
    }

    private fun hidePrimingViews() {
        showHidePrimingViews(false)
    }

    private fun showHidePrimingViews(show: Boolean){
        arrayListOf(permissionsPrimingView, permissionsPrimingTextView, permissionsPrimingButton).forEach { view ->
            view.visibility = when(show){
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
    }

    private fun readSMSes() {
        //Now to read sms's!
    }
}
