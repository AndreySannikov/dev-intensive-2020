package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.snackBar
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            queryHint = "Введите имя пользователя"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.handleSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.handleSearchQuery(newText)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            if (it.chatType == ChatType.ARCHIVE) {
                val intent = Intent(this, ArchiveActivity::class.java)
                startActivity(intent)
            } else {
                snackBar(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
            }
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val archiveIcon = getDrawable(R.drawable.ic_archive_white_24dp)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter, archiveIcon!!) { chatItem ->
            viewModel.addToArchive(chatItem.id)
            snackBar(
                rv_chat_list,
                "Вы точно хотите добавить ${chatItem.title} в архив?",
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.cancellation) { viewModel.restoreFromArchive(chatItem.id) }
                .show()
            rv_chat_list.scrollToPosition(0)
        }

        with(rv_chat_list) {
            ItemTouchHelper(touchCallback).attachToRecyclerView(this)
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.getActiveChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}
