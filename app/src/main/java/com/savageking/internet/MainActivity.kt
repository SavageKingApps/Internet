package com.savageking.internet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

class MainActivity : AppCompatActivity() {

    override fun onCreate(bundle : Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.app_container)

        if( bundle == null ) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment.getInstance(), MainFragment.getInstanceTag())
                .commit()
        }
    }
}
