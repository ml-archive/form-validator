package dk.nodes.formvalidator.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) showFragment(SignupFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menuLogin -> showFragment(LoginFragment())
                R.id.menuSignup -> showFragment(SignupFragment())
                R.id.menuCustom -> showFragment(dk.nodes.formvalidator.example.CustomFormFragment())
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(container.id, fragment, fragment.tag)
            .commit()
    }

}
