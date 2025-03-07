package com.picpay.desafio.android.presentation

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.di.AppModule
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter

    private val repository: UserRepository = UserRepositoryImpl(AppModule.service)

    override fun onResume() {
        super.onResume()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val users = repository.getUsers()
                progressBar.visibility = View.GONE
                adapter.users = users
            } catch (e: Exception) {
                val message = e.message ?: getString(R.string.error)

                progressBar.visibility = View.GONE
                recyclerView.visibility = View.GONE

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}