package com.savageking.internet

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MainFragment : Fragment()
{
    companion object {
        fun getInstance() = MainFragment()
        fun getInstanceTag() = "MAIN_FRAGMENT"
    }

    private lateinit var wifi : WifiManager
    private lateinit var connection : ConnectivityManager

    private lateinit var clickToolbar : View.OnClickListener
    private lateinit var clickButton : View.OnClickListener

    private lateinit var receiver : InternetReceiver
    private lateinit var filter : IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickToolbar = View.OnClickListener{ view : View -> Unit
            activity?.finish()
        }

        clickButton = View.OnClickListener{ Unit ->

            Intent().apply{
                setAction(Settings.ACTION_WIRELESS_SETTINGS)
                setFlags( Intent.FLAG_ACTIVITY_NEW_TASK )
                startActivity( this )
            }
        }

        filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        val callback = object : InternetReceiverLink {
            override fun updateUi()
            {
               if ( isAdded )
               {
                   updateInstanceUi()
               }
            }
        }

        receiver = InternetReceiver( callback )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById<Toolbar>( R.id.toolbar )

        toolbar.apply{
            setTitle( R.string.toolbar_title )
            setSubtitle( R.string.toolbar_subtitle )
            setNavigationIcon(R.drawable.ic_clear_mtrl_alpha)
            setNavigationOnClickListener( clickToolbar )
        }

        val button = view.findViewById<Button>( R.id.button )
        button.setOnClickListener( clickButton )

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connection =  activity?.getSystemService( Context.CONNECTIVITY_SERVICE ) as ConnectivityManager
        wifi = activity?.applicationContext?.getSystemService( Context.WIFI_SERVICE ) as WifiManager
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver( receiver )
    }

    override fun onResume() {
        super.onResume()
        updateInstanceUi()
    }

    private fun isNetworkConnected() : Boolean{
        val activeNetwork : NetworkInfo? = connection.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

    private fun updateInstanceUi()
    {
        val textViewInternet = view?.findViewById<TextView>( R.id.textview_internet )
        val isNetworkConnected : Boolean  = isNetworkConnected()
        textViewInternet?.setText( getNetworkStatus( isNetworkConnected ))
    }

    private fun getNetworkStatus( isOn : Boolean ) : Int = when ( isOn )
    {
        true -> R.string.textview_internet_connected
        false -> R.string.textview_internet_not_connected
    }
}
