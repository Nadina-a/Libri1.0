package com.example.libri

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    interface FontSelectionListener {
        fun onFontSelected(fontName: String)
    }

    interface ThemeSelectionListener {
        fun onThemeSelected(theme: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fontSpinner: Spinner = view.findViewById(R.id.spinnerFont)
        val fontArray = resources.getStringArray(R.array.font)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, fontArray)
        fontSpinner.adapter = adapter

        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedFont = sharedPreferences.getString("selectedFont", null)
        if (selectedFont != null) {
            val position = fontArray.indexOf(selectedFont)
            if (position != -1) {
                fontSpinner.setSelection(position)
            }
        }

        val buttonDark: Button = view.findViewById(R.id.buttonDark)
        buttonDark.setOnClickListener {
            (activity as? ThemeSelectionListener)?.onThemeSelected(R.color.darkButtonColor)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with(sharedPref.edit()) {
                putInt("theme_color", R.color.darkButtonColor)
                apply()
            }
        }

        val buttonWhite: Button = view.findViewById(R.id.buttonWhite)
        buttonWhite.setOnClickListener {
            (activity as? ThemeSelectionListener)?.onThemeSelected(R.color.whiteButtonColor)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with(sharedPref.edit()) {
                putInt("theme_color", R.color.whiteButtonColor)
                apply()
            }
        }

        val buttonNewspaper: Button = view.findViewById(R.id.buttonNewspaper)
        buttonNewspaper.setOnClickListener {
            (activity as? ThemeSelectionListener)?.onThemeSelected(R.color.newspaperButtonColor)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with(sharedPref.edit()) {
                putInt("theme_color", R.color.newspaperButtonColor)
                apply()
            }
        }

        fontSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedFont = fontArray[position]
                sharedPreferences.edit().putString("selectedFont", selectedFont).apply()
                (activity as? FontSelectionListener)?.onFontSelected(selectedFont)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
    }