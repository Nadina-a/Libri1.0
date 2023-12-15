package com.example.libri
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ReadActivity : AppCompatActivity(), BottomSheetFragment.FontSelectionListener,
    BottomSheetFragment.ThemeSelectionListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var scrollView: ScrollView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val actionRelative = findViewById<RelativeLayout>(R.id.actionRelative)
        val intent: Intent = intent
        val content: String? = intent.getStringExtra("content")
        val title: String? = intent.getStringExtra("title")
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val contentTextView: TextView = findViewById(R.id.contentTextView)
        scrollView = findViewById(R.id.scrollView)

        
        val selectedTextSize = sharedPreferences.getFloat("selectedTextSize", -1f)
        if (selectedTextSize != -1f) {
            contentTextView.textSize = selectedTextSize
        }

// Предварительная загрузка анимации
        actionRelative.alpha = 0f
        actionRelative.animate().alpha(1f).setDuration(0).start()

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                
                val controller = ViewCompat.getWindowInsetsController(scrollView)

                // Видны ли панели
                if (controller?.systemBarsBehavior == WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH) {
                    
                    scrollView.postDelayed({
                        // Скрыть системные панели
                        controller.hide(WindowInsetsCompat.Type.systemBars())
                        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
                    }, 200)

                    // Скрыть пользовательский интерфейс с анимацией
                    actionRelative.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .withEndAction { actionRelative.visibility = View.GONE }
                        .start()
                } else {

                    scrollView.postDelayed({
                        // Показать системные панели
                        controller?.show(WindowInsetsCompat.Type.systemBars())
                        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
                    }, 0)

                    // Показать пользовательский интерфейс с анимацией
                    actionRelative.visibility = View.VISIBLE
                    actionRelative.alpha = 0f
                    actionRelative.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start()
                }
                return super.onSingleTapUp(e)
            }
        })


        scrollView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        contentTextView.text = content
        titleTextView.text = title

        val bottomSheetFragment = BottomSheetFragment()
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

        // Восстановить прогресс скроллинга
        val scrollY = sharedPreferences.getInt("scrollY_${title}", 0)
        scrollView.post { scrollView.scrollTo(0, scrollY) }

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultColor = ResourcesCompat.getColor(resources, R.color.darkButtonColor, null)
        val themeColor = sharedPref.getInt("theme_color", defaultColor)
        onThemeSelected(themeColor)
    }

    override fun onPause() {
        super.onPause()

        // Сохранить прогресс скроллинга
        val title: String? = intent.getStringExtra("title")
        val scrollY = scrollView.scrollY
        sharedPreferences.edit().putInt("scrollY_${title}", scrollY).apply()
    }

    override fun onFontSelected(fontName: String) {
        val contentTextView: TextView = findViewById(R.id.contentTextView)
        applyFontToTextView(contentTextView, fontName)
        sharedPreferences.edit().putString("selectedFont", fontName).apply()
    }

    override fun onThemeSelected(theme: Int) {
        try {
            val backgroundLayout: RelativeLayout = findViewById(R.id.backgroundRelative)
            val contentTextView: TextView = findViewById(R.id.contentTextView)


            backgroundLayout.setBackgroundColor(ContextCompat.getColor(this, theme))
            scrollView.setBackgroundColor(ContextCompat.getColor(this, theme))
            contentTextView.setBackgroundColor(ContextCompat.getColor(this, theme))

            window.navigationBarColor = ContextCompat.getColor(this, theme)

            when (theme) {
                R.color.darkButtonColor -> {
                    scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkButtonColor))
                    backgroundLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkButtonColor))
                    contentTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.color.whiteButtonColor -> {
                    scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.whiteButtonColor))
                    backgroundLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.whiteButtonColor))
                    contentTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
                R.color.newspaperButtonColor -> {
                    scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.newspaperButtonColor))
                    backgroundLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.newspaperButtonColor))
                    contentTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
            }
        }catch (e:Exception){

        }

    }

    private fun applyFontToTextView(textView: TextView, fontName: String) {
        val typeface = Typeface.createFromAsset(assets, "fonts/$fontName.ttf")
        textView.typeface = typeface
    }

    fun BackClick(view: View) {
        finish()
    }
}
