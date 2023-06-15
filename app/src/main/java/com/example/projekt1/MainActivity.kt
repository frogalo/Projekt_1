package com.example.projekt1


import EditFragment
import ARG_EDIT_IT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projekt1.fragments.ListFragment

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, id: Int?) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.container,
                    listFragment,
                    listFragment.javaClass.name
                )

                Navigable.Destination.Add -> {
                    replace(
                        R.id.container,
                        EditFragment(),
                        EditFragment::class.java.name
                    )
                    addToBackStack(
                        EditFragment::class.java.name
                    )
                }

                Navigable.Destination.Edit -> {
                    replace(
                        R.id.container,
                        EditFragment::class.java,
                        Bundle().apply { putInt(ARG_EDIT_IT, id ?: -1) },
                        EditFragment::class.java.name
                    )
                    addToBackStack(
                        EditFragment::class.java.name
                    )
                }
            }
        }.commit()
    }
}