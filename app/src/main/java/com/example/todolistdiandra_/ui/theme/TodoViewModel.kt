package com.example.todolistdiandra_.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistdiandra_.data.Todo
import com.example.todolistdiandra_.data.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TodoDatabase.getDatabase(application).todoDao()

    val todos = dao.getAllTodos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTodo(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertTodo(Todo(title = title, description = description))
        }
    }

    fun toggleComplete(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteTodo(todo)
        }
    }
}