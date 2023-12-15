package com.example.libri
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception

@Suppress("UNREACHABLE_CODE")
class BookActivity : AppCompatActivity() {
    private lateinit var bookList: ArrayList<Book>
    private lateinit var viewModel: BookViewModel
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_activity)
        progressBar = findViewById(R.id.progressBar)

        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        bookList = ArrayList()
        bookList.add(Book("Дубровский\nАвтор: Александр Пушкин", readBookFromResources(R.raw.dubrovskiy)))
        bookList.add(Book("Ревизор\n Автор: Николай Гоголь", readBookFromResources(R.raw.revizor)))
        bookList.add(Book("Бедная Лиза\nАвтор: Николай Карамзин",readBookFromResources(R.raw.liza) ))
        bookList.add(Book("Гранатовый браслет\nАвтор: Александр Куприн",readBookFromResources(R.raw.braslet) ))
        bookList.add(Book("Мы\nАвтор: Евгений Замятин",readBookFromResources(R.raw.myi) ))
        bookList.add(Book("Горе от ума\nАвтор: Александр Грибоедов",readBookFromResources(R.raw.goreotuma) ))
        bookList.add(Book("Гроза\nАвтор: Александр Островский",readBookFromResources(R.raw.groza) ))
        bookList.add(Book("Морфий\nАвтор: Михаил Булгаков",readBookFromResources(R.raw.morfiyi) ))
        bookList.add(Book("Вишнёвый сад\nАвтор: Антон Чехов",readBookFromResources(R.raw.vishnevyiyisad) ))
        bookList.add(Book("Герой нашего времени\nАвтор: Михаил Лермонтов",readBookFromResources(R.raw.hero_of_our_time) ))
        bookList.add(Book("Отцы и дети\nАвтор: Иван Тургенев",readBookFromResources(R.raw.fathers_and_sons) ))
        bookList.add(Book("Господин из Сан-Франциско\nАвтор: Иван Бунин",readBookFromResources(R.raw.gentleman_from_san_francisco) ))
        bookList.add(Book("Капитанская дочка\nАвтор: Александр Пушкин",readBookFromResources(R.raw.captains_daughter) ))
        bookList.add(Book("На дне\nАвтор: Максим Горький",readBookFromResources(R.raw.na_dne) ))



        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookList.map { it.title })
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedBook = bookList[position]
            viewModel.setLoading(true)


            val intent = Intent(this, ReadActivity::class.java)
            intent.putExtra("title", selectedBook.title)
            intent.putExtra("content", selectedBook.content)
            startActivity(intent)
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }) }


    override fun onResume() {
        super.onResume()
        viewModel.setLoading(false)
    }
    private fun readBookFromResources(resourceId: Int): String {
        try {
            return resources.openRawResource(resourceId).bufferedReader().use {

                it.readText() }
        }catch (e:Exception){

        }
        return "error"
}
    fun InformationClick(view: View){
        val intent = Intent(this, AboutProgrammActivity::class.java)
        startActivity(intent)}
}